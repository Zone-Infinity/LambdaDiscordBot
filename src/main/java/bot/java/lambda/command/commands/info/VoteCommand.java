package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;

public class VoteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        // BOTRIX = https://botrix.cc/bots/752052866809593906/

    }

    @Override
    public String getName() {
        return "vote";
    }

    @Override
    public String getHelp() {
        return "Gives u the vote links for the bot";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }
}
