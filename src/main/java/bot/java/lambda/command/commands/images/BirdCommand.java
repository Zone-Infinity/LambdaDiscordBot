package bot.java.lambda.command.commands.images;

import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.category.Images;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ImageCommand;

@CommandHandler
public class BirdCommand implements ImageCommand {
    @Override
    public Images getImages() {
        return Images.BIRD;
    }

    @Override
    public String getName() {
        return "bird";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives random image of bird";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.IMAGES;
    }
}
