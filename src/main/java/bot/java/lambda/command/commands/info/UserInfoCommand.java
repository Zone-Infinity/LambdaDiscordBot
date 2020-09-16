package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserInfoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        User user;
        if(args.isEmpty()){
            channel.sendMessage("Missing Argument, check `>help userinfo`").queue();
            return;
        }

        String joined = String.join("",args);
        List<User> foundUsers = FinderUtil.findUsers(joined, ctx.getJDA());

        if(foundUsers.isEmpty()){

            List<Member> foundMembers = FinderUtil.findMembers(joined, ctx.getGuild());

            if(foundMembers.isEmpty()){
                channel.sendMessage("No users found for `"+joined+"`").queue();
                return;
            }

            foundUsers = foundMembers.stream().map(Member::getUser).collect(Collectors.toList());

        }

        user = foundUsers.get(0);
        Member member = ctx.getGuild().getMember(user);

        MessageEmbed embed = EmbedUtils.getDefaultEmbed()
                .setColor(Objects.requireNonNull(member).getColor())
                .setThumbnail(user.getEffectiveAvatarUrl().replaceFirst("gif","png"))
                .addField("Username#Discriminator", String.format("%#s,",user), false)
                .addField("Display name", member.getEffectiveName(), false)
                .addField("User Id + Mention", String.format("%s (%s)",user.getId(), member.getAsMention()),false)
                .addField("Account Created",user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME), false)
                .addField("Guild joined", member.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME), false)
                .addField("Online Status", member.getOnlineStatus().name().toLowerCase().replaceAll("_"," "), false)
                .addField("Bot Account", user.isBot()?"Yes":"No", false)
                .build();

        channel.sendMessage(embed).queue();

    }

    @Override
    public String getName() {
        return "userinfo";
    }

    @Override
    public String getHelp() {
        return "Displays information about a user\n" +
                "Usage : >userinfo <user_name/@user/user_id>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }
}
