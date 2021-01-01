package bot.java.lambda.command.commands.music;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class DisconnectCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        if (!selfVoiceState.inVoiceChannel()) {
            channel.sendMessage("I'm not in a voice channel").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (selfVoiceState.getChannel().getMembers().size() - 1 > 1) {
            if (!member.hasPermission(Permission.MANAGE_SERVER)) {
                channel.sendMessage("You don't have ADMIN Permission\n" +
                        "The bot will disconnect automatically if is inactive").queue();
                return;
            }
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();

        if (!memberVoiceState.inVoiceChannel()) {
            if (selfVoiceState.getChannel().getMembers().size() - 1 == 0) {
                channel.sendMessage("👍🏻").queue();
            } else {
                channel.sendMessage("You need to be in a voice channel for this command to work").queue();
                return;
            }
        }

        channel.sendMessageFormat("Disconnected `\uD83D\uDD0A %s`", selfVoiceState.getChannel().getName()).queue();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        musicManager.scheduler.queue.clear();
        musicManager.audioPlayer.stopTrack();
        musicManager.audioPlayer.setPaused(false);
        musicManager.scheduler.loopEnabled = false;
        audioManager.closeAudioConnection();

    }

    @Override
    public String getName() {
        return "disconnect";
    }

    @Override
    public String getHelp(String prefix) {
        return "Makes the bot disconnect from it's voice channel";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }

    @Override
    public List<String> getAliases() {
        return List.of("dis", "d");
    }
}

