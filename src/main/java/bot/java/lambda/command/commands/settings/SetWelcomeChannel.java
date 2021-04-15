package bot.java.lambda.command.commands.settings;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.SettingCommand;

public class SetWelcomeChannel implements SettingCommand {
    @Override
    public void updateSetting(CommandContext ctx) {

    }

    @Override
    public String getName() {
        return "setwelcomechannel";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sets the welcome channel for this server\n" +
                "Usage: " + prefix + "setwelcomechannel <#channel/channelId>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return null;
    }
}
