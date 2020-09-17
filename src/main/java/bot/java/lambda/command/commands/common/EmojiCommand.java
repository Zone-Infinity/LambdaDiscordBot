package bot.java.lambda.command.commands.common;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

public class EmojiCommand implements ICommand {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        Guild guild = ctx.getJDA().getGuildById(740228383446925402L);
        final List<Emote> emotes = guild.getEmotes();
        StringBuilder allEmotes = new StringBuilder();
        for (Emote emote : emotes){
            allEmotes.append(emote.getAsMention())
                    .append(" - ")
                    .append(emote.getName())
                    .append("\n");
        }
        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed()
                                .setTitle("Emojis you can use")
                                .setDescription(allEmotes)
                                .build()).queue();
    }

    @Override
    public String getName() {
        return "emojis";
    }

    @Override
    public String getHelp() {
        return "Shows you the list of Emojis of the Bot";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }

    @Override
    public List<String> getAliases() {
        return List.of("emotes","allEmoji");
    }
}
