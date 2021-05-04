package bot.java.lambda.command.commands.utils;

import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.category.ImageUtil;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ImageUtilCommand;

@CommandHandler
public class DarkenCommand implements ImageUtilCommand {
    @Override
    public ImageUtil getUtil() {
        return ImageUtil.DARKEN;
    }

    @Override
    public String getName() {
        return "darken";
    }

    @Override
    public String getHelp(String prefix) {
        return "Darkens the supplied Image";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.UTIL;
    }
}
