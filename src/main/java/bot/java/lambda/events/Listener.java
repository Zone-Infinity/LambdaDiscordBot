package bot.java.lambda.events;

import bot.java.lambda.command.CommandManager;
import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import bot.java.lambda.config.Config;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private TextChannel globalAuditsChannel;
    private final CommandManager manager;
    private final EventWaiter waiter;
    private final List<User> saidHello = new ArrayList<>();

    public Listener(EventWaiter waiter) {
        this.waiter = waiter;
        manager = new CommandManager(waiter);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
        event.getJDA().getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        Runnable status = () -> {
            event.getJDA().getPresence().setActivity(Activity.watching(event.getJDA().getGuilds().size() + " guilds | Contact Zone_Infinityλ7763 for help"));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            event.getJDA().getPresence().setActivity(Activity.watching(">help | Contact Zone_Infinityλ7763 for help"));
        };
        Runnable checkWhetherInactive = () -> event.getJDA().getGuilds().forEach(guild -> {
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
                            30, TimeUnit.SECONDS, () -> {
                                if (!(audioManager.getConnectedChannel().getMembers().size() > 1) || player.getPlayingTrack() == null)
                                    audioManager.closeAudioConnection();
                            }
                    );
                }
            }
        });

        executor.scheduleWithFixedDelay(checkWhetherInactive, 0, 10, TimeUnit.SECONDS);
        executor.scheduleWithFixedDelay(status, 0, 5, TimeUnit.SECONDS);

        final Guild lambdaGuild = event.getJDA().getGuildById(755433534495391805L);
        if (!(lambdaGuild == null)) {
            final VoiceChannel create_vc = lambdaGuild.getVoiceChannelsByName("Create VC", true).get(0);
            create_vc.getManager().putPermissionOverride(Objects.requireNonNull(lambdaGuild.getRolesByName("@everyone", true).get(0)), Collections.singletonList(Permission.VOICE_CONNECT), Collections.emptyList()).queue();
        }

        globalAuditsChannel = event.getJDA().getTextChannelById(758724135790051368L);

    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        if (event.getGuild().getId().equals("755433534495391805")) {
            if (event.getChannelJoined().getName().equals("Create VC")) {
                String s = "Lobby #" + (Objects.requireNonNull(event.getGuild().getCategoryById("758701038399389727")).getVoiceChannels().size() - 2);

                event.getGuild().createVoiceChannel(s, event.getChannelJoined().getParent()).queue(
                        channel -> {
                            event.getGuild().moveVoiceMember(event.getMember(), channel).queue();
                            channel.getManager().setUserLimit(10).queue();
                            waitForVoiceLeave(channel);
                        }
                );
            }
        }
    }

    public void waitForVoiceLeave(VoiceChannel channel) {
        waiter.waitForEvent(
                GuildVoiceLeaveEvent.class,
                e -> e.getChannelLeft().getMembers().isEmpty() &&
                        e.getChannelLeft().equals(channel),
                e -> e.getChannelLeft().delete().queue()
        );
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        globalAuditsChannel.sendMessage("```Added to " + event.getGuild() + "```").queue();
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

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        String prefix = Config.get("prefix");
        String raw = event.getMessage().getContentRaw();

        if (raw.equalsIgnoreCase(prefix + "guilds")) {
            if (event.getAuthor().getIdLong() == Long.parseLong(Config.get("owner_id"))) {
                final List<Guild> guilds = event.getJDA().getGuilds();
                StringBuilder guildList = new StringBuilder();
                guildList.append("```");
                for (Guild guild : guilds)
                    guildList.append("-")
                            .append(guild.getName())
                            .append(" : ")
                            .append(guild.getMemberCount())
                            .append(" : ")
                            .append(guild.getId())
                            .append("\n");
                guildList.append("```");
                event.getChannel().sendMessage(guildList).queue();
            }
        }

        if (raw.equalsIgnoreCase("hello") || raw.equalsIgnoreCase("hi") || raw.equalsIgnoreCase("hey") || raw.equalsIgnoreCase("helo")) {

            if (!eventGuild.getId().equals("755433534495391805"))
                return;

            if (saidHello.contains(user))
                return;

            saidHello.add(user);

            event.getChannel().sendMessage("Hello. What is your name?").queue();

            waiter.waitForEvent(MessageReceivedEvent.class,
                    e -> e.getAuthor().equals(user)
                            && e.getChannel().equals(event.getChannel())
                            && !e.getMessage().equals(event.getMessage()),
                    e -> {
                        final String message = e.getMessage().getContentRaw();
                        String name = e.getGuild().getSelfMember().getNickname() == null ? e.getJDA().getSelfUser().getName() : e.getGuild().getSelfMember().getNickname();
                        if (message.contains(name) || message.contains("Lambda")) {
                            e.getChannel().sendMessage("<:Wot:755715077029625916> Eh , it's my name. Bruh!!").queue();
                            return;
                        }
                        event.getChannel().sendMessage("Hello, `" + message + "`! I'm `" + name + "`!").queue();
                    },
                    1, TimeUnit.MINUTES, () -> event.getChannel().sendMessage("Sorry, you took too long.").queue());
        }

        if (raw.equals("<@!752052866809593906>")) {
            event.getChannel().sendMessageFormat("Hi %s , my prefix is %s", user.getAsMention(), Config.get("prefix")).queue();
        }

        if (raw.equalsIgnoreCase(prefix + "close")) {
            if (event.getAuthor().getId().equals(Config.get("owner_id"))) {
                event.getChannel().sendMessage("Shutting Down").queue();
                event.getMessage().addReaction("✅").queue();
                LOGGER.info("Shutting Down");

                if (eventGuild.getId().equals("755433534495391805")) {
                    final VoiceChannel create_vc = eventGuild.getVoiceChannelsByName("Create VC", true).get(0);
                    create_vc.getManager().putPermissionOverride(Objects.requireNonNull(eventGuild.getRoleById(755433534495391805L)), Collections.emptyList(), Collections.singletonList(Permission.VOICE_CONNECT)).queue();
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.fillInStackTrace();
                }

                event.getJDA().shutdown();
                BotCommons.shutdown(event.getJDA());
                System.exit(0);
            }
        }

        if (raw.startsWith(prefix)) {
            manager.handle(event, prefix);
        }
    }
}

