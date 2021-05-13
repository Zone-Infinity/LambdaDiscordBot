package bot.java.lambda.apis;

import bot.java.lambda.Bot;
import net.dv8tion.jda.api.JDA;

import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public record ServerCountPoster(JDA jda) {

    public void startPostingServerCount(Set<ServerCountSite> sites, int delay, Bot bot) {
        Runnable postServerCount = () -> {
            final long guildCount = jda.getGuildCache().size();

            for (ServerCountSite site : sites) {
                site.postServerCount(guildCount);
            }
        };

        bot.getExecutor().scheduleWithFixedDelay(postServerCount, 1, delay, TimeUnit.MINUTES);
    }
}
