package bot.java.lambda.events;

import bot.java.lambda.Bot;
import bot.java.lambda.command.CommandManager;
import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import bot.java.lambda.config.GuildSettings;
import bot.java.lambda.database.DatabaseManager;
import bot.java.lambda.utils.AuditUtils;
import bot.java.lambda.utils.Utils;
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

        Runnable status = () -> {
            String[] AllStatus = {
                    jda.getGuildCache().size() + " guilds",
                    "Loading... 0%",
                    "Loading... " + Utils.random(5, 9) + "%",
                    "Loading... " + Utils.random(26, 30) + "%",
                    "Loading... " + Utils.random(40, 44) + "%",
                    "Loading... " + Utils.random(72, 76) + "%",
                    "Loading... " + Utils.random(94, 98) + "%",
                    "Loading... 99%"
            };
            // Loading... x% , Error!, Restarting
            for (String Status : AllStatus) {
                jda.getPresence().setActivity(Activity.watching(Status));
                try {
                    Thread.sleep(3000);
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

        Bot.executor.scheduleWithFixedDelay(checkWhetherInactive, 0, 60, TimeUnit.SECONDS);
        Bot.executor.scheduleWithFixedDelay(status, 0, 30, TimeUnit.SECONDS);

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
        // if (!event.getGuild().getId().equals("740228383446925402")) return;

        User user = event.getAuthor();
        final Guild eventGuild = event.getGuild();

        if (user.isBot() || event.isWebhookMessage())
            return;

        final long guildId = eventGuild.getIdLong();
        final String prefix = GuildSettings.PREFIXES.computeIfAbsent(guildId, DatabaseManager.INSTANCE::getPrefix);
        final Message message = event.getMessage();
        String raw = message.getContentRaw();

        if (raw.equals(eventGuild.getSelfMember().getAsMention())) {
            event.getChannel().sendMessage("My Prefix is " + prefix + ".\n" +
                    "To get started send " + prefix + "help.").queue();
        }

        if (raw.startsWith(prefix) || raw.startsWith(event.getGuild().getSelfMember().getAsMention())) {
            manager.handle(event, prefix);
        }
    }
}
