package bot.java.lambda.command.commands.music;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import bot.java.lambda.utils.Utils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class QueueCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getMusicManager(ctx.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();

        EmbedBuilder builder = EmbedUtils.getDefaultEmbed();

        if (queue.isEmpty()) {
            builder.setThumbnail("https://images-ext-2.discordapp.net/external/frvcoRmUh1i7edHL92fPYF3qwlg8ZQ-Hh6v1tFeRF6s/%3Fv%3D1/https/cdn.discordapp.com/emojis/753940153780928522.png")
                    .setTitle("The queue is empty")
                    .setDescription("Use -play <song_url> for adding song in the queue");
            channel.sendMessage(builder.build()).queue();
            return;
        }

        final int trackCount = Math.min(queue.size(), 20);
        List<AudioTrack> tracks = new ArrayList<>(queue);

        builder.setTitle("Current Queue (Total : " + queue.size() + ")");

        int count = 1;
        for (AudioTrack track : tracks) {

            if (count == trackCount + 1) break;

            AudioTrackInfo info = track.getInfo();

            builder.appendDescription(String.format(
                    "|` %s -> %s [%s]`\n",
                    info.title,
                    info.author,
                    Utils.getTimestamp(track.getDuration())
            ));
            count++;
        }

        if (tracks.size() > trackCount) {
            builder.appendDescription("and `" + (tracks.size() - trackCount) + "` more...");
        }


        channel.sendMessage(builder.build()).queue();
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getHelp() {
        return "Shows the current queue for the music player\n" +
                "Aliases : {q, songlist}";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }

    @Override
    public List<String> getAliases() {
        return List.of("q", "songlist");
    }
}
