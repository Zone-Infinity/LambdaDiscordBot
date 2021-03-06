package bot.java.lambda.command.commands.music.lavaplayer;

import bot.java.lambda.utils.Utils;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel channel, String trackUrl, boolean playList) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        try {
            this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    musicManager.scheduler.queue(track);

                    channel.sendMessage("Adding to queue: `")
                            .append(track.getInfo().title)
                            .append("` by `")
                            .append(track.getInfo().author)
                            .append("` for `")
                            .append(Utils.getTimestamp(track.getDuration()))
                            .append("`")
                            .queue();
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    final List<AudioTrack> tracks = playlist.getTracks();

                    if (playList) {
                        channel.sendMessage("Adding to queue: `")
                                .append(String.valueOf(tracks.size()))
                                .append("` tracks from playlist `")
                                .append(playlist.getName())
                                .append('`')
                                .queue();

                        playlist.getTracks().forEach(musicManager.scheduler::queue);
                        return;
                    }
                    final AudioTrack track = tracks.get(0);

                    musicManager.scheduler.queue(track);

                    channel.sendMessage("Adding to queue: `")
                            .append(track.getInfo().title)
                            .append("` by `")
                            .append(track.getInfo().author)
                            .append("` for `")
                            .append(Utils.getTimestamp(track.getDuration()))
                            .append("`")
                            .queue();
                }

                @Override
                public void noMatches() {
                    channel.sendMessage("No Match Found").queue();
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    exception.printStackTrace();
                    channel.sendMessage("Failed to Load the track").queue();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            channel.sendMessage("Failed to Load the track").queue();
        }
    }

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }

}

