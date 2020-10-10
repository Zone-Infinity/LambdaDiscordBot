package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class EchoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
                                                                                                                                                                List<String> messages = List.of("fuck", "bitch", "porn", "ass", "boob");
        List<String> replies = List.of("Nah", "We know", "I know", "No U", "ok");

        if(args.isEmpty()){
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        for(String a : args){
            if(a.toLowerCase().contains("stupid") && a.toLowerCase().contains("i")){
                channel.sendMessage("Yeah we know").queue();
                return;
            }
            if(messages.contains(a.toLowerCase())){
                channel.sendMessage(replies.get(new Random().nextInt(replies.size()))).queue();
                return;
            }
        }

        final String join = String.join(" ", args);

        channel.sendTyping().queue();
        channel.sendMessage(join).queueAfter(join.length()*50, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getName() {
        return "echo";
    }

    @Override
    public String getHelp() {
        return "Echos your message";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.FUN;
    }
}
