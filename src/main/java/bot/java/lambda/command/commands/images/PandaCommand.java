package bot.java.lambda.command.commands.images;

import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandCategory.Images;
import bot.java.lambda.command.commandType.ImageCommand;

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
