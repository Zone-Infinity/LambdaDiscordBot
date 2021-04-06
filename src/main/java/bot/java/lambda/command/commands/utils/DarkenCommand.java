package bot.java.lambda.command.commands.utils;

import bot.java.lambda.command.HelpCategory;

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
