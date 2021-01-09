package bot.java.lambda.events;

import bot.java.lambda.command.CommandManager;
import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import bot.java.lambda.config.Config;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private TextChannel globalAuditsChannel;
    private final CommandManager manager;
    private final EventWaiter waiter;

    public Listener(EventWaiter waiter) {
        this.waiter = waiter;
        manager = new CommandManager(waiter);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        final JDA jda = event.getJDA();
        LOGGER.info("{} is ready", jda.getSelfUser().getAsTag());
        jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        final String asTag = Objects.requireNonNull(jda.getUserById(Config.get("owner_id"))).getAsTag();

        Runnable status = () -> {
            jda.getPresence().setActivity(Activity.watching(jda.getUsers().size() + " users | Contact " + asTag.replace("#", "λ") + " for help"));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jda.getPresence().setActivity(Activity.watching(jda.getGuilds().size() + " guilds | Contact " + asTag.replace("#", "λ") + " for help"));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jda.getPresence().setActivity(Activity.watching(">help | Contact " + asTag.replace("#", "λ") + " for help"));
        };

        Runnable checkWhetherInactive = () -> jda.getGuilds().forEach(guild -> {
            AudioManager audioManager = guild.getAudioManager();
            final PlayerManager playerManager = PlayerManager.getInstance();
            final GuildMusicManager musicManager = playerManager.getMusicManager(guild);
            AudioPlayer player = musicManager.audioPlayer;
            if (audioManager.getConnectedChannel() != null) {
                if (audioManager.getConnectedChannel().getMembers().size() == 1 || player.getPlayingTrack() == null) {
                    waiter.waitForEvent(
                            GuildVoiceJoinEvent.class,
                            e -> e.getChannelJoined() == audioManager.getConnectedChannel(),
                            e -> {
                            },
                            60, TimeUnit.SECONDS, () -> {
                                if (!(audioManager.getConnectedChannel().getMembers().size() > 1) || player.getPlayingTrack() == null)
                                    audioManager.closeAudioConnection();
                            }
                    );
                }
            }
        });

        executor.scheduleWithFixedDelay(checkWhetherInactive, 0, 60, TimeUnit.SECONDS);
        executor.scheduleWithFixedDelay(status, 0, 5, TimeUnit.SECONDS);

        globalAuditsChannel = jda.getTextChannelById(758724135790051368L);

    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        globalAuditsChannel.sendMessage("```Added to " + event.getGuild() + "```").queue();
        final TextChannel systemChannel = event.getGuild().getSystemChannel();
        if (systemChannel == null) {
            return;
        }
        systemChannel.sendMessage("Thank you for adding me \uD83D\uDE03").queue();
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        globalAuditsChannel.sendMessage("```Removed from " + event.getGuild() + "```").queue();
    }

    @Override
    public void onGuildBan(@NotNull GuildBanEvent event) {
        globalAuditsChannel.sendMessage("```Banned from " + event.getGuild() + "```").queue();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        final Guild eventGuild = event.getGuild();

        if (user.isBot() || event.isWebhookMessage())
            return;


        final String prefix = getPrefix(eventGuild.getId());
        final Message message = event.getMessage();
        String raw = message.getContentRaw();

        if (raw.startsWith(prefix)) {
            manager.handle(event, prefix);
        }
    }

    @SuppressWarnings("unused")
    public static String getPrefix(String guildID) {
        return Config.get("prefix");
    }
}
