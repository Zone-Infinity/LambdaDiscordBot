package bot.java.lambda;

import bot.java.lambda.config.Config;
import bot.java.lambda.config.Profanity;
import bot.java.lambda.events.Listener;
import bot.java.lambda.events.audits.JDAEventListener;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public class Bot {
    final EventWaiter waiter = new EventWaiter();

    private void ready() throws LoginException, InterruptedException {
        WebUtils.setUserAgent("Zone-Infinity#7763");

        Object[] listeners = {
                waiter,
                new Listener(waiter),
                new JDAEventListener()
        };

        JDABuilder jdaBuilder = JDABuilder.createDefault(
                Config.get("token"),
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS
        )
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .enableCache(EnumSet.of(
                        CacheFlag.CLIENT_STATUS,
                        CacheFlag.EMOTE,
                        CacheFlag.ACTIVITY
                ))
                .addEventListeners(listeners);
        final JDA jda = jdaBuilder.build();

        Profanity.loadProfanityList();
        jda.awaitReady();

        //TopGG.startPostingServerCount(jda, 30);
    }

    public static void main(String[] args) throws LoginException, InterruptedException {
        new Bot().ready();
    }
}
