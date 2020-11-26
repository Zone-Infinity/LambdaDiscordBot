package bot.java.lambda.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public class GuildUtils {
    public static List<TextChannel> getTextChannel(Guild guild, String[] contains) {
        List<TextChannel> results = new ArrayList<>();
        final List<TextChannel> textChannels = guild.getTextChannels();
        for (TextChannel channel : textChannels) {
            boolean b = false;
            final String name = channel.getName();
            for (String c : contains) {
                if (name.contains(c)) b = true;
                else {
                    b = false;
                    break;
                }
            }
            if (b) results.add(channel);
        }
        return results;
    }
}
