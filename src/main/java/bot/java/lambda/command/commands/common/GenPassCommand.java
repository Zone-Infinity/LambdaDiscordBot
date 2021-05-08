package bot.java.lambda.command.commands.common;

import bot.java.lambda.Constant;
import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@CommandHandler
public class GenPassCommand implements ICommand {
    private static final Random RNG = new Random();

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();

        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        try {
            int len = Integer.parseInt(args.get(0));

            if (len >= 51) {
                ctx.getMessage().addReaction(Constant.Emote.LAMBDA_FAILURE.asReaction).queue();
                channel.sendMessage("You don't need password exceeding 50 <:Wot:755715077029625916>").queue();
                return;
            }

            String password = IntStream.generate(RNG::nextInt)
                    .limit(len)
                    .mapToObj(i -> (char) (Math.abs(i) % 93 + 33))
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();

            ctx.getMessage().addReaction(Constant.Emote.LAMBDA_SUCCESS.asReaction).queue();
            if (args.size() == 1 || !"DM".equalsIgnoreCase(args.get(1))) {
                channel.sendMessage("Here's your Pass - \n``` " + password + " ```").queue();
            } else {
                ctx.getAuthor().openPrivateChannel()
                        .queue(dc -> dc.sendMessage("Here's your Pass - \n```" + password + "```").queue());
            }
        } catch (NumberFormatException e) {
            e.fillInStackTrace();
            ctx.getMessage().addReaction(Constant.Emote.LAMBDA_FAILURE.asReaction).queue();
            channel.sendMessage("Enter a number to specify the length !! ").queue();
        }
    }

    @Override
    public String getName() {
        return "genpass";
    }

    @Override
    public String getHelp(String prefix) {
        return """
                Generate Random password for you
                Usage : %sgenPass <length>
                        %sgenPass <length> dm
                """.formatted(prefix, prefix);
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }

    @Override
    public List<String> getAliases() {
        return List.of("generatePass", "password", "genPass");
    }

    @Override
    public int getCoolDown() {
        return 15;
    }
}
