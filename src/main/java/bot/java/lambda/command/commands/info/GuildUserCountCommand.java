package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class GuildUserCountCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        if (!ctx.getArgs().isEmpty() && ctx.getArgs().get(0).equalsIgnoreCase("total")) {
            int total = 0;
            for (Guild guild : ctx.getJDA().getGuilds()) {
                total += guild.getMemberCount();
            }
            channel.sendMessage("Total Members I can see : " + total).queue();
            return;
        }
        channel.sendMessage("Members in this server : " + ctx.getGuild().getMemberCount()).queue();
    }

    @Override
    public String getName() {
        return "guildusercount";
    }

    @Override
    public String getHelp() {
        return "Sends the number of users in the server\n" +
                "Usage : >guildusercount or >guc\n" +
                "For Total Member Bot can Watch : >guc total\n" +
                "Aliases : {gucount, guc, gusercount}";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }

    @Override
    public List<String> getAliases() {
        return List.of("gucount", "guc", "gusercount");
    }
}
