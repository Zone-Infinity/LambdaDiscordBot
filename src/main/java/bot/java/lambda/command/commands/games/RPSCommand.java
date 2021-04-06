package bot.java.lambda.command.commands.games;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandType.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RPSCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }
        String[] rps = new String[]{"rock", "r", "paper", "p", "scissors", "scissor", "s"};
        if (!Arrays.asList(rps).contains(args.get(0))) {
            channel.sendMessage("Invalid Argument\n" +
                    "try : `rock`, `paper`, `scissor`").queue();
            return;
        }
        final String s = rps[new Random().nextInt(rps.length - 1)];

        StringBuilder result = new StringBuilder(String.format("You : %s\n" +
                "Me : %s\n", args.get(0), s));

        if (args.get(0).toLowerCase().startsWith("r")) {
            result.append(s.startsWith("p") ? "I win !! :D" : "You win !! D:");
        }
        if (args.get(0).toLowerCase().startsWith("p")) {
            result.append(s.startsWith("s") ? "I win !! :D" : "You win !! D:");
        }
        if (args.get(0).toLowerCase().startsWith("s")) {
            result.append(s.startsWith("r") ? "I win !! :D" : "You win !! D:");
        }

        channel.sendMessageFormat("You : %s\n" +
                "Me : %s\n", args.get(0), s).queue(
                message -> {
                    if (s.equals(args.get(0))) {
                        message.editMessage(String.format("You : %s\n" +
                                "Me : %s\n" +
                                "It's a tie !!!", args.get(0), s)).queueAfter(1, TimeUnit.SECONDS);
                        return;
                    }
                    message.editMessage(result.toString()).queueAfter(1, TimeUnit.SECONDS);
                }
        );

    }

    @Override
    public String getName() {
        return "rps";
    }

    @Override
    public String getHelp(String prefix) {
        return "Play `Rock-Paper-Scissors` with the bot\n" +
                "Usage : " + prefix + "rps <r/p/s>\n" +
                "`Rock -> rock, r`\n" +
                "`Paper -> paper, p`\n" +
                "`Scissors -> scissor, scissors, s";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.GAME;
    }

    @Override
    public int getCoolDown() {
        return 10;
    }

}
