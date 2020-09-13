package bot.java.lambda.command.commands.common;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;

import java.util.List;
import java.util.Random;

public class RandomCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();

        if(args.size() < 2){
            ctx.getChannel().sendMessage("Missing Arguments").queue();
            return;
        }

        Random random = new Random();
        ctx.getChannel().sendMessage(""+random.ints(Integer.parseInt(args.get(0)), Integer.parseInt(args.get(1)))).queue();
    }

    @Override
    public String getName() {
        return "random";
    }

    @Override
    public String getHelp() {
        return "Gives you random number between your bounds\n" +
                "Usage : <prefix> random <number> <number>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }
}
