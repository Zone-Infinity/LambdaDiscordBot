package bot.java.lambda.events;

import bot.java.lambda.command.CommandManager;
import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import bot.java.lambda.config.Config;
import bot.java.lambda.utils.DatabaseUtils;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
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
            event.getJDA().getPresence().setActivity(Activity.watching(event.getJDA().getUsers().size() + " users | Contact Zone Infinityλ7763 for help"));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            event.getJDA().getPresence().setActivity(Activity.watching(event.getJDA().getGuilds().size() + " guilds | Contact Zone Infinityλ7763 for help"));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            event.getJDA().getPresence().setActivity(Activity.watching(">help | Contact Zone Infinityλ7763 for help"));
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

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        final String prefix = getPrefix(eventGuild.getId());
        final TextChannel channel = event.getChannel();
        final Message message = event.getMessage();
        String raw = message.getContentRaw();

        final String path = Listener.class.getResource("/files/profanity.txt").getPath();

        List<String> profanityWords = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNextLine()) {
                profanityWords.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Did not found profanity.txt");
        }

        if (raw.equalsIgnoreCase("hello") || raw.equalsIgnoreCase("hi") || raw.equalsIgnoreCase("hey") || raw.equalsIgnoreCase("helo")) {

            if (!eventGuild.getId().equals("755433534495391805"))
                return;

            if (saidHello.contains(user))
                return;

            saidHello.add(user);

            channel.sendMessage("Hello. What is your name?").queue();

            waiter.waitForEvent(MessageReceivedEvent.class,
                    e -> e.getAuthor().equals(user)
                            && e.getChannel().equals(channel)
                            && !e.getMessage().equals(message),
                    e -> {
                        final String userName = e.getMessage().getContentRaw();
                        String name = e.getGuild().getSelfMember().getNickname() == null ? e.getJDA().getSelfUser().getName() : e.getGuild().getSelfMember().getNickname();
                        if (userName.contains(name) || userName.contains("Lambda")) {
                            e.getChannel().sendMessage("<:Wot:755715077029625916> Eh , it's my name. Bruh!!").queue();
                            return;
                        }
                        channel.sendMessage("Hello, `" + userName + "`! I'm `" + name + "`!").queue();
                    },
                    1, TimeUnit.MINUTES, () -> channel.sendMessage("Sorry, you took too long.").queue());
        }

        if (raw.startsWith(prefix)) {
            for (String s : profanityWords) {
                if (raw.contains(s)) {
                    channel.sendMessage("I don't reply to profanity").queue();
                    return;
                }
            }

            manager.handle(event, prefix);
        }
    }

    public static String getPrefix(String guildID) {
        final String prefixes = DatabaseUtils.PrefixDotJson;

        try (FileReader reader = new FileReader(prefixes)) {
            JSONParser jsonParser = new JSONParser();
            Object jsonFile = jsonParser.parse(reader);
            JSONObject prefixList = (JSONObject) jsonFile;

            final String prefix = (String) prefixList.get(guildID);
            if (prefix != null)
                return prefix;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return Config.get("prefix");
    }
}
