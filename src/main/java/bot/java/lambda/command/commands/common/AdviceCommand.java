package bot.java.lambda.command.commands.common;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.TextChannel;

public class AdviceCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        WebUtils.ins.getJSONObject("https://api.adviceslip.com/advice").async(
            (json) -> {
                final JsonNode slip = json.get("slip");
                final String advice = slip.get("advice").asText();
                channel.sendMessageFormat(advice).queue();
            }
        );
    }

    @Override
    public String getName() {
        return "advice";
    }

    @Override
    public String getHelp() {
        return "Gives a advice to you";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }
}
