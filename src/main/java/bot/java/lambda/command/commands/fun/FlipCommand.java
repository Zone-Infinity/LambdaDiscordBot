package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;
import bot.java.lambda.config.Config;
import bot.java.lambda.utils.Discord;
import bot.java.lambda.utils.StringUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

@CommandHandler
public class FlipCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();

        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        String join = StringUtils.flipText(Discord.replaceAllMention(ctx.getMessage()).replaceFirst(Config.get("prefix") + getName(), ""));

        channel.sendMessage(join).queue();
    }

    @Override
    public String getName() {
        return "flip";
    }

    @Override
    public String getHelp(String prefix) {
        return "Flips the provided text upside down\n" +
                "Usage: " + prefix + " flip <text>\n" +
                "Note: Only flips some characters";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return null;
    }

    @Override
    public int getCoolDown() {
        return 10;
    }
}
