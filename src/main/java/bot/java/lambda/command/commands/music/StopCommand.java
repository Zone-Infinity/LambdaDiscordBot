package bot.java.lambda.command.commands.music;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.util.concurrent.TimeUnit;

public class StopCommand implements ICommand {
    EventWaiter waiter;

    public StopCommand(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getMusicManager(ctx.getGuild());
        final TextChannel channel = ctx.getChannel();

        if (ctx.getMember().getVoiceState() == null) {
            channel.sendMessage("You need to be in a voice channel for this command to work").queue();
            return;
        }

        if (playerManager.getMusicManager(ctx.getGuild()).audioPlayer.getPlayingTrack() == null) {
            channel.sendMessage("Nothing in the queue to clear").queue();
            return;
        }

        final GuildVoiceState voiceState = ctx.getSelfMember().getVoiceState();
        final int size = voiceState.getChannel().getMembers().size();

        if (voiceState.getChannel().getMembers().size() == 2) {
            channel.sendMessage("Stopping the player and clearing the queue").queue();
            musicManager.scheduler.getQueue().clear();
            musicManager.audioPlayer.stopTrack();
            musicManager.audioPlayer.setPaused(false);
            return;
        }

        channel.sendMessage("React to the message to skip\n" +
                "Need " + (size - 2) + " reactions ( only ⏹️)").queue(
                message -> {
                    message.addReaction("⏹️").queue();
                    waiter.waitForEvent(MessageReactionAddEvent.class,
                            e -> e.getReaction().retrieveUsers().stream().count() > size - 2 &&
                                    e.getChannel().equals(channel) &&
                                    e.getMessageIdLong() == message.getIdLong(),
                            e -> {
                                channel.sendMessage("Stopping the player and clearing the queue").queue();
                                musicManager.scheduler.getQueue().clear();
                                musicManager.audioPlayer.stopTrack();
                                musicManager.audioPlayer.setPaused(false);
                            },
                            70, TimeUnit.SECONDS, () -> channel.sendMessage("Time up !! can't stop").queue());
                }
        );
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getHelp(String prefix) {
        return "Stops the music player";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }
}
