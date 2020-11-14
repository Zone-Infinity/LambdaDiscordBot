package bot.java.lambda.command.commands.games;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CountCommand implements ICommand {

    private final EventWaiter waiter;

    private final List<Guild> countGoingOnGuild = new ArrayList<>();
    private final Map<Guild, Integer> num = new HashMap<>();

    public CountCommand(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Guild guild = ctx.getGuild();

        if (countGoingOnGuild.contains(guild)) {
            channel.sendMessage("Count is going on, \n" +
                    "can't start a new count in this guild").queue();
            return;
        }

        countGoingOnGuild.add(guild);
        num.putIfAbsent(guild, 1);

        channel.sendMessage("Start counting from `1`").queue();

        waitForEvent(channel, guild);

    }

    private void waitForEvent(TextChannel channel, Guild guild) {
        waiter.waitForEvent(
                GuildMessageReceivedEvent.class,
                e -> e.getChannel().equals(channel),
                e -> {
                    final Message message = e.getMessage();
                    final StringBuilder sb = new StringBuilder();
                    for (String s : message.getContentRaw().split("")) {
                        if (Character.isDigit(s.charAt(0))) {
                            sb.append(s);
                        } else {
                            break;
                        }
                    }

                    if (sb.toString().isEmpty()) {
                        waitForEvent(channel, guild);
                        return;
                    }

                    int i = Integer.parseInt(sb.toString());

                    if (i == num.get(guild)) {
                        message.addReaction(":TickYes:755716208191602738").queue();
                        num.replace(guild, num.get(guild) + 1);
                        waitForEvent(channel, guild);
                    } else {
                        message.addReaction(":TickNo:755716160472875079").queue();
                        channel.sendMessage(e.getAuthor().getAsMention() + " ruined the counting on number " + (num.get(guild) - 1)).queue();
                        countGoingOnGuild.remove(guild);
                        num.replace(guild, 1);
                    }
                }, 15, TimeUnit.SECONDS, () -> {
                    channel.sendMessage("You took too long to count further").queue();
                    countGoingOnGuild.remove(guild);
                    num.replace(guild, 1);
                }
        );
    }

    @Override
    public String getName() {
        return "count";
    }

    @Override
    public String getHelp(String prefix) {
        return "Starts a count session\n" +
                "try it out `>count`\n" +
                "You have to only count\n" +
                "If the number is not the next number you lose\n" +
                "and if u don't reply in 15 seconds you lose";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.GAME;
    }

    @Override
    public int getCoolDown() {
        return 10;
    }

}
