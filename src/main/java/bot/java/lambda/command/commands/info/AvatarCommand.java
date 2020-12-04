package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;

public class AvatarCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();

        final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setThumbnail(null)
                .setFooter("|", "https://media.discordapp.net/attachments/751297245068132472/753934986943528980/1tNXllYx93ipMLK44F6QWQw-removebg-preview.png")
                .setTimestamp(Instant.now());

        if (args.isEmpty()) {
            final User author = ctx.getAuthor();

            try {
                channel.sendMessage("<" + author.getEffectiveAvatarUrl() + "?size=2048>")
                        .addFile(new File(new URI(author.getEffectiveAvatarUrl())), "avatar.png").queue();
            } catch (URISyntaxException e) {
                channel.sendMessage("Something got wrong").queue();
            }
            return;
        }

        final List<Member> mentionedMembers = ctx.getMessage().getMentionedMembers();
        if (!mentionedMembers.isEmpty()) {
            final Member member = mentionedMembers.get(0);
            try {
                channel.sendMessage("<" + member.getUser().getEffectiveAvatarUrl() + "?size=2048>")
                        .addFile(new File(new URI(member.getUser().getEffectiveAvatarUrl())), "avatar.png").queue();
            } catch (URISyntaxException e) {
                channel.sendMessage("Something got wrong").queue();
            }
            return;
        }

        if (!isDiscordID(args.get(0))) {
            channel.sendMessage("Arguments doesn't have a mention or a id").queue();
            return;
        }

        final Member memberById = ctx.getGuild().getMemberById(args.get(0));

        if (memberById == null) {
            channel.sendMessage("No user found having this id").queue();
            return;
        }


        try {
            channel.sendMessage("<" + memberById.getUser().getEffectiveAvatarUrl() + "?size=2048>")
                    .addFile(new File(new URI(memberById.getUser().getEffectiveAvatarUrl())), "avatar.png").queue();
        } catch (URISyntaxException e) {
            channel.sendMessage("Something got wrong").queue();
        }

    }

    public static boolean isDiscordID(String s) {
        boolean hasDigits = false;
        try {
            Long.parseLong(s);
            hasDigits = true;
        } catch (NumberFormatException e) {
            e.fillInStackTrace();
        }
        return hasDigits && s.length() == 18;
    }

    @Override
    public String getName() {
        return "avatar";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sends the avatar of you or the user <mention/id>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }

    @Override
    public List<String> getAliases() {
        return List.of("av", "pfp", "avt", "profilepic");
    }
}
