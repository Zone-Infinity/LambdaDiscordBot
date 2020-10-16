package bot.java.lambda.command.commands.common;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.TextChannel;

public class SimplifyCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        StringBuilder expression = new StringBuilder();

        for (char c : String.join(" ", ctx.getArgs()).toCharArray()) {
            if (c == '/') {
                expression.append("(over)");
                continue;
            }
            expression.append(c);
        }

        WebUtils.ins.getJSONObject("https://newton.now.sh/api/v2/simplify/" + expression).async(
                (json) -> {
                    final String result = json.get("result").asText();
                    channel.sendMessageFormat("Expression : %s\n" +
                            "Result : %s", String.join(" ", ctx.getArgs()), result).queue();
                }
        );
    }

    @Override
    public String getName() {
        return "simplify";
    }

    @Override
    public String getHelp() {
        return "Simplifies your expression";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }
}
