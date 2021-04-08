package bot.java.lambda.command.commands.music;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.ICommand;
import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import bot.java.lambda.utils.Utils;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class NowPlayingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getMusicManager(ctx.getGuild());
        final AudioPlayer player = musicManager.audioPlayer;

        if (player.getPlayingTrack() == null) {
            channel.sendMessage("The player is not playing any track").queue();
            return;
        }

        AudioTrackInfo info = player.getPlayingTrack().getInfo();

        final long position = player.getPlayingTrack().getPosition();
        final long length = info.length;
        int Position = (int) (position / (length / 8.0));

        StringBuilder bar = new StringBuilder();

        bar.append("▶️ ");
        for (int i = 0; i < 8; i++) {
            if (i == Position) {
                bar.append("\uD83D\uDD18");
                continue;
            }
            bar.append("▬");
        }
        bar.append(" `[").append(Utils.getTimestamp(position))
                .append("/").append(Utils.getTimestamp(length)).append("]` ");
        bar.append("\uD83D\uDD0A");

        channel.sendMessage(EmbedUtils.embedMessage(String.format(
                "**Playing**  [%s](%s) by %s\n" +
                        "%s", info.title, info.uri, info.author, bar.toString()
        )).build()).queue();

    }

    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows currently playing track";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }

    @Override
    public List<String> getAliases() {
        return List.of("np", "nplaying", "nowp");
    }
}
