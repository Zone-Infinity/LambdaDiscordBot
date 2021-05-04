package bot.java.lambda.command.commands.games;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;

import java.util.List;
import java.util.Random;

@CommandHandler
public class _8BallCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage("Missing Arguments").queue();
            return;
        }

        List<String> responses = List.of(
                "It is certain",
                "It is decidedly so",
                "Without a doubt",
                "Yes - definitely",
                "You may rely on it",
                "As I see it, yes",
                "Most Likely",
                "Outlook good",
                "Yes",
                "Signs point to yes",
                "Reply hazy, try again",
                "Ask again later",
                "Better not tell you now",
                "Cannot predict now",
                "Concentrate and ask again",
                "Don't count on it",
                "My reply is no",
                "Outlook not so good",
                "No",
                "Very doubtful"
        );
        ctx.getChannel().sendMessageFormat("Question : %s\n" +
                "Answer : %s", String.join(" ", ctx.getArgs()), responses.get(new Random().nextInt(responses.size()))).queue();
    }

    @Override
    public String getName() {
        return "8ball";
    }

    @Override
    public String getHelp(String prefix) {
        return "Ask the magic 8-ball a question and receive an answer\n" +
                "Usage : " + prefix + "8ball <question>";
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
