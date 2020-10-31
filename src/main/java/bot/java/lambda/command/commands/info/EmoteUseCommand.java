package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.utils.Utils;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class EmoteUseCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        final Emote emote = Utils.searchEmote(ctx, args.get(0));
        if (emote == null) {
            channel.sendMessage("No Emote Found").queue();
            return;
        }
        channel.sendMessage(emote.getAsMention()).queue();
    }

    @Override
    public String getName() {
        return "emote";
    }

    @Override
    public String getHelp() {
        return "I use a server emoji that you provided\n" +
                "Usage : >emoji <name>\n" +
                "If u don't know the emoji names , do >emojis";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }
}
