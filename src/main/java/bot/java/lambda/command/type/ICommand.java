package bot.java.lambda.command.type;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import org.apache.commons.collections4.map.HashedMap;

import java.util.List;
import java.util.Map;

public interface ICommand {
    Map<String, Map<Long, Long>> coolDowns = new HashedMap<>();

    void handle(CommandContext ctx);

    String getName();

    String getHelp(String prefix);

    HelpCategory getHelpCategory();

    default int getCoolDown() {
        return 5;
    }

    default boolean containsCoolDown(long userId) {
        return coolDowns.computeIfAbsent(this.getName(), name -> new HashedMap<>()).containsKey(userId);
    }

    default long getCoolDown(long userId) {
        return coolDowns.computeIfAbsent(this.getName(), name -> new HashedMap<>()).computeIfAbsent(userId, user -> System.currentTimeMillis());
    }

    default void addCoolDown(long userId) {
        final Map<Long, Long> cmdCoolDowns = coolDowns.get(this.getName());
        cmdCoolDowns.put(userId, System.currentTimeMillis());
        coolDowns.put(this.getName(), cmdCoolDowns);
    }

    default List<String> getAliases() {
        return List.of();
    }
}
