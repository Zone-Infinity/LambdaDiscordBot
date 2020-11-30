package bot.java.lambda.command.commands.admin;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GuildsCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getAuthor().getIdLong() == Long.parseLong(Config.get("owner_id"))) {
            final TextChannel channel = ctx.getChannel();
            List<Guild> guilds = new ArrayList<>(ctx.getJDA().getGuilds());
            final List<List<Guild>> guildsList = new ArrayList<>();
            try {
                final int page = Integer.parseInt(ctx.getArgs().get(0));

                guilds.sort(Comparator.comparingInt(Guild::getMemberCount));
                Collections.reverse(guilds);

                final int size = guilds.size();
                final int guildsLists = size / 10;

                for (int i = 1; i <= guildsLists; i++) {
                    int j = i * 10;
                    guildsList.add(guilds.subList(j - 10, j - 1));
                }

                final int remainingGuilds = size % 10;
                final int guildsInTen = guildsLists * 10;
                guildsList.add(guilds.subList(guildsInTen, guildsInTen + remainingGuilds));

                channel.sendMessage(new EmbedBuilder()
                        .setDescription("```" + getGuildTable(guildsList.get(page - 1)) + "```")
                        .build()).queue();
            } catch (NumberFormatException e) {
                channel.sendMessage("Provide a number").queue();
            } catch (IndexOutOfBoundsException e) {
                channel.sendMessage("No guilds on the page").queue();
            }
        }
    }

    private String getGuildTable(List<Guild> guildList) {
        StringBuilder table = new StringBuilder();

        final int guildSize = guildList.stream().mapToInt(it -> Math.min(it.getName().length(), 22)).max().orElse(0);
        final int memberSize = guildList.stream().mapToInt(it -> String.valueOf(it.getMemberCount()).length()).max().orElse(0);

        String rowFormat = "║%-" + (Math.max(5, String.valueOf(guildList.size()).length()) + 1) + "s║%-" + (Math.max(guildSize, 5) + 1) + "s║%-" + (Math.max(memberSize, 7) + 1) + "s║%-20s║%n";
        final String divider = String.format(rowFormat, "", "", "", "").replaceAll(" ", "═");

        table.append(String.format(rowFormat, "", "", "", "").replaceFirst("║", "╔").replaceFirst("║", "╦").replaceFirst("║", "╦").replaceFirst("║", "╦").replaceFirst("║", "╗").replaceAll(" ", "═"));
        table.append(String.format(rowFormat, "Rank ", "Name", "Members ", "ID"));
        table.append(divider);

        for (int i = 0; i < guildList.size(); i++) {
            final Guild guild = guildList.get(i);
            final String name = guild.getName();
            table.append(String.format(rowFormat, (i + 1) + ".", name.substring(0, Math.min(22, name.length())), guild.getMemberCount(), guild.getId()));
        }

        table.append(String.format(rowFormat, "", "", "", "").replaceFirst("║", "╚").replaceFirst("║", "╩").replaceFirst("║", "╩").replaceFirst("║", "╩").replaceFirst("║", "╝").replaceAll(" ", "═"));

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
