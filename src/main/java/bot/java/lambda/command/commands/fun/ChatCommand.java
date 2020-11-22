package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatCommand implements ICommand {

    EventWaiter waiter;

    public ChatCommand(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();

        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        final String msg = String.join(" ", args);
        ctx.getMessage().reply(getReply(msg)).queue();
        waitForMessage(channel, ctx.getAuthor());

    }

    private void waitForMessage(TextChannel channel, User user) {
        waiter.waitForEvent(
                GuildMessageReceivedEvent.class,
                e -> e.getAuthor() == user &&
                        e.getChannel() == channel,
                e -> {
                    String message = e.getMessage().getContentRaw();

                    if (message.startsWith(">chat")) {
                        channel.sendMessage("I am already talking to you, You don't need to have `>chat`").queue();
                        message = message.replaceFirst(">chat", "");
                    }

                    if (message.startsWith("bye")) {
                        e.getMessage().reply("Bye! \uD83D\uDC4B\uD83C\uDFFB").queue();
                        return;
                    }

                    e.getMessage().reply(getReply(message)).queue();
                    waitForMessage(channel, user);

                }, 30, TimeUnit.SECONDS, () -> channel.sendMessage("Bye! \uD83D\uDC4B\uD83C\uDFFB").queue()
        );
    }

    private String getReply(String msg) {
        String url = "https://api.snowflakedev.xyz/chatbot?message=" + msg.replaceAll(" ", "%20");
        return WebUtils.ins.getJSONObject(url).execute().get("message").asText();
    }

    @Override
    public String getName() {
        return "chat";
    }

    @Override
    public String getHelp(String prefix) {
        return "Chat with the bot\n" +
                "Usage : " + prefix + "chat <message>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.FUN;
    }

    @Override
    public int getCoolDown() {
        return 5;
    }
}
