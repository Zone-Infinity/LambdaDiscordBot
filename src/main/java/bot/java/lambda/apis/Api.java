package bot.java.lambda.apis;

import bot.java.lambda.Bot;
import net.dv8tion.jda.api.JDA;

import java.util.concurrent.TimeUnit;

public class Api {
    public static void startPostingServerCount(JDA jda, int delay) {
        Runnable postServerCount = () -> {
            final long guildCount = jda.getGuildCache().size();

            TopGG.postServerCount(guildCount);
            InfinityBots.postServerCount(guildCount);
        };

        Bot.executor.scheduleWithFixedDelay(postServerCount, 1, delay, TimeUnit.MINUTES);
    }
}
