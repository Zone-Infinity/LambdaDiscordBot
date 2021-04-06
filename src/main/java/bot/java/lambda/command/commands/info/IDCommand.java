package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandType.ICommand;
import net.dv8tion.jda.api.entities.*;

import java.util.List;

public class IDCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();

        if (args.isEmpty()) {
            channel.sendMessageFormat("Guild ID : %s\n" +
                    "Current Channel ID : %s\n" +
                    "Your ID : %s", ctx.getGuild().getId(), channel.getId(), ctx.getAuthor().getId()).queue();
            return;
        }

        final Message message = ctx.getMessage();
        final List<Member> mentionedMembers = message.getMentionedMembers();

        if (!mentionedMembers.isEmpty()) {
            final Member member = mentionedMembers.get(0);
            channel.sendMessage("User ID : " + member.getId()).queue();
            return;
        }

        final List<TextChannel> mentionedChannels = message.getMentionedChannels();

        if (!mentionedChannels.isEmpty()) {
            final TextChannel ch = mentionedChannels.get(0);
            channel.sendMessage("Channel ID : " + ch.getId()).queue();
            return;
        }

        final List<Role> mentionedRoles = message.getMentionedRoles();

        if (!mentionedRoles.isEmpty()) {
            final Role role = mentionedRoles.get(0);
            channel.sendMessage("Role ID : " + role.getId()).queue();
            return;
        }

        final List<Emote> emotes = message.getEmotes();

        if (!emotes.isEmpty()) {
            final Emote emote = emotes.get(0);
            channel.sendMessage("Emote ID : " + emote.getId()).queue();
            return;
        }

        channel.sendMessage("No ID found for this").queue();

    }

    @Override
    public String getName() {
        return "id";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives you the id of <user/channel/emoji/guild/role>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }
}
