package bot.java.lambda.command.commands.utils;

import bot.java.lambda.command.HelpCategory;

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
