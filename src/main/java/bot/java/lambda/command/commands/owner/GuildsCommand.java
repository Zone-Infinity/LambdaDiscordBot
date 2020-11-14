package bot.java.lambda.command.commands.owner;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.config.Config;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

public class GuildsCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getAuthor().getIdLong() == Long.parseLong(Config.get("owner_id"))) {
            final List<Guild> guilds = ctx.getJDA().getGuilds();
            StringBuilder guildList = new StringBuilder();
            guildList.append("```");
            for (Guild guild : guilds)
                guildList.append("-")
                        .append(guild.getName())
                        .append(" : ")
                        .append(guild.getMemberCount())
                        .append(" : ")
                        .append(guild.getId())
                        .append("\n");
            guildList.append("```");
            ctx.getChannel().sendMessage(guildList).queue();
        }
    }

    @Override
    public String getName() {
        return "guilds";
    }

    @Override
    public String getHelp(String prefix) {
        return "Send the guild it is in";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.OWNER;
    }
}
