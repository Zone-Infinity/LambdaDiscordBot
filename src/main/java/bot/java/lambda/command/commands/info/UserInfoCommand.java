package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandType.ICommand;
import bot.java.lambda.utils.Utils;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Guild guild = ctx.getGuild();

        if (args.isEmpty()) {
            final User user = message.getAuthor();
            final Member member = guild.getMember(user);

            if (member == null) {
                channel.sendMessage("No users found for `" + String.join(" ", ctx.getArgs()) + "`").queue();
                return;
            }

            final List<Role> roles = member.getRoles();
            final int roleCount = roles.size();
            String highestRole = "No Role";
            if (!roles.isEmpty()) {
                highestRole = roles.get(0).getAsMention();
            }

            final String status = member.getOnlineStatus().name().toLowerCase();
            String utilStatus = "";
            if (!member.getActivities().isEmpty() && member.getActivities().stream().anyMatch(it -> it.getType() == Activity.ActivityType.STREAMING))
                utilStatus = "s";
            else if (status.startsWith("on")) utilStatus = "on";
            else if (status.startsWith("off")) utilStatus = "off";
            else if (status.startsWith("i")) utilStatus = "idle";
            else if (status.startsWith("d")) utilStatus = "dnd";

            final MessageEmbed embed = EmbedUtils.getDefaultEmbed()
                    .setAuthor(String.format("%#s", user), user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl())
                    .setTitle(Utils.getStatusAsEmote(utilStatus) + " " + member.getEffectiveName())
                    .setThumbnail(user.getEffectiveAvatarUrl())
                    .setColor(member.getColor())
                    .addField("User Id + Mention", String.format("%s {%s}", user.getId(), member.getAsMention()), true)
                    .addField("Account Created", user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                    .addBlankField(true)
                    .addField("Guild Joined", member.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                    .addField("Bot Account", user.isBot() ? "<:TickYes:755716208191602738> " : "<:TickNo:755716160472875079>", true)
                    .addBlankField(true)
                    .addField("Role Count", roleCount + "", true)
                    .addField("Highest Role", highestRole, true)
                    .addBlankField(true)
                    .build();

            channel.sendMessage(embed).queue();
            return;
        }

        String joined = String.join("", args);

        List<User> foundUsers = FinderUtil.findUsers(joined, ctx.getJDA());

        if (foundUsers.isEmpty()) {

            List<Member> foundMembers = FinderUtil.findMembers(joined, guild);

            if (foundMembers.isEmpty()) {
                channel.sendMessage("No users found for `" + joined + "`").queue();
                return;
            }

            foundUsers = foundMembers.stream().map(Member::getUser).collect(Collectors.toList());

        }

        User user = foundUsers.get(0);
        Member member = guild.getMember(user);

        if (member == null) {
            channel.sendMessage("No users found for `" + joined + "`").queue();
            return;
        }

        final List<Role> roles = member.getRoles();
        final int roleCount = roles.size();
        String highestRole = "No Role";
        if (!roles.isEmpty()) {
            highestRole = roles.get(0).getAsMention();
        }

        final String status = member.getOnlineStatus().name().toLowerCase();
        String utilStatus = "";
        if (!member.getActivities().isEmpty() && member.getActivities().stream().anyMatch(it -> it.getType() == Activity.ActivityType.STREAMING))
            utilStatus = "s";
        else if (status.startsWith("on")) utilStatus = "on";
        else if (status.startsWith("off")) utilStatus = "off";
        else if (status.startsWith("i")) utilStatus = "idle";
        else if (status.startsWith("d")) utilStatus = "dnd";

        final MessageEmbed embed = EmbedUtils.getDefaultEmbed()
                .setAuthor(String.format("%#s", user), user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl())
                .setTitle(Utils.getStatusAsEmote(utilStatus) + " " + member.getEffectiveName())
                //.setThumbnail(user.getEffectiveAvatarUrl().replace("gif", "png"))
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setColor(member.getColor())
                .addField("User Id + Mention", String.format("%s {%s}", user.getId(), member.getAsMention()), true)
                .addField("Account Created", user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                .addBlankField(true)
                .addField("Guild Joined", member.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                .addField("Bot Account", user.isBot() ? "<:TickYes:755716208191602738> " : "<:TickNo:755716160472875079>", true)
                .addBlankField(true)
                .addField("Role Count", roleCount + "", true)
                .addField("Highest Role", highestRole, true)
                .addBlankField(true)
                .build();

        channel.sendMessage(embed).queue();

    }

    @Override
    public String getName() {
        return "userinfo";
    }

    @Override
    public String getHelp(String prefix) {
        return "Displays information about a user\n" +
                "Usage : " + prefix + "userinfo < username / @user / user_id >";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }

    @Override
    public int getCoolDown() {
        return 20;
    }

    @Override
    public List<String> getAliases() {
        return List.of("whois", "user", "info", "profile");
    }
}
