package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.config.Config;
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
        List<String> replies = List.of("Nah", "I know", "No U", "ok");

        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        final String join = String.join(" ", args);

        if (join.contains("@everyone") || join.contains("@here") || join.contains("<@&")) {
            channel.sendMessage("Please don't tell me to ping some roles or everyone").queue();
            return;
        }

        for (String a : args) {
            if (messages.contains(a.toLowerCase())) {
                channel.sendMessage(replies.get(new Random().nextInt(replies.size()))).queue();
                return;
            }
        }

        if (join.toLowerCase().contains("stupid") && join.toLowerCase().contains("i")) {
            channel.sendMessage("Yeah we know").queue();
            return;
        }

        channel.sendTyping().queue();
        channel.sendMessage(join).queueAfter(join.length() * 10, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getName() {
        return "echo";
    }

    @Override
    public String getHelp(String prefix) {
        return "Echos your message\n" +
                "Usage : " + prefix + "echo <message>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.FUN;
    }
}
