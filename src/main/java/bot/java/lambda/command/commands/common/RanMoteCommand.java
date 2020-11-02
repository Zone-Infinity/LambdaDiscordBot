package bot.java.lambda.command.commands.common;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import net.dv8tion.jda.api.entities.Emote;

import java.util.List;
import java.util.Random;

public class RanMoteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<Emote> emotes = ctx.getJDA().getEmotes();
        ctx.getChannel().sendMessage(""+emotes.get(new Random().nextInt(emotes.size())).getAsMention()).queue();
    }

    @Override
    public String getName() {
        return "ranmote";
    }

    @Override
    public String getHelp() {
        return "Sends a random emote";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }
}
