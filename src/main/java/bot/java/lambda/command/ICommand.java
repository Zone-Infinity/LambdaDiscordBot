package bot.java.lambda.command;

import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx);

    String getName();

    String getHelp();

    HelpCategory getHelpCategory();

    default int getCoolDown() {
        return 5;
    }

    default List<String> getAliases() {
        return List.of();
    }
}
