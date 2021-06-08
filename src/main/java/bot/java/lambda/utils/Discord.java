package bot.java.lambda.utils;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.config.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Discord {
    private static final List<String> CONTRIBUTOR_IDS = List.of("722854351600615465", "616969228972458008", "757050742379905056");

    private static final Pattern EMOTE_NAME_PATTERN = Pattern.compile(":(\\S+):");

    public static List<TextChannel> getTextChannel(Guild guild, String[] filters) {
        return guild.getTextChannels()
                .stream()
                .filter(channel -> Arrays.stream(filters).allMatch(filter -> channel.getName().contains(filter)))
                .collect(Collectors.toList());
    }

    public static Emote searchEmote(CommandContext ctx, String name) {
        return ctx.getJDA()
                .getGuilds()
                .stream()
                .flatMap(guild -> guild.getEmotes().stream())
                .filter(emote -> emote.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static String getAuthorRequested(GuildMessageReceivedEvent event) {
        return "Requested by " + event.getAuthor().getName() + "λ" + event.getAuthor().getDiscriminator();
    }

    public static String replaceAllMention(Message message) {
        String content = message.getContentDisplay()
                .replace("@everyone", "<:LambdaPing:780988909433389066>everyone")
                .replace("@here", "<:LambdaPing:780988909433389066>");
        for (Role role : message.getMentionedRoles()) {
            content = content.replace("@" + role.getName(), "<:LambdaPing:780988909433389066>" + role.getName());
        }
        return content;
    }

    public static User getZoneInfinity(JDA jda) {
        return jda.retrieveUserById(Config.get("owner_id")).complete();
    }

    public static String getZoneInfinityAsTag(JDA jda) {
        final User zoneInfinity = getZoneInfinity(jda);
        return zoneInfinity.getName() + "λ" + zoneInfinity.getDiscriminator();
    }

    public static String getContributorsAsTag(JDA jda) {
        return CONTRIBUTOR_IDS.stream().map(id -> {
            final User user = jda.retrieveUserById(id).complete();
            return user.getName() + "λ" + user.getDiscriminator();
        }).collect(Collectors.joining(" , ")).replaceFirst(",(?!.*,)", "and");
    }

    public static String replaceAllEmojiString(String message, CommandContext ctx) {
        message = message.replaceAll("<(a?):(\\w+):(\\d+)>", "{{$1;$2;$3}}");
        Matcher matcher = EMOTE_NAME_PATTERN.matcher(message);
        StringBuilder result = new StringBuilder();
        if (matcher.find()) {
            do {
                matcher.appendReplacement(result, Discord.searchEmote(ctx, matcher.group(1)).getAsMention());
            } while (matcher.find());
        } else {
            result.append(message);
        }
        return result.toString().replaceAll("\\{\\{(a?);(\\w+);(\\d+)}}", "<$1:$2:$3>");
    }

    public static List<List<Guild>> getGuildsList(JDA jda) {
        List<Guild> guilds = jda.getGuilds()
                .stream()
                .sorted(Comparator.comparingInt(Guild::getMemberCount).reversed())
                .collect(Collectors.toList());

        return IntStream.range(0, guilds.size())
                .boxed()
                .collect(Collectors.groupingBy(i -> i / 10))
                .values()
                .stream()
                .map(indices -> indices.stream().map(guilds::get).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
