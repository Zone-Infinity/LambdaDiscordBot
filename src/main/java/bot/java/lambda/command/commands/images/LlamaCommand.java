package bot.java.lambda.command.commands.images;

import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandCategory.Images;
import bot.java.lambda.command.commandType.ImageCommand;

public class LlamaCommand implements ImageCommand {
    @Override
    public Images getImages() {
        return Images.LLAMA;
    }

    @Override
    public String getName() {
        return "llama";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives random image of llama";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.IMAGES;
    }
}
