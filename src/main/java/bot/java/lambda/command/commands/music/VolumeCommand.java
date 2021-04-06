package bot.java.lambda.command.commands.music;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandType.ICommand;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class VolumeCommand implements ICommand {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();

        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            channel.sendMessage("I need to be in a voice channel for this to work").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command to work").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            channel.sendMessage("You need to be in the same voice channel as me for this to work").queue();
            return;
        }

        final AudioPlayer player = PlayerManager.getInstance().getMusicManager(ctx.getGuild()).audioPlayer;

        if (args.isEmpty()) {
            channel.sendMessageFormat("The current volume is **%s**", player.getVolume()).queue();
            return;
        }

        try {
            int newVolume = Math.max(5, Math.min(1000, Integer.parseInt(args.get(0))));
            int oldVolume = player.getVolume();

            player.setVolume(newVolume);

            channel.sendMessageFormat("Player volume changed from **%s** to **%s**", oldVolume, newVolume).queue();
        } catch (NumberFormatException e) {
            channel.sendMessageFormat("**%s** is not a valid integer. (5 - 1000)", args.get(0)).queue();
            e.fillInStackTrace();
        }

    }

    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sets the volume on the music player\n" +
                "Usage : " + prefix + "volume <volume>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }

    @Override
    public int getCoolDown() {
        return 0;
    }
}
