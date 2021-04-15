package bot.java.lambda.command.commands.settings;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.SettingCommand;
import bot.java.lambda.config.GuildSettings;
import bot.java.lambda.database.DatabaseManager;
import bot.java.lambda.database.WelcomeSetting;

import java.util.List;

public class SetWelcomeMessage implements SettingCommand {

    @Override
    public void updateSetting(CommandContext ctx) {
        final long guildId = ctx.getGuild().getIdLong();

        final String newMessage = ctx.getMessage().getContentRaw().replaceFirst("(?i)(>setwelcomemessage|>welcomemessage)", "");

        final WelcomeSetting welcomeSetting = GuildSettings.WELCOME_SETTINGS.get(guildId);

        GuildSettings.WELCOME_SETTINGS.put(guildId, welcomeSetting.setWelcomeMessage(newMessage));

        DatabaseManager.INSTANCE.setWelcomeMessage(guildId, newMessage);

        ctx.getChannel().sendMessage("New Prefix set to : ```" + newMessage + "```").queue();
    }

    @Override
    public String getName() {
        return "setwelcomemessage";
    }

    @Override
    public String getHelp(String prefix) {
        return "null";
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
