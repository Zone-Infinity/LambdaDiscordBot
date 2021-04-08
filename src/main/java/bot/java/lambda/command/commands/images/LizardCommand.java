package bot.java.lambda.command.commands.images;

import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.category.Images;
import bot.java.lambda.command.type.ImageCommand;

public class LizardCommand implements ImageCommand {
    @Override
    public Images getImages() {
        return Images.FOX;
    }

    @Override
    public String getName() {
        return "lizard";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives random image of lizard";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.IMAGES;
    }
}
