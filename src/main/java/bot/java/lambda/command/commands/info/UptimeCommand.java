package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;
import bot.java.lambda.utils.Utils;

@CommandHandler
public class UptimeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        ctx.getChannel().sendMessage("My Uptime is `" + Utils.getUptime() + "`").queue();
    }

    @Override
    public String getName() {
        return "uptime";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows the current uptime of the bot";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }
}
