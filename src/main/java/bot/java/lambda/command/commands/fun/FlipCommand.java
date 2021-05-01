package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.ICommand;

public class FlipCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

    }

    @Override
    public String getName() {
        return "flip";
    }

    @Override
    public String getHelp(String prefix) {
        return "Flips the provided text upside down\n" +
                "Usage: " + prefix + " flip <text>\n" +
                "Note: Only flips some characters";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return null;
    }
}
