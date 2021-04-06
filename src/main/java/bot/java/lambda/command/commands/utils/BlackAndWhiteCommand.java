package bot.java.lambda.command.commands.utils;

import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandCategory.ImageUtil;
import bot.java.lambda.command.commandType.ImageUtilCommand;

public class BlackAndWhiteCommand implements ImageUtilCommand {
    @Override
    public ImageUtil getUtil() {
        return ImageUtil.BLACK_AND_WHITE;
    }

    @Override
    public String getName() {
        return "b&w";
    }

    @Override
    public String getHelp(String prefix) {
        return "Black and Whites your image";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.UTIL;
    }
}
