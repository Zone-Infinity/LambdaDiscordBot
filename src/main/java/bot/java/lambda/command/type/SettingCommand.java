package bot.java.lambda.command.type;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.database.Setting;
import bot.java.lambda.utils.Utils;
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

            if (setting == Setting.WELCOME_BACKGROUND) {
                updateSetting(ctx);
                return;
            }

            updateSettingSilently(ctx, defaultValue);

            channel.sendMessage("`" + setting.getName() + "` changed to default : `" + defaultValue + "`").queue();
            return;
        }

        try {
            updateSetting(ctx);
        } catch (Exception e) {
            channel.sendMessage("Something Went Wrong, please try again later or Contact " + Utils.getZoneInfinityAsTag(ctx.getJDA())).queue();
            e.printStackTrace();
        }
    }

    void updateSetting(CommandContext ctx);

    void updateSettingSilently(CommandContext ctx, String setting);

    Setting getSetting();
}

