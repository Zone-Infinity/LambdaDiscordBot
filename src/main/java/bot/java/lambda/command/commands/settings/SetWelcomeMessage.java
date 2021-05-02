package bot.java.lambda.command.commands.settings;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.SettingCommand;
import bot.java.lambda.config.GuildSettings;
import bot.java.lambda.database.DatabaseManager;
import bot.java.lambda.database.Setting;
import bot.java.lambda.database.WelcomeSetting;

import java.util.List;

public class SetWelcomeMessage implements SettingCommand {

    @Override
    public void updateSetting(CommandContext ctx) {
        final String newMessage = ctx.getMessage().getContentRaw().replaceFirst("(?i)(>setwelcomemessage|>welcomemessage)", "");
        updateSettingSilently(ctx, newMessage);
        ctx.getChannel().sendMessage("New Welcome Message set to : ```" + newMessage + "```").queue();
    }

    @Override
    public void updateSettingSilently(CommandContext ctx, String setting) {
        final long guildId = ctx.getGuild().getIdLong();
        final WelcomeSetting welcomeSetting = GuildSettings.WELCOME_SETTINGS.computeIfAbsent(guildId, DatabaseManager.INSTANCE::getWelcomeSettings);
        GuildSettings.WELCOME_SETTINGS.put(guildId, welcomeSetting.withMessage(setting));
        DatabaseManager.INSTANCE.setWelcomeMessage(guildId, setting);
    }

    @Override
    public Setting getSetting() {
        return Setting.WELCOME_MESSAGE;
    }

    @Override
    public String getName() {
        return "setwelcomemessage";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sets the welcome message for this server\n" +
                "Usage: " + prefix + "setwelcomemessage <message>\n" +
                "Variables: " +
                "   {user} -> Name of the user joined\n" +
                "   {user.mention} -> Mention user joined\n" +
                "   {guild} -> Name of this server";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.SETTINGS;
    }

    @Override
    public List<String> getAliases() {
        return List.of("welcomemessage");
    }
}
