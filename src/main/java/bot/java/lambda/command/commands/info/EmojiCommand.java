package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@CommandHandler
public class EmojiCommand implements ICommand {
    EventWaiter waiter;
    List<StringBuilder> listOfAllEmote = new ArrayList<>();
    int pageNumber = 1;

    public EmojiCommand(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void handle(CommandContext ctx) {
        listOfAllEmote.clear();
        pageNumber = 1;
        final Guild server = ctx.getGuild();
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        final List<Guild> guilds = ctx.getJDA().getGuilds().stream().filter(g -> g.getEmotes().size() > 15).collect(Collectors.toList());
        int count = 0;
        StringBuilder pageBuilder = new StringBuilder();
        for (final Guild guild : guilds) {
            final boolean notServer = !Objects.equals(guild, server);
            final List<Emote> eligible = guild.getEmotes();
            eligible.removeIf(e -> e.isAnimated() && notServer);
            for (final Emote emote : eligible) {
                pageBuilder.append(emote.getAsMention()).append(" - `").append(emote.getName()).append("`\n");
                ++count;
                if (count % 10 == 0) {
                    listOfAllEmote.add(pageBuilder);
                    pageBuilder = new StringBuilder();
                }
            }
        }
        if (count % 10 != 0) {
            listOfAllEmote.add(pageBuilder);
        }

        try {
            final int pageNum = args.isEmpty() ? 1 : Integer.parseInt(args.get(0));
            final int totalPages = listOfAllEmote.size();
            if (totalPages < pageNum) {
                channel.sendMessage("Page " + pageNum + " doesn't exist.").queue();
                return;
            }
            channel.sendMessage(EmbedUtils.getDefaultEmbed()
                    .setTitle("Emojis you can use : Page " + pageNum + " out of " + totalPages)
                    .setThumbnail(null)
                    .setDescription(listOfAllEmote.get(pageNum - 1))
                    .build())
                    .queue(message -> {
                        message.addReaction("⬅").queue();
                        message.addReaction("🛑").queue();
                        message.addReaction("➡").queue();
                        emojiPageWaiter(message, ctx);
                    });
        } catch (final NumberFormatException e) {
            e.fillInStackTrace();
            channel.sendMessage("Please provide a number.").queue();
        }
    }

    private void emojiPageWaiter(Message message, CommandContext ctx) {
        waiter.waitForEvent(
                GuildMessageReactionAddEvent.class,
                e -> e.getUser().equals(ctx.getAuthor())
                        && e.getChannel().equals(ctx.getChannel())
                        && !e.getMessageId().equals(ctx.getMessage().getId()),
                e -> {
                    final String asReactionCode = e.getReactionEmote().getAsReactionCode();
                    if (!asReactionCode.equals("🛑")) {
                        if (asReactionCode.equals("➡")) {
                            pageNumber++;
                            if (pageNumber > (listOfAllEmote.size())) {
                                pageNumber--;
                                emojiPageWaiter(message, ctx);
                                return;
                            }
                            message.editMessage(EmbedUtils.getDefaultEmbed()
                                    .setTitle("Emojis you can use : Page " + (pageNumber + " out of " + listOfAllEmote.size()))
                                    .setThumbnail(null)
                                    .setDescription(listOfAllEmote.get(pageNumber - 1))
                                    .build()).queue();
                        }
                        if (asReactionCode.equals("⬅")) {
                            pageNumber--;
                            if (pageNumber < 1) {
                                pageNumber++;
                                emojiPageWaiter(message, ctx);
                                return;
                            }
                            message.editMessage(EmbedUtils.getDefaultEmbed()
                                    .setTitle("Emojis you can use : Page " + (pageNumber + " out of " + listOfAllEmote.size()))
                                    .setThumbnail(null)
                                    .setDescription(listOfAllEmote.get(pageNumber - 1))
                                    .build()).queue();
                        }
                        emojiPageWaiter(message, ctx);
                        try {
                            e.getReaction().removeReaction(e.getUser()).queue();
                        } catch (InsufficientPermissionException ignored) {
                        }
                        return;
                    }
                    message.delete().queue();
                    ctx.getMessage().addReaction(":TickYes:755716208191602738").queue();
                }, 30, TimeUnit.SECONDS, () -> message.clearReactions().queue()
        );
    }

    @Override
    public String getName() {
        return "emojis";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows you the list of Emojis of the Bot";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }

    @Override
    public List<String> getAliases() {
        return List.of("emotes", "allemoji");
    }
}
