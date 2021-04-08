package bot.java.lambda.command.commands.utils;

import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.category.ImageUtil;
import bot.java.lambda.command.type.ImageUtilCommand;

public class PixelateCommand implements ImageUtilCommand {
    @Override
    public ImageUtil getUtil() {
        return ImageUtil.PIXELATE;
    }

    @Override
    public String getName() {
        return "pixelate";
    }

    @Override
    public String getHelp(String prefix) {
        return "Pixelates the given Image";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.UTIL;
    }
}
