package bot.java.lambda.command.commands.images;

import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandCategory.Images;
import bot.java.lambda.command.commandType.ImageCommand;

public class AlpacaCommand implements ImageCommand {
    @Override
    public Images getImages() {
        return Images.ALPACA;
    }

    @Override
    public String getName() {
        return "alpaca";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives random image of alpaca";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.IMAGES;
    }
}
