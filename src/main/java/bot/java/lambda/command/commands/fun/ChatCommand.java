package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandType.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatCommand implements ICommand {

    List<User> areChatting = new ArrayList<>();
    final WebUtils ins = WebUtils.ins;
    EventWaiter waiter;

    public ChatCommand(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final User author = ctx.getAuthor();

        if (areChatting.contains(author))
            return;

        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        areChatting.add(author);

        final String msg = String.join(" ", args);
        String url = "https://api.snowflakedev.xyz/chatbot?message=" + msg.replaceAll(" ", "%20");
        ins.getJSONObject(url).async(
                json -> {
                    ctx.getMessage().reply(json.get("message").asText()).queue();
                    waitForMessage(channel, author);
                }
        );

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

                    if (message.isEmpty()) {
                        waitForMessage(channel, user);
                        return;
                    }

                    if (message.contains("bye")) {
                        e.getMessage().reply("Bye! \uD83D\uDC4B\uD83C\uDFFB").queue();
                        areChatting.remove(user);
                        return;
                    }

                    String url = "https://api.snowflakedev.xyz/chatbot?message=" + message.replaceAll(" ", "%20");

                    ins.getJSONObject(url).async(
                            json -> {
                                e.getMessage().reply(json.get("message").asText()).mentionRepliedUser(false).queue();
                                waitForMessage(channel, user);
                            }
                    );

                }, 30, TimeUnit.SECONDS, () -> {
                    channel.sendMessage("Looks like I am alone , Bye! \uD83D\uDC4B\uD83C\uDFFB").queue();
                    areChatting.remove(user);
                }
        );
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
