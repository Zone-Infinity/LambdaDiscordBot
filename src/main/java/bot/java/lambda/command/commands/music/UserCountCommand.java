package bot.java.lambda.command.commands.music;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class UserCountCommand implements ICommand {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member member = ctx.getMember();
        final GuildVoiceState voiceState = member.getVoiceState();
        if (!voiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this to work").queue();
            return;
        }
        final int size = voiceState.getChannel().getMembers().size();
        channel.sendMessage("Users in your voice channel : " + size).queue();

    }

    @Override
    public String getName() {
        return "user";
    }

    @Override
    public String getHelp() {
        return "Gives you the number of users there in your voice channel\n" +
                "Aliases : {u}";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }

    @Override
    public List<String> getAliases() {
        return List.of("u");
    }
}
