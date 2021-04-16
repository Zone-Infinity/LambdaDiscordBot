package bot.java.lambda.command.type;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.database.Setting;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public interface SettingCommand extends ICommand {
    @Override
    default void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        final Member member = ctx.getMember();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            channel.sendMessage("You don't have MANAGE SERVER Permission").queue();
            return;
        }

        if (args.isEmpty()) {
            final Setting setting = getSetting();
            final String defaultValue = setting.getDefaultValue();

            updateSettingSilently(ctx);

            channel.sendMessage("`" + setting.getName() + "` changed to default : `" + defaultValue + "`").queue();
            return;
        }

        updateSetting(ctx);
    }

    void updateSetting(CommandContext ctx);

    void updateSettingSilently(CommandContext ctx);

    Setting getSetting();
}

