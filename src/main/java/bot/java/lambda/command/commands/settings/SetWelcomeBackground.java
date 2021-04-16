package bot.java.lambda.command.commands.settings;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.SettingCommand;
import bot.java.lambda.database.Setting;

import java.util.List;

public class SetWelcomeBackground implements SettingCommand {
    @Override
    public void updateSetting(CommandContext ctx) {

    }

    @Override
    public void updateSettingSilently(CommandContext ctx) {

    }

    @Override
    public Setting getSetting() {
        return Setting.WELCOME_BACKGROUND;
    }

    @Override
    public String getName() {
        return "setwelcomebackground";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sets the welcome background image for this server\n" +
                "Usage: " + prefix + "setwelcomebackground " +
                "[With Background Image Uploaded / Upload Image After Using The Command] (Use Command For More)\n";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.SETTINGS;
    }

    @Override
    public List<String> getAliases() {
        return List.of("welcomebackground");
    }
}
