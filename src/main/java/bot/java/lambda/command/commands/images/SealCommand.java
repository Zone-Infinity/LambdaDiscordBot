package bot.java.lambda.command.commands.images;

import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.category.Images;
import bot.java.lambda.command.type.ImageCommand;

public class SealCommand implements ImageCommand {
    @Override
    public Images getImages() {
        return Images.SEAL;
    }

    @Override
    public String getName() {
        return "seal";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives random image of seal";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.IMAGES;
    }
}