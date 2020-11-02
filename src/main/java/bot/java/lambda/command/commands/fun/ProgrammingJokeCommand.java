package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Collections;
import java.util.List;

public class ProgrammingJokeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        WebUtils.ins.getJSONObject("https://sv443.net/jokeapi/v2/joke/Programming").async(
                (json) -> {
                    if (json.get("error").asBoolean()) {
                        channel.sendMessage("Error").queue();
                        return;
                    }
                    if (!json.has("setup")) {
                        final String joke = json.get("joke").asText();
                        channel.sendMessage("```-> " + joke + "```").queue();
                        return;
                    }
                    final String setup = json.get("setup").asText();
                    final String delivery = json.get("delivery").asText();
                    channel.sendMessage("```-> " + setup + "\n-> " + delivery + "```").queue();
                }
        );
    }

    @Override
    public String getName() {
        return "programmingjoke";
    }

    @Override
    public String getHelp() {
        return "Sends a random programming joke";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.FUN;
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("pjoke");
    }
}
