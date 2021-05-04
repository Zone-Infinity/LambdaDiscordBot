package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;
import bot.java.lambda.utils.Utils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

@CommandHandler
public class EmojifyCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }
        final String join = String.join(" ", args);
        int len = join.length();
        if (len > 200) {
            channel.sendMessage("Text Exceeds 200 Characters").queue();
            return;
        }
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (Utils.getEmojiFor(String.valueOf(join.charAt(i))) == null) {
                output.append(join.charAt(i));
            }
            output.append(Utils.getEmojiFor(String.valueOf(join.charAt(i))));
            output.append("\u200B");
        }
        channel.sendMessage(output).queue();
    }

    @Override
    public String getName() {
        return "emojify";
    }

    @Override
    public String getHelp(String prefix) {
        return "Emojifies your text\n" +
                "Usage : " + prefix + "emojify <text>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.FUN;
    }
}
