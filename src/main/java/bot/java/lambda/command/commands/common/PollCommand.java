package bot.java.lambda.command.commands.common;

import bot.java.lambda.Config;
import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.regex.Pattern;

import static bot.java.lambda.command.Utils.getEmojiFor;

public class PollCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();

        if(ctx.getArgs().size()<31){
            channel.sendMessage("Need at least 3 arguments").queue();
        }

        String[] split = message.getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Config.get("prefix"))+getName(), "")
                .split(";");

        String question = split[0];
        String[] options = new String[split.length-1];
        int i=0;
        for(int s=1;s<= split.length-1;s++){
            options[i] = split[s];
            i++;
        }
        StringBuilder optionSB = new StringBuilder();
        int count = 1;
        for(String option : options){
            optionSB.append(getEmojiFor(String.valueOf(count)));
            optionSB.append(option);
            count++;
        }

        channel.sendMessage(optionSB.toString()).queue();

    }

    @Override
    public String getName() {
        return "poll";
    }

    @Override
    public String getHelp() {
        return "Does a Poll\n" +
                "Usage : >poll <question> ; <option1> ; <option2> ; ....\n" +
                "Condition : There must be at least 2 options , Maximum number of Options are 9";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }
}
