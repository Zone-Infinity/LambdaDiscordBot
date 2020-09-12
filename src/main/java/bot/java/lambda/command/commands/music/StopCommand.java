package bot.java.lambda.command.commands.music;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Objects;

public class StopCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getMusicManager(ctx.getGuild());
        final TextChannel channel = ctx.getChannel();

        musicManager.scheduler.getQueue().clear();
        musicManager.audioPlayer.stopTrack();
        musicManager.audioPlayer.setPaused(false);

        if(ctx.getMember().getVoiceState()==null){
            channel.sendMessage("You need to be in a voice channel for this command to work").queue();
        }

        if(playerManager.getMusicManager(ctx.getGuild()).audioPlayer.getPlayingTrack()==null){
            channel.sendMessage("Nothing in the queue to clear").queue();
            return;
        }

        musicManager.scheduler.getQueue().clear();
        musicManager.audioPlayer.stopTrack();
        musicManager.audioPlayer.setPaused(false);
        channel.sendMessage("Stopping the player and clearing the queue").queue();
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getHelp() {
        return "Stops the music player";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }
}
