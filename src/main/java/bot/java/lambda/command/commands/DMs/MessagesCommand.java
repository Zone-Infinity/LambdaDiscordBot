package bot.java.lambda.command.commands.DMs;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class MessagesCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        if(!channel.getId().equals("766902384584032287"))
            return;

        final String id = ctx.getArgs().get(0);

        if(ctx.getArgs().isEmpty()){
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        final User user = ctx.getJDA().getUserById(id);

        if(user==null){
            channel.sendMessage("User doesn't exists").queue();
            return;
        }

        StringBuilder messages = new StringBuilder();

        user.openPrivateChannel().queue(
                privateChannel -> {
                    final List<Message> history = privateChannel.getHistory().getRetrievedHistory();

                    for(Message m : history){
                        messages.append(m.getContentRaw()).append("\n");
                    }
                }
        );

        channel.sendMessage(EmbedUtils.getDefaultEmbed()
                                .setDescription(messages)
                                .setTitle(user.getName()+"#"+user.getDiscriminator())
                                .setThumbnail(user.getEffectiveAvatarUrl())
                                .setDescription(messages)
                                .setFooter(user.getId())
                                .build()).queue();

    }

    @Override
    public String getName() {
        return "messages";
    }

    @Override
    public String getHelp() {
        return ">messages <user_id>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.OWNER;
    }
}
