package bot.java.lambda.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GuildUtils {
    public static List<TextChannel> getTextChannel(Guild guild, String[] filters) {
        return guild.getTextChannels()
            .stream()
            .filter(channel -> Arrays.stream(filters).allMatch(filter -> channel.getName().contains(filter)))
            .collect(Collectors.toList());
    }
}
