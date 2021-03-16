package bot.java.lambda.utils;

import bot.java.lambda.command.CommandContext;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static final List<String> profanityWords = new ArrayList<>();
    private static final Map<String, String> emojis = new HashMap<>();

    public static String getEmojiFor(String character) {
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
        if (emojis.containsKey(character.toLowerCase())) {
            return emojis.get(character.toLowerCase());
        }
        return ".";
    }

    public static String getAuthorRequested(GuildMessageReceivedEvent event) {
        final String asTag = event.getAuthor().getAsTag();
        return "Requested by " + asTag.substring(0, asTag.length() - 5) + "λ" + asTag.substring(asTag.length() - 4);
    }

    public static Emote searchEmote(CommandContext ctx, String name) {
        List<Guild> guilds = new ArrayList<>();
        for (Guild guild : ctx.getJDA().getGuilds()) {
            if (guild.getEmotes().size() > 15) {
                guilds.add(guild);
            }
        }

        for (Guild guild : guilds) {
            final List<Emote> emotes = guild.getEmotes();
            for (Emote emote : emotes) {
                if (emote.getName().equalsIgnoreCase(name.replaceAll("\\W", ""))) {
                    return emote;
                }
            }
        }
        return null;
    }

    public static String getUptime() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long uptime = runtimeMXBean.getUptime();
        /*
        long uptimeInSeconds = uptime / 1000;
        long numberOfDays = uptimeInSeconds / (60 * 60 * 24);
        long numberOfHours = (uptimeInSeconds / (60 * 60)) - (numberOfDays * 24);
        long numberOfMinutes = (uptimeInSeconds / 60) - (numberOfDays * 24 * 60);
        long numberOfSeconds = uptimeInSeconds % 60;

        return String.format("%s days , %s hours, %s minutes, %s seconds",
                numberOfDays, numberOfHours, numberOfMinutes, numberOfSeconds);
        */
        final String timestamp = getTimestamp(uptime);
        final String[] split = timestamp.split(":");
        final int length = split.length;

        return switch (length) {
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
        String online = "<a:Online:772748895700647946>",
                offline = "<a:Offline:772748768307183617>",
                idle = "<a:Idle:772748809377415189>",
                dnd = "<a:Dnd:772748860057583626>",
                streaming = "<a:Streaming:778822668035948585>";
        return switch (status.toLowerCase()) {
            case "s" -> streaming;
            case "on" -> online;
            case "idle" -> idle;
            case "dnd" -> dnd;
            default -> offline;
        };
    }

    public static boolean hasProfanity(String text) {
        for (String w : Utils.profanityWords) {
            if (text.contains(w)) {
                return true;
            }
        }
        return false;
    }

    public static String replaceAllMention(Message message) {
        final String contentRaw = message.getContentRaw();
        String replacedContent = contentRaw.replaceAll("@everyone", "<:LambdaPing:780988909433389066>everyone")
                .replaceAll("@here", "<:LambdaPing:780988909433389066>here")
                .replaceAll("<@&[0-9]{18}>", "<:LambdaPing:780988909433389066>Role");
        final List<Role> mentionedRoles = message.getMentionedRoles();
        Pattern pattern = Pattern.compile("<:LambdaPing:780988909433389066>Role");
        Matcher matcher = pattern.matcher(replacedContent);
        int count = 0;

        if (mentionedRoles.isEmpty()) {
            return replacedContent;
        }

        String earlierContent = replacedContent;

        while (matcher.find()) {
            try {
                earlierContent = replacedContent;
                replacedContent = replacedContent.replaceFirst(replacedContent.substring(matcher.start(), matcher.end()), "<:LambdaPing:780988909433389066>" + mentionedRoles.get(count).getName());
                count++;
            } catch (IndexOutOfBoundsException ignored) {
                replacedContent = earlierContent;
            }
        }

        return replacedContent;
    }

    public static User getZoneInfinity(JDA jda) {
        return jda.getUserById("722854351600615465");
    }

    public static String getZoneInfinityAsTag(JDA jda) {
        final User zoneInfinity = getZoneInfinity(jda);
        return zoneInfinity.getName() + "λ" + zoneInfinity.getDiscriminator();
    }

    public static String replaceAllEmojiString(String message, CommandContext ctx) {
        List<String> nameOfEmotes = new ArrayList<>();
        for (String m : message.split(" ")) {
            if (m.startsWith(":") && m.endsWith(":") && m.length() > 2)
                nameOfEmotes.add(m.substring(1, m.length() - 1));
        }

        String replacedString = message.replaceAll("<(a?):(\\w+):(\\d+)>", "{{$1;$2;$3}}")
                .replaceAll(":\\w+:", "<∭Emote>:::::")
                .replaceAll("\\{\\{(a?);(\\w+);(\\d+)}}", "<$1:$2:$3>");
        StringBuilder result = new StringBuilder();
        int count = 0;
        for (String s : replacedString.split(" ")) {
            if (s.equals("<∭Emote>:::::")) {
                final Emote emote = searchEmote(ctx, nameOfEmotes.get(count));
                if (emote == null) {
                    result.append(":").append(nameOfEmotes.get(count)).append(":").append(" ");
                    continue;
                }
                result.append(emote.getAsMention()).append(" ");
                count++;
                continue;
            }
            result.append(s).append(" ");
        }

        return result.toString();
    }

}
