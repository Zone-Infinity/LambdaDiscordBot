package bot.java.lambda.command.commands.images;

import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.category.Images;
import bot.java.lambda.command.type.ImageCommand;

public class CamelCommand implements ImageCommand {
    @Override
    public Images getImages() {
        return Images.CAMEL;
    }

    @Override
    public String getName() {
        return "camel";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives random image of camel";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.IMAGES;
    }
}
