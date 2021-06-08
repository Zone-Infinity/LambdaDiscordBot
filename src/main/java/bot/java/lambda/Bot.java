package bot.java.lambda;

import bot.java.lambda.apis.Boats;
import bot.java.lambda.apis.InfinityBots;
import bot.java.lambda.apis.ServerCountPoster;
import bot.java.lambda.apis.TopGG;
import bot.java.lambda.config.Config;
import bot.java.lambda.events.*;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.web.WebUtils;
import me.infinity.ibl.IBL;
import net.dv8tion.jda.api.GatewayEncoding;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Bot {
    private final ScheduledExecutorService executor;

    private Bot(String token) throws LoginException, InterruptedException {
        WebUtils.setUserAgent("Mozilla/5.0 (compatible; Lambda/1.1; https://github.com/Zone-Infinity/LambdaDiscordBot");

        EventWaiter waiter = new EventWaiter();
        executor = new ScheduledThreadPoolExecutor(10);
        Object[] listeners = {
                waiter,
                new Listener(this, waiter),
                new JDAEventListener(),
                new MusicEventListener(),
                new MemberEventListener(),
                new GuildsCommandButtonListener()
        };

        JDABuilder jdaBuilder = JDABuilder.createDefault(
                token,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_MESSAGE_REACTIONS
        )
                .setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.DEFAULT.or((member -> member.getGuild().getId().equals("755433534495391805")))))
                .enableCache(EnumSet.of(
                        CacheFlag.VOICE_STATE,
                        CacheFlag.EMOTE
                ))
                .addEventListeners(listeners)
                .setBulkDeleteSplittingEnabled(false)
                .setGatewayEncoding(GatewayEncoding.ETF);
        final JDA jda = jdaBuilder.build();
        jda.awaitReady();

        if (token.equals(Config.get("token"))) {
            Logger LOGGER = LoggerFactory.getLogger(Bot.class);
            IBL ibl = new IBL.Builder(jda.getSelfUser().getId(), Config.get("InfinityBotList_Token"));

            LOGGER.info("Starting to Post counts on Bot Lists");
            ServerCountPoster poster = new ServerCountPoster(jda);
            poster.startPostingServerCount(Set.of(
                    new TopGG(),
                    new InfinityBots(ibl),
                    new Boats()
            ), 60, this);
        }
    }

    public ScheduledExecutorService getExecutor() {
        return executor;
    }

    public static void main(String[] args) throws LoginException, InterruptedException {
        if (args.length == 1)
            if (args[0].equalsIgnoreCase("true")) {
                new Bot(Config.get("beta_token"));
                return;
            }

        new Bot(Config.get("token"));
    }
}
