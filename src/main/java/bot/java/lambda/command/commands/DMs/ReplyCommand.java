package bot.java.lambda.command.commands.DMs;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class ReplyCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        if (!channel.getId().equals("766902384584032287"))
            return;

        final List<String> args = ctx.getArgs();

        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        final String id = args.get(0);
        final String message = String.join(" ", args.subList(1, args.size()));

        final User user = ctx.getJDA().getUserById(id);

        if (user == null) {
            channel.sendMessage("User doesn't exists").queue();
            return;
        }

        user.openPrivateChannel().queue(
                privateChannel -> privateChannel.sendMessage(EmbedUtils.getDefaultEmbed()
                        .setAuthor(ctx.getAuthor().getName() + "#" + ctx.getAuthor().getDiscriminator(), ctx.getAuthor().getEffectiveAvatarUrl(), ctx.getAuthor().getEffectiveAvatarUrl())
                        .setDescription(message)
                        .build()).queue()
        );
        ctx.getMessage().addReaction(":TickYes:755716208191602738").queue();

    }

    @Override
    public String getName() {
        return "reply";
    }

    @Override
    public String getHelp() {
        return ">reply <user_id> <message>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.OWNER;
    }
}
