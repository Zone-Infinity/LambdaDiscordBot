package bot.java.lambda.command.commands.info;

import bot.java.lambda.Constant;
import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

@CommandHandler
public class PingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                (ping) -> {
                    final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                            .setTitle("PING")
                            .addField("\uD83E\uDDE1 Rest ping", ping + "ms", true)
                            .addField(Constant.Emote.PING_PONG.emote + " WS ping", (jda.getGatewayPing() == 69 ? 70 : jda.getGatewayPing()) + "ms", true);
                    ctx.getChannel().sendMessage(embed.build()).queue();
                }
        );
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows the current ping from the bot to the discord servers";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }
}
