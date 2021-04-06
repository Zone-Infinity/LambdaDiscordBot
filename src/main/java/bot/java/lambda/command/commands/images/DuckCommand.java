package bot.java.lambda.command.commands.images;

import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandCategory.Images;
import bot.java.lambda.command.commandType.ImageCommand;

public class DuckCommand implements ImageCommand {
    @Override
    public Images getImages() {
        return Images.DUCK;
    }

    @Override
    public String getName() {
        return "duck";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives random image of duck";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.IMAGES;
    }
}
