package bot.java.lambda.command.commands.music;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandType.ICommand;
import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PauseCommand implements ICommand {

    private final EventWaiter waiter;

    public PauseCommand(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getMusicManager(ctx.getGuild());
        AudioPlayer player = musicManager.audioPlayer;

        if (!ctx.getMember().getVoiceState().inVoiceChannel()) {
            channel.sendMessage("You are not in the voice channel").queue();
            return;
        }

        if (player.getPlayingTrack() == null) {
            channel.sendMessage("The player isn't playing anything").queue();
            return;
        }

        final GuildVoiceState voiceState = ctx.getSelfMember().getVoiceState();
        final List<Member> members = voiceState.getChannel().getMembers();
        final int size = members.size();

        if (size < 3) {
            channel.sendMessage("⏸️ Track Paused").queue();
            player.setPaused(!player.isPaused());
            return;
        }
        channel.sendMessage("React to the message to skip\n" +
                "Need " + (size - 2) + " reactions ( only  ⏸️️ )").queue(
                message -> {
                    message.addReaction("⏸️").queue();
                    waiter.waitForEvent(MessageReactionAddEvent.class,
                            e -> e.getReaction().retrieveUsers().stream().count() > size - 2 &&
                                    e.getChannel().equals(channel) &&
                                    e.getMessageIdLong() == message.getIdLong(),
                            e -> {
                                channel.sendMessage("⏸️ Track Paused").queue();
                                player.setPaused(!player.isPaused());
                            },
                            70, TimeUnit.SECONDS, () -> channel.sendMessage("Time up !! can't pause").queue());
                }
        );
    }

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getHelp(String prefix) {
        return "Pauses the player if it's playing\n" +
                "Resumes the player if it's paused";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }
}
