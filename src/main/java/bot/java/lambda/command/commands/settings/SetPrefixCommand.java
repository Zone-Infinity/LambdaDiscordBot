package bot.java.lambda.command.commands.settings;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.SettingCommand;
import bot.java.lambda.config.GuildSettings;
import bot.java.lambda.database.DatabaseManager;
import bot.java.lambda.database.Setting;

import java.util.List;

public class SetPrefixCommand implements SettingCommand {
    @Override
    public void updateSetting(CommandContext ctx) {
        final String newPrefix = String.join("", ctx.getArgs());
        updateSettingSilently(ctx, newPrefix);
        ctx.getChannel().sendMessage("New Prefix set to `" + newPrefix + "`").queue();
    }

    @Override
    public void updateSettingSilently(CommandContext ctx, String setting) {
        final long guildId = ctx.getGuild().getIdLong();
        GuildSettings.PREFIXES.put(guildId, setting);
        DatabaseManager.INSTANCE.setPrefix(guildId, setting);
    }

    @Override
    public Setting getSetting() {
        return Setting.PREFIX;
    }

    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sets the prefix for this server\n" +
                "Usage: " + prefix + "setprefix <prefix>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.SETTINGS;
    }

    @Override
    public List<String> getAliases() {
        return List.of("sp", "prefix");
    }
}
