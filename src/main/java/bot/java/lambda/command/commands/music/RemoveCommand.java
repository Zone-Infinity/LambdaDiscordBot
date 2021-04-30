package bot.java.lambda.command.commands.music;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import bot.java.lambda.command.type.ICommand;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class RemoveCommand implements ICommand {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getMusicManager(ctx.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command to work").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel() || queue.isEmpty()) {
            channel.sendMessage("Cannot remove when Player is not playing anything").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            channel.sendMessage("You need to be in the same voice channel as me for this to work").queue();
            return;
        }

        final List<String> args = ctx.getArgs();

        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        int index = -1;

        try {
            index = Integer.parseInt(args.get(0));
        } catch (NumberFormatException ignored) {
        }

        final AudioTrack track = getTrack(queue, index);
        queue.remove(track);
        channel.sendMessage(track.getInfo().title + " was removed from the queue").queue();

    }

    private AudioTrack getTrack(BlockingQueue<AudioTrack> queue, int index) {
        if (index >= queue.size() || index == -1) return null;

        for (int i = 0; i < queue.size(); i++) {
            if (i == index) return queue.peek();
            queue.remove();
        }

        return null;
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getHelp(String prefix) {
        return "Removes a track in the queue\n" +
                "Usage: " + prefix + " remove <number (index of the track) >";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }
}
