package bot.java.lambda.command.commands.admin;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.config.Config;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Comparator;
import java.util.List;

public class GuildsCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getAuthor().getIdLong() == Long.parseLong(Config.get("owner_id"))) {
            final List<Guild> guilds = ctx.getJDA().getGuilds();
            guilds.sort(Comparator.comparingInt(Guild::getMemberCount));
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

    private String getGuildTable(List<Guild> guildList) {
        StringBuilder table = new StringBuilder();

        final int namesSize = guildList.stream().mapToInt(it -> it.getName().length()).max().orElse(0);
        final int pointSize = guildList.stream().mapToInt(it -> String.valueOf(it.getMemberCount()).length()).max().orElse(0);

        String rowFormat = "║%-" + (Math.max(5, String.valueOf(guildList.size()).length()) + 1) + "s║%-" + (Math.max(namesSize, 5) + 1) + "s║%" + (Math.max(pointSize, 7) + 1) + "s║%n";
        final String divider = String.format(rowFormat, "", "", "").replaceAll(" ", "═");



        return table.toString();

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
