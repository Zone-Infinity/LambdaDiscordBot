package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;

import java.util.List;

public class GuildCountCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final int guilds = ctx.getJDA().getGuilds().size();
        ctx.getChannel().sendMessage("Currently the bot is in : "+guilds+" guilds").queue();
    }

    @Override
    public String getName() {
        return "guildcount";
    }

    @Override
    public String getHelp() {
        return "Sends the number of guilds the bot is in";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }

    @Override
    public List<String> getAliases() {
        return List.of("gcount","gc");
    }
}
