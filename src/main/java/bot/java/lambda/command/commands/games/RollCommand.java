package bot.java.lambda.command.commands.games;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.ICommand;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RollCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        Random random = new Random();
        ctx.getChannel().sendMessage("Rolling...").queue(
                message -> message.editMessage(
                        "" + (random.nextInt(6) + 1)
                ).queueAfter(500, TimeUnit.MILLISECONDS)
        );
    }

    @Override
    public String getName() {
        return "roll";
    }

    @Override
    public String getHelp(String prefix) {
        return "Rolls a dice for you";
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
