package bot.java.lambda.command.commands.music;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandType.ICommand;

import java.util.List;

public class RemoveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        // TODO: make this command
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getHelp(String prefix) {
        return "Removes a track";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }
}
