package bot.java.lambda.command.commands.owner.DMs;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.events.PrivateMessageListener;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class UnblockCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        if (!channel.getId().equals("766902384584032287"))
            return;

        final String id = ctx.getArgs().get(0);

        final User user = ctx.getJDA().getUserById(id);

        if (user == null) {
            channel.sendMessage("User doesn't exists").queue();
            return;
        }

        ctx.getMessage().addReaction(":TickYes:755716208191602738").queue();
        PrivateMessageListener.blockedUsers.remove(user);
    }

    @Override
    public String getName() {
        return "unblock";
    }

    @Override
    public String getHelp() {
        return "Unblocks Someone";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.OWNER;
    }
}
