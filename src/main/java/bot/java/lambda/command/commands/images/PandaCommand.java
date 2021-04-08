package bot.java.lambda.command.commands.images;

import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.category.Images;
import bot.java.lambda.command.type.ImageCommand;

public class PandaCommand implements ImageCommand {
    @Override
    public Images getImages() {
        return Images.PANDA;
    }

    @Override
    public String getName() {
        return "panda";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives random image of panda";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.IMAGES;
    }
}
