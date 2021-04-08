package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class VoteCommand implements ICommand {
    final Map<String, String> voteLinks = new HashMap<>();

    public VoteCommand() {
        voteLinks.put("TopGG", "https://top.gg/bot/752052866809593906/vote");
        voteLinks.put("Discord Bot List", "https://discordbotlist.com/bots/lambda-l/upvote");
        voteLinks.put("Botrix", "https://botrix.cc/vote/752052866809593906");
        voteLinks.put("Infinity Bot List", "https://infinitybots.xyz/bots/752052866809593906/vote");
        // voteLinks.put("Rovel", "???");
    }

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        StringBuilder description = new StringBuilder();

        int count = 1;
        for (Map.Entry<String, String> entry : voteLinks.entrySet()) {
            description.append(String.format("%d. [Vote on %s](%s)\n", count, entry.getKey(), entry.getValue()));
            count++;
        }

        final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setTitle("Vote Lambda λ")
                .setDescription(description);
        channel.sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "vote";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives u the vote links for the bot";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }

    @Override
    public int getCoolDown() {
        return 60;
    }
}
