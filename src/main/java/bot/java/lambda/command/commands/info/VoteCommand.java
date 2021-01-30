package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class VoteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        String BOTRIX = "https://botrix.cc/vote/752052866809593906";
        String ROVEL = "https://bots.rovelstars.ga/bots/752052866809593906/vote";
        String INFINITY = "https://infinitybotlist.com/bots/752052866809593906/vote";
        String DISCORD = "https://discordbotlist.com/bots/lambda-l/upvote";
        String TOPGG = "https://top.gg/bot/752052866809593906/vote";

        final TextChannel channel = ctx.getChannel();

        final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setTitle("Vote Lambda λ")
                .setDescription("1. [Vote on TopGG](" + TOPGG + ")\n" +
                        "2. [Vote on RovelBotList](" + ROVEL + ")\n" +
                        "3. [Vote on BOTRIX](" + BOTRIX + ")\n" +
                        "4. [Vote on InfinityBotList](" + INFINITY + ")\n" +
                        "5. [Vote on DiscordBotList](" + DISCORD + ")" +
                        "");
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
