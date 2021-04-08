package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Random;

public class FlipCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final Random random = new Random();
        ctx.getChannel().sendMessage(new EmbedBuilder()
                .setColor(random.nextInt(256 * 256 * 256))
                .setImage(random.nextBoolean() ?
                        "https://media.discordapp.net/attachments/741153014080864267/767282060997165056/heads.png" :
                        "https://media.discordapp.net/attachments/741153014080864267/767282063656222721/tails.png")
                .build()).queue();

    }

    @Override
    public String getName() {
        return "flip";
    }

    @Override
    public String getHelp(String prefix) {
        return "Flips a coin for you";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.FUN;
    }
}
