package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class VoteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        String BOTRIX = "https://botrix.cc/vote/752052866809593906";
        String ROVEL = "https://bots.rovelstars.ga/bots/752052866809593906/vote";
        String INFINITY = "https://infinitybotlist.com/bots/752052866809593906/vote";

        final TextChannel channel = ctx.getChannel();

        final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setTitle("Vote Lambda λ")
                .setDescription("1. [Vote on RovelBotList](" + ROVEL + ")\n" +
                        "2. [Vote on BOTRIX](" + BOTRIX + ")\n" +
                        "3. [Vote on InfinityBotList](" + INFINITY + ")\n" +
                        "")
                .addField("Votes on Rovel Bot List", getVotes("rbl"), false)
                .addField("Votes on BOTRIX", getVotes("botrix"), false)
                .addField("Votes on Infinity Bot List", getVotes("infinity"), false);
        channel.sendMessage(embed.build()).queue();

    }

    private String getVotes(String botList) {
        final Map<String, String> votes = new HashMap<>();

        final WebUtils ins = WebUtils.ins;

        // ROVEL BOT LIST
        ins.getJSONObject("https://bots.rovelstars.ga/api/v1/bots/752052866809593906/stats").async(
                (json) -> {
                    final JsonNode general = json.get("general");
                    final int totalVotes = general.get("totalVotes").asInt();
                    final int voteCount = general.get("voteCount").asInt();

                    String voteString = String.format(
                            "Total Votes : %s\nVote Count : %s", totalVotes, voteCount
                    );

                    votes.put("rbl", voteString);

                }
        );

        // BOTRIX BOT LIST
        ins.getJSONObject("https://botrix.cc/api/v1/bot/752052866809593906").async(
                (json) -> {
                    final JsonNode general = json.get("bot");
                    final int totalVotes = general.get("votes").asInt();

                    String voteString = String.format(
                            "Total Votes : %s", totalVotes
                    );

                    votes.put("botrix", voteString);

                }
        );

        // INFINITY BOT LIST
        ins.getJSONObject("https://infinitybotlist.com/api/bots/752052866809593906/info").async(
                (json) -> {
                    final JsonNode totalVotes = json.get("votes");

                    String voteString = String.format(
                            "Total Votes : %s", totalVotes
                    );

                    votes.put("infinity", voteString);

                }
        );


        final String vote = votes.get(botList.toLowerCase());
        return vote == null ? "" : vote;
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
