package bot.java.lambda.command.commands.common;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.config.Config;

import java.util.List;
import java.util.OptionalInt;
import java.util.Random;

public class RandomCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        try {
            final List<String> args = ctx.getArgs();

            if (args.isEmpty()) {
                ctx.getChannel().sendMessage("Missing Arguments").queue();
                return;
            }

            Random random = new Random();

            if (args.size() == 1) {
                final OptionalInt num = random.ints(0, Integer.parseInt(args.get(0))).findFirst();
                ctx.getChannel().sendMessage("" + (num.isPresent() ? num.getAsInt() : "Something went wrong")).queue();
                return;
            }

            int a = Integer.parseInt(args.get(0));
            final int b = Integer.parseInt(args.get(1));

            final OptionalInt num = a < b ? random.ints(a, b).findFirst() : random.ints(b, a).findFirst();

            ctx.getChannel().sendMessage("" + (num.isPresent() ? num.getAsInt() : "Something went wrong")).queue();
        } catch (NumberFormatException e) {
            ctx.getChannel().sendMessage("Invalid Input! Provide Numbers").queue();
            e.fillInStackTrace();
        }
    }

    @Override
    public String getName() {
        return "random";
    }

    @Override
    public String getHelp() {
        return "Gives you random number between your bounds\n" +
                "Usage : " + Config.get("prefix") + "random <number> <number>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }
}
