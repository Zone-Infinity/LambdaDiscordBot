package bot.java.lambda;

import bot.java.lambda.config.Config;
import bot.java.lambda.config.Profanity;
import bot.java.lambda.events.Listener;
import bot.java.lambda.events.MusicEventListener;
import bot.java.lambda.events.audits.JDAEventListener;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.text.TextColor;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.GatewayEncoding;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public class Bot {
    final EventWaiter waiter = new EventWaiter();

    private Bot() throws LoginException, InterruptedException {
        WebUtils.setUserAgent("Zone-Infinity#7763");

        Object[] listeners = {
                waiter,
                new Listener(waiter),
                new JDAEventListener(),
                new MusicEventListener()
        };

        JDABuilder jdaBuilder = JDABuilder.createDefault(
                Config.get("token"),
                // GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                // GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_MESSAGE_REACTIONS
        )
                .setMemberCachePolicy(MemberCachePolicy.DEFAULT)
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

        //TopGG.startPostingServerCount(jda, 30);
    }

    public static void main(String[] args) throws LoginException, InterruptedException {
        new Bot();
    }
}
