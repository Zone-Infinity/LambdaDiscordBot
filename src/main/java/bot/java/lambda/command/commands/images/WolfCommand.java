package bot.java.lambda.command.commands.images;

import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandCategory.Images;
import bot.java.lambda.command.commandType.ImageCommand;

public class WolfCommand implements ImageCommand {
    @Override
    public Images getImages() {
        return Images.WOLF;
    }

    @Override
    public String getName() {
        return "wolf";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives random image of wolf";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.IMAGES;
    }
}
