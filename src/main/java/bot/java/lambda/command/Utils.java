package bot.java.lambda.command;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
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
        emojis.put("0", ":zero:");
        emojis.put("1", ":one:");
        emojis.put("2", ":two:");
        emojis.put("3", ":three:");
        emojis.put("4", ":four:");
        emojis.put("5", ":five:");
        emojis.put("6", ":six:");
        emojis.put("7", ":seven:");
        emojis.put("8", ":eight:");
        emojis.put("9", ":nine:");
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

    public static String getAuthorRequested(GuildMessageReceivedEvent event){
        final String asTag = event.getAuthor().getAsTag();
        return "Requested by "+asTag.substring(0,asTag.length()-5)+"λ"+ asTag.substring(asTag.length()-4);
    }

    public static Emote searchEmote(CommandContext ctx, String name){
        List<Guild> guilds = new ArrayList<>();
        for(Guild guild: ctx.getJDA().getGuilds()){
            if(guild.getEmotes().size()>15){
                guilds.add(guild);
            }
        }

        for(Guild guild : guilds) {
            final List<Emote> emotes = guild.getEmotes();
            for (Emote emote : emotes) {
                if (emote.getName().equals(name.replaceAll("\\W", ""))) {
                    return emote;
                }
            }
        }
        return null;
    }

    public static String getUptime(){
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long uptime = runtimeMXBean.getUptime();
        long uptimeInSeconds = uptime / 1000;
        long numberOfHours = uptimeInSeconds / (60 * 60);
        long numberOfMinutes = (uptimeInSeconds / 60) - (numberOfHours * 60);
        long numberOfSeconds = uptimeInSeconds % 60;

        return String.format("%s hours, %s minutes, %s seconds",
                numberOfHours, numberOfMinutes, numberOfSeconds);
    }

}
