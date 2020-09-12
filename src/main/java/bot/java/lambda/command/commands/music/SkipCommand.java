package bot.java.lambda.command.commands.music;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import bot.java.lambda.command.commands.music.lavaplayer.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.Objects;

public class SkipCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getMusicManager(ctx.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        AudioPlayer player = musicManager.audioPlayer;

        if(player.getPlayingTrack()==null){
            channel.sendMessage("The player isn't playing anything").queue();
            return;
        }

        try{
            if (ctx.getMember().hasPermission(Permission.ADMINISTRATOR) || Objects.requireNonNull(Objects.requireNonNull(ctx.getSelfMember().getVoiceState()).getChannel()).getMembers().size() <= 3) {
                scheduler.nextTrack();
                channel.sendMessage("Skipping the current track").queue();
                return;
            }
            int skips = 0;
            skips++;
            if (skips == Objects.requireNonNull(Objects.requireNonNull(ctx.getSelfMember().getVoiceState()).getChannel()).getMembers().size() - 2) {
                scheduler.nextTrack();
                channel.sendMessage("Skipping the current track").queue();
            }

        }catch (IllegalStateException e){
            e.fillInStackTrace();
            scheduler.nextTrack();
            channel.sendMessage("Skipping the current track").queue();
        }
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "Skips the current song";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }

    @Override
    public List<String> getAliases() {
        return List.of("s","skipsong");
    }
}
