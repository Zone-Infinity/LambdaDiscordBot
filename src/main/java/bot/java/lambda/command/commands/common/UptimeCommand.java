package bot.java.lambda.command.commands.common;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.command.Utils;

public class UptimeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        ctx.getChannel().sendMessage("My Uptime is `"+Utils.getUptime()+"`").queue();
    }

    @Override
    public String getName() {
        return "uptime";
    }

    @Override
    public String getHelp() {
        return "Shows the current uptime of the bot";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }
}
