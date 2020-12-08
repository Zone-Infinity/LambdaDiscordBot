package bot.java.lambda.command.commands.music;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import bot.java.lambda.utils.Utils;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class PlayCommand implements ICommand {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) throws FriendlyException {
        final TextChannel channel = ctx.getChannel();

        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command to work").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel()) {
            channel.sendMessage("I need to be in a voice channel for this command to work... Do `>join`").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            channel.sendMessage("You need to be in the same voice channel as me for this to work").queue();
            return;
        }

        if (ctx.getArgs().isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        String link = String.join(" ", ctx.getArgs());

        if (Utils.isNotUrl(link)) {
            link = "ytsearch:" + link + " .";
        }

        System.out.println(link);

        PlayerManager.getInstance().loadAndPlay(channel, link, false);
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp(String prefix) {
        return "Plays a song\n" +
                "Usage: <prefix> play <youtube link / name>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }

    @Override
    public List<String> getAliases() {
        return List.of("p", "playsong");
    }
}
