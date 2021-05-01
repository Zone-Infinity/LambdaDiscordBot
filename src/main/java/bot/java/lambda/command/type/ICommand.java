package bot.java.lambda.command.type;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import org.apache.commons.collections4.map.HashedMap;

import java.util.List;
import java.util.Map;

public interface ICommand {
    Map<Long, Long> coolDowns = new HashedMap<>();

    void handle(CommandContext ctx);

    String getName();

    String getHelp(String prefix);

    HelpCategory getHelpCategory();

    default int getCoolDown() {
        return 5;
    }

    default void addCoolDown(long userId) {
        coolDowns.put(userId, System.currentTimeMillis());
    }

    default List<String> getAliases() {
        return List.of();
    }
}
