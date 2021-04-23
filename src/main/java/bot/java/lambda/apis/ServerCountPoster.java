package bot.java.lambda.apis;

import bot.java.lambda.Bot;
import net.dv8tion.jda.api.JDA;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ServerCountPoster {
    private final JDA jda;

    public ServerCountPoster(JDA jda) {
        this.jda = jda;
    }

    public void startPostingServerCount(Set<ServerCountSite> sites, int delay) {
        Runnable postServerCount = () -> {
            final long guildCount = jda.getGuildCache().size();

            for (ServerCountSite site : sites) {
                site.postServerCount(guildCount);
            }
        };

        Bot.executor.scheduleWithFixedDelay(postServerCount, 1, delay, TimeUnit.MINUTES);
    }
}
