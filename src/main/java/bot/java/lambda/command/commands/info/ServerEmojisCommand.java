package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Emote;

public class ServerEmojisCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final StringBuilder animated = new StringBuilder();
        final StringBuilder simple = new StringBuilder();
        for (Emote e : ctx.getGuild().getEmotes()) {
            if (e.isAnimated()) {
                animated.append(e.getAsMention()).append(" ");
            } else {
                simple.append(e.getAsMention()).append(" ");
            }
        }
        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed()
                .setTitle("Emote in this server")
                .addField("Animated", animated.toString(), false)
                .addField("Simple", simple.toString(), false)
                .build()).queue();
    }

    @Override
    public String getName() {
        return "serveremojis";
    }

    @Override
    public String getHelp() {
        return "Shows the emojis in the server";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }
}
