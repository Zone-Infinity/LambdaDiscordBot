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
        String BOTRIX = "https://botrix.cc/vote/752052866809593906";
        String ROVEL = "https://bots.rovelstars.ga/bots/752052866809593906/vote";
        String INFINITY = "https://infinitybotlist.com/bots/752052866809593906/vote";

        final TextChannel channel = ctx.getChannel();

        final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setTitle("Vote Lambda λ")
                .setDescription("1. [Vote on RovelBotList](" + ROVEL + ")\n" +
                        "2. [Vote on BOTRIX](" + BOTRIX + ")\n" +
                        "3. [Vote on InfinityBotList](" + INFINITY + ")\n" +
                        "");
        sendVotes(channel, embed);
    }

    private void sendVotes(TextChannel channel, EmbedBuilder voteEmbed) {
        final WebUtils ins = WebUtils.ins;

        String BOTRIX = "https://botrix.cc/api/v1/bot/752052866809593906";
        String ROVEL = "https://bots.rovelstars.ga/api/v1/bots/752052866809593906/stats";
        String INFINITY = "https://infinitybotlist.com/api/bots/752052866809593906/info";

        ins.getJSONObject(ROVEL).async(
                rbl -> ins.getJSONObject(BOTRIX).async(
                        botrix -> ins.getJSONObject(INFINITY).async(
                                infinity -> {
                                    final JsonNode rblGeneral = rbl.get("general");
                                    final int RblTotalVotes = rblGeneral.get("totalVotes").asInt();
                                    final int RblVoteCount = rblGeneral.get("voteCount").asInt();

                                    final String botrixTotalVotes = botrix.get("bot").get("votes").asText();

                                    String infinityTotalVotes = "not available";

                                    if (!infinity.get("error").asBoolean())
                                        infinityTotalVotes = infinity.get("votes").asText();

                                    voteEmbed
                                            .addField("Votes on Rovel Bot List",
                                                    String.format(
                                                            "Total Votes : %s\nVote Count : %s", RblTotalVotes, RblVoteCount
                                                    ), false
                                            )
                                            .addField("Votes on BOTRIX",
                                                    String.format(
                                                            "Total Votes : %s", botrixTotalVotes
                                                    ), false)
                                            .addField("Votes on Infinity Bot List",
                                                    String.format(
                                                            "Total Votes : %s", infinityTotalVotes
                                                    ), false);

                                    channel.sendMessage(voteEmbed.build()).queue();
                                }
                        )
                )
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

    @Override
    public int getCoolDown() {
        return 60;
    }
}
