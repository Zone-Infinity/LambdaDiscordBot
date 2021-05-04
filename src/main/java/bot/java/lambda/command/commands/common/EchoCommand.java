package bot.java.lambda.command.commands.common;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;
import bot.java.lambda.utils.Discord;
import bot.java.lambda.utils.StringUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@CommandHandler
public class EchoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();

        List<String> replies = List.of("Nah", "I know", "No U", "ok");

        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        String join = Discord.replaceAllMention(ctx.getMessage()).replaceFirst(">echo", "");


        if (StringUtils.hasProfanity(ctx.getMessage().getContentRaw())) {
            channel.sendMessage(replies.get(new Random().nextInt(replies.size()))).queue();
            return;
        }

        if ((join.toLowerCase().contains("stupid") || join.toLowerCase().contains("dumb")) && join.toLowerCase().contains("i")) {
            channel.sendMessage("Yeah we know").queue();
            return;
        }

        channel.sendTyping().queue();
        channel.sendMessage(join).queueAfter(join.length() * 10L, TimeUnit.MILLISECONDS);
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
        return HelpCategory.COM;
    }

    @Override
    public int getCoolDown() {
        return 10;
    }
}
