package bot.java.lambda.events;

import bot.java.lambda.Bot;
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
        // jda.getPresence().setActivity(Activity.streaming("Vote Me Pls !!!", "https://top.gg/bot/752052866809593906/vote"));

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        int sleepTime = 2500;
        Runnable status = () -> {
            // Loading... x% , Error!, Restarting
            for (int i = 0; i <= 99; i += Bot.random.nextInt(9) + 1) {
                jda.getPresence().setActivity(Activity.playing("Loading... " + i + "%"));
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            jda.getPresence().setActivity(Activity.playing("Error!"));
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 1; i <= 3; i++) {
                jda.getPresence().setActivity(Activity.playing("Restarting" + ".".repeat(i)));
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
        // if(!event.getGuild().getId().equals(AuditUtils.lambdaGuildId)) return;

        User user = event.getAuthor();
        final Guild eventGuild = event.getGuild();

        if (user.isBot() || event.isWebhookMessage())
            return;

        final String prefix = getPrefix(eventGuild.getId());
        final Message message = event.getMessage();
        String raw = message.getContentRaw();

        if (raw.equals(eventGuild.getSelfMember().getAsMention())) {
            event.getChannel().sendMessage("My Prefix is " + prefix + ".\n" +
                    "To get started send " + prefix + "help.").queue();
        }

        if (raw.startsWith(prefix) || raw.startsWith("<@!752052866809593906>") || raw.startsWith("<@752052866809593906>")) {
            manager.handle(event, prefix);
        }
    }

    @SuppressWarnings("unused")
    public static String getPrefix(String guildID) {
        return Config.get("prefix");
    }
}
