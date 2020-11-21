package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class VoteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        // BOTRIX = https://botrix.cc/vote/752052866809593906
        // ROVEL = https://bots.rovelstars.ga/bots/752052866809593906/vote

        final TextChannel channel = ctx.getChannel();


        WebUtils.ins.getJSONObject("https://bots.rovelstars.ga/api/v1/bots/752052866809593906/stats").async(
                (json) -> {
                    final JsonNode general = json.get("general");
                    final int totalVotes = general.get("totalVotes").asInt();
                    final int voteCount = general.get("voteCount").asInt();
                    final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                            .setTitle("Vote Lambda λ")
                            .setDescription("1. [Vote on BOTRIX](https://botrix.cc/vote/752052866809593906)\n" +
                                    "2. [Vote on RBL](https://bots.rovelstars.ga/bots/752052866809593906/vote)\n" +
                                    "")
                            .addField("Votes on RBL",
                                    "Total Votes : " + totalVotes + "\n" +
                                            "Vote Count : " + voteCount
                                    , false);
                    channel.sendMessage(embed.build()).queue();
                }
        );
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
}
