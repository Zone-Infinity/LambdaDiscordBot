package bot.java.lambda.command.commands.utils;

import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.category.ImageUtil;
import bot.java.lambda.command.type.ImageUtilCommand;

public class BlurCommand implements ImageUtilCommand {
    @Override
    public ImageUtil getUtil() {
        return ImageUtil.BLUR;
    }

    @Override
    public String getName() {
        return "blur";
    }

    @Override
    public String getHelp(String prefix) {
        return "Blurs the supplied Image";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.UTIL;
    }
}
