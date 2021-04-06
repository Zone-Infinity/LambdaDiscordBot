package bot.java.lambda.command.commands.music;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandType.ICommand;
import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import bot.java.lambda.command.commands.music.lavaplayer.TrackScheduler;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ConstantConditions")
public class SkipCommand implements ICommand {
    EventWaiter waiter;

    public SkipCommand(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final Guild guild = ctx.getGuild();
        final GuildMusicManager musicManager = playerManager.getMusicManager(guild);
        TrackScheduler scheduler = musicManager.scheduler;
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
            channel.sendMessage("<:NextTrack:755716597842182164> Track Skipped").queue();
            try {
                scheduler.nextTrack();
            } catch (IllegalStateException e) {
                e.fillInStackTrace();
            }
            return;
        }
        channel.sendMessage("React to the message to skip\n" +
                "Need " + (size - 2) + " reactions ( only <:NextTrack:755716597842182164> )").queue(
                message -> {
                    message.addReaction(":NextTrack:755716597842182164").queue();
                    waiter.waitForEvent(MessageReactionAddEvent.class,
                            e -> e.getReaction().retrieveUsers().stream().count() > size - 2 &&
                                    e.getChannel().equals(channel) &&
                                    e.getMessageIdLong() == message.getIdLong(),
                            e -> {
                                channel.sendMessage("Track Skipped ").queue();
                                try {
                                    scheduler.nextTrack();
                                } catch (IllegalStateException ex) {
                                    ex.fillInStackTrace();
                                }
                            },
                            70, TimeUnit.SECONDS, () -> {});
                }
        );
    }


    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp(String prefix) {
        return "Skips the current song";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }

    @Override
    public List<String> getAliases() {
        return List.of("s", "skipsong", "next");
    }
}
