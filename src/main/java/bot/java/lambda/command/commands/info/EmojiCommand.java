package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
        List<Guild> guilds = new ArrayList<>();
        for (Guild guild : ctx.getJDA().getGuilds()) {
            if (guild.getEmotes().size() > 15) {
                guilds.add(guild);
            }
        }
        int count = 0;
        int page = 0;
        for (Guild guild : guilds) {
            final List<Emote> emotes = guild.getEmotes();
            for (Emote emote : emotes) {
                try {
                    if (emote.isAnimated() && !Objects.equals(emote.getGuild(), server)) {
                        continue;
                    }
                    listOfAllEmote.get(page).append(emote.getAsMention())
                            .append(" - ").append("`").append(emote.getName()).append("`")
                            .append("\n");
                } catch (IndexOutOfBoundsException e) {
                    e.fillInStackTrace();
                    listOfAllEmote.add(new StringBuilder());
                }
                if (count % 10 == 1 && count != 1) {
                    page++;
                }
                count++;
            }
        }

        if (args.isEmpty()) {
            ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed()
                    .setTitle("Emojis you can use : Page 1 out of " + listOfAllEmote.size())
                    .setThumbnail(null)
                    .setDescription(listOfAllEmote.get(0))
                    .build()).queue(
                    message -> {
                        message.addReaction("⬅").queue();
                        message.addReaction("🛑").queue();
                        message.addReaction("➡").queue();
                        emojiPageWaiter(message, ctx);
                    }
            );
            return;
        }

        try {
            ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed()
                    .setTitle("Emojis you can use : Page " + (Integer.parseInt(args.get(0))) + " out of " + listOfAllEmote.size())
                    .setThumbnail(null)
                    .setDescription(listOfAllEmote.get((Integer.parseInt(args.get(0))) - 1))
                    .build()).queue(
                    message -> {
                        message.addReaction("⬅").queue();
                        message.addReaction("🛑").queue();
                        message.addReaction("➡").queue();
                        emojiPageWaiter(message, ctx);
                    }
            );
        } catch (NumberFormatException e) {
            e.fillInStackTrace();
            ctx.getChannel().sendMessage("Pls provide a page number").queue();
        } catch (IndexOutOfBoundsException e) {
            e.fillInStackTrace();
            ctx.getChannel().sendMessage("Only " + listOfAllEmote.size() + " page exist").queue();
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
                    if (!e.getUser().equals(ctx.getAuthor())) {
                        return;
                    }

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
                        //e.getReaction().removeReaction(e.getUser()).queue();
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
