package bot.java.lambda.command.commandType;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.HelpCategory;

import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx);

    String getName();

    String getHelp(String prefix);

    HelpCategory getHelpCategory();

    default int getCoolDown() {
        return 5;
    }

    default List<String> getAliases() {
        return List.of();
    }
}
