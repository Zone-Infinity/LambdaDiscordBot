package bot.java.lambda;

import bot.java.lambda.apis.Api;
import bot.java.lambda.config.Config;
import bot.java.lambda.config.Profanity;
import bot.java.lambda.events.Listener;
import bot.java.lambda.events.MusicEventListener;
import bot.java.lambda.events.audits.JDAEventListener;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.GatewayEncoding;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Bot {
    final EventWaiter waiter = new EventWaiter();
    public static ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(3);

    private Bot(String token) throws LoginException, InterruptedException {
        WebUtils.setUserAgent("Mozilla/5.0 (compatible; Lambda/1.1; https://github.com/Zone-Infinity/LambdaDiscordBot");

        Object[] listeners = {
                waiter,
                new Listener(waiter),
                new JDAEventListener(),
                new MusicEventListener()
        };

        JDABuilder jdaBuilder = JDABuilder.createDefault(
                token,
                // GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                // GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_MESSAGE_REACTIONS
        )
                .setMemberCachePolicy(MemberCachePolicy.DEFAULT.or((member -> member.getGuild().getId().equals("755433534495391805"))))
                // .setChunkingFilter(ChunkingFilter.ALL)
                .enableCache(EnumSet.of(
                        // CacheFlag.CLIENT_STATUS,
                        // CacheFlag.ACTIVITY,
                        CacheFlag.VOICE_STATE,
                        CacheFlag.EMOTE
                ))
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS)
                .addEventListeners(listeners)
                .setBulkDeleteSplittingEnabled(false)
                .setMemberCachePolicy(MemberCachePolicy.DEFAULT)
                .setGatewayEncoding(GatewayEncoding.ETF);
        final JDA jda = jdaBuilder.build();

        Profanity.loadProfanityList();
        jda.awaitReady();

        if (!token.equals(Config.get("beta_token"))) Api.startPostingServerCount(jda, 60);
    }

    public static void main(String[] args) throws LoginException, InterruptedException {
        new Bot(Config.get("token"));
    }
}
