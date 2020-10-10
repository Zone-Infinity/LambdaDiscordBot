package bot.java.lambda.command.commands.Owner;

import bot.java.lambda.Config;
import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.concurrent.TimeUnit;

public class TestCommand implements ICommand {
    EventWaiter waiter;
    int i = 0;
    public TestCommand(EventWaiter waiter){
        this.waiter = waiter;
    }
    @Override
    public void handle(CommandContext ctx) {
        if(!ctx.getAuthor().getId().equals(Config.get("owner_id")))
            return;
        ctx.getChannel().sendMessage(i+"").queue(
                message -> {
                    message.addReaction("⬅").queue();
                    message.addReaction("🛑").queue();
                    message.addReaction("➡").queue();
                    testWaiter(message, ctx);
                }
        );
    }

    private void testWaiter(Message message, CommandContext ctx){
        waiter.waitForEvent(
                GuildMessageReactionAddEvent.class,
                e -> e.getUser().equals(ctx.getAuthor())
                        && e.getChannel().equals(ctx.getChannel())
                        && !e.getMessageId().equals(ctx.getMessage().getId()),
                e -> {
                    final String asReactionCode = e.getReactionEmote().getAsReactionCode();
                    e.getReaction().removeReaction(e.getUser()).queue();

                    if(!asReactionCode.equals("🛑")){
                        if(asReactionCode.equals("➡")) {
                            i++;
                            message.editMessage(i+"").queue();
                        }
                        if(asReactionCode.equals("⬅")) {
                            i--;
                            message.editMessage(i+"").queue();
                        }
                        testWaiter(message, ctx);
                        return;
                    }
                    message.editMessage("Deleting message in 5 seconds").queue();
                    message.delete().queueAfter(5, TimeUnit.SECONDS);
                    ctx.getMessage().delete().queue();
                }
        );
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getHelp() {
        return "Testing Commands";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.OWNER;
    }
}
