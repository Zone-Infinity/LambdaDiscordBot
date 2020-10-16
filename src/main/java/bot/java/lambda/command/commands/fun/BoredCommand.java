package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.TextChannel;

public class BoredCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        WebUtils.ins.getJSONObject("http://www.boredapi.com/api/activity/").async(
                (json) -> channel.sendMessage(json.get("activity").asText()).queue()
        );
    }

    @Override
    public String getName() {
        return "bored";
    }

    @Override
    public String getHelp() {
        return "Sends random things to do when you are bored";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.FUN;
    }
}
