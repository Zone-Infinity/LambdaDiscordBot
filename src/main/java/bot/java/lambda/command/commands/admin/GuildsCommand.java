package bot.java.lambda.command.commands.admin;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.ICommand;
import bot.java.lambda.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GuildsCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getAuthor().getIdLong() == Long.parseLong(Config.get("owner_id"))) {
            List<Guild> guilds = ctx.getJDA().getGuilds()
                .stream()
                .sorted(Comparator.comparingInt(Guild::getMemberCount).reversed())
                .collect(Collectors.toList());
            final List<List<Guild>> guildsList = IntStream.range(0, guilds.size())
                .boxed()
                .collect(Collectors.groupingBy(i -> i / 4))
                .values()
                .stream()
                .map(indices -> indices.stream().map(guilds::get).collect(Collectors.toList()))
                .collect(Collectors.toList());
            try {
                final List<String> args = ctx.getArgs();
                final int page = args.isEmpty() ? 1 : Integer.parseInt(args.get(0));
                ctx.getChannel().sendMessage(new EmbedBuilder()
                        .setDescription("```" + getGuildTable(guildsList.get(page - 1)) + "```")
                        .build()).queue();
            } catch (NumberFormatException e) {
                ctx.getChannel().sendMessage("Provide a number").queue();
            } catch (IndexOutOfBoundsException e) {
                ctx.getChannel().sendMessage("No guilds on the page").queue();
            }
        }
    }

    private String getGuildTable(List<Guild> guildList) {
        StringBuilder table = new StringBuilder();
        final int guildSize = guildList.stream()
            .mapToInt(it -> Math.min(it.getName().length(), 22))
            .max()
            .orElse(0);
        final int memberSize = guildList.stream()
            .mapToInt(it -> String.valueOf(it.getMemberCount()).length())
            .max()
            .orElse(0);

        String rowFormat = "║%-" +
            (Math.max(5, String.valueOf(guildList.size()).length()) + 1) +
            "s║%-" +
            (Math.max(guildSize, 5) + 1) +
            "s║%-" +
            (Math.max(memberSize, 7) + 1) +
            "s║%-18s║%n";
        final String divider = String.format(rowFormat, "", "", "", "").replaceAll(" ", "═");

        table.append(String.format(rowFormat, "", "", "", "").replaceFirst("║", "╔").replaceFirst("║", "╦").replaceFirst("║", "╦").replaceFirst("║", "╦").replaceFirst("║", "╗").replaceAll(" ", "═"));
        table.append(String.format(rowFormat, "Rank ", "Name", "Members", "ID"));
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
