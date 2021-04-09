package bot.java.lambda.utils;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.config.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final Random RNG = new Random();
    private static final long START_TIME = System.currentTimeMillis();

    private static final Pattern EMOTE_NAME_PATTERN = Pattern.compile(":(\\S+):");

    public static final List<String> profanityWords = new ArrayList<>();
    private static final Map<String, String> emojis = new HashMap<>();

    static {
        emojis.put("a", "\uD83C\uDDE6");
        emojis.put("b", "\uD83C\uDDE7");
        emojis.put("c", "\uD83C\uDDE8");
        emojis.put("d", "\uD83C\uDDE9");
        emojis.put("e", "\uD83C\uDDEA");
        emojis.put("f", "\uD83C\uDDEB");
        emojis.put("g", "\uD83C\uDDEC");
        emojis.put("h", "\uD83C\uDDED");
        emojis.put("i", "\uD83C\uDDEE");
        emojis.put("j", "\uD83C\uDDEF");
        emojis.put("k", "\uD83C\uDDF0");
        emojis.put("l", "\uD83C\uDDF1");
        emojis.put("m", "\uD83C\uDDF2");
        emojis.put("n", "\uD83C\uDDF3");
        emojis.put("o", "\uD83C\uDDF4");
        emojis.put("p", "\uD83C\uDDF5");
        emojis.put("q", "\uD83C\uDDF6");
        emojis.put("r", "\uD83C\uDDF7");
        emojis.put("s", "\uD83C\uDDF8");
        emojis.put("t", "\uD83C\uDDF9");
        emojis.put("u", "\uD83C\uDDFA");
        emojis.put("v", "\uD83C\uDDFB");
        emojis.put("w", "\uD83C\uDDFC");
        emojis.put("x", "\uD83C\uDDFD");
        emojis.put("y", "\uD83C\uDDFE");
        emojis.put("z", "\uD83C\uDDFF");
        emojis.put("0", "0️⃣");
        emojis.put("1", "1️⃣");
        emojis.put("2", "2️⃣");
        emojis.put("3", "3️⃣");
        emojis.put("4", "4️⃣");
        emojis.put("5", "5️⃣");
        emojis.put("6", "6️⃣");
        emojis.put("7", "7️⃣");
        emojis.put("8", "8️⃣");
        emojis.put("9", "9️⃣");
        emojis.put("?", "\u2754");
        emojis.put("!", "\u2755");
        emojis.put(" ", "\u25AB");
        emojis.put("up", "\u2B06");
        emojis.put("down", "\u2B07");
        emojis.put("left", "\u2B05");
        emojis.put("right", "\u27A1");
    }

    public static String getEmojiFor(String character) {
        return emojis.getOrDefault(character.toLowerCase(), ".");
    }

    public static String getAuthorRequested(GuildMessageReceivedEvent event) {
        return "Requested by " + event.getAuthor().getName() + "λ" + event.getAuthor().getDiscriminator();
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

    public static String getUptime() {
        final String timestamp = getTimestamp(System.currentTimeMillis() - START_TIME);
        final String[] split = timestamp.split(":");
        return switch (split.length) {
            case 2 -> String.format("%s minutes, %s seconds", split[0], split[1]);
            case 3 -> String.format("%s hours, %s minutes, %s seconds", split[0], split[1], split[2]);
            case 4 -> String.format("%s days, %s hours, %s minutes, %s seconds", split[0], split[1], split[2], split[3]);
            default -> timestamp;
        };
    }

    public static String getTimestamp(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        int days = (int) (milliseconds / (1000 * 60 * 60) / 24);
        if (days > 0)
            return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
        else if (hours > 0)
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        else
            return String.format("%02d:%02d", minutes, seconds);
    }

    public static boolean isNotUrl(String url) {
        try {
            new URL(url).toURI();
            return false;
        } catch (URISyntaxException | MalformedURLException e) {
            return true;
        }
    }

    public static String getStatusAsEmote(String status) {
        return switch (status.toLowerCase()) {
            case "s" -> "<a:Streaming:778822668035948585>";
            case "on" -> "<a:Online:772748895700647946>";
            case "idle" -> "<a:Idle:772748809377415189>";
            case "dnd" -> "<a:Dnd:772748860057583626>";
            default -> "<a:Offline:772748768307183617>";
        };
    }

    public static boolean hasProfanity(String text) {
        return profanityWords.stream().anyMatch(text::contains);
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

    public static String replaceAllEmojiString(String message, CommandContext ctx) {
        message = message.replaceAll("<(a?):(\\w+):(\\d+)>", "{{$1;$2;$3}}");
        Matcher matcher = EMOTE_NAME_PATTERN.matcher(message);
        StringBuilder result = new StringBuilder();
        if (matcher.find()) {
            do {
                matcher.appendReplacement(result, searchEmote(ctx, matcher.group(1)).getAsMention());
            } while (matcher.find());
        } else {
            result.append(message);
        }
        return result.toString().replaceAll("\\{\\{(a?);(\\w+);(\\d+)}}", "<$1:$2:$3>");
    }

    public static int random(int lowerbound, int upperbound) {
        return RNG.nextInt(upperbound - lowerbound) + lowerbound;
    }
}
