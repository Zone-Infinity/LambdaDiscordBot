package bot.java.lambda.events;

import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MusicEventListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        final Guild guild = event.getGuild();

        if (event.getMember() != guild.getSelfMember()) return;

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        musicManager.setLastChannelId(-1);
    }
}
