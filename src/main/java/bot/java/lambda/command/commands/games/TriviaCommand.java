package bot.java.lambda.command.commands.games;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandType.ICommand;
import bot.java.lambda.utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TriviaCommand implements ICommand {
    private final EventWaiter waiter;

    public TriviaCommand(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        WebUtils.ins.getJSONObject("https://opentdb.com/api.php?amount=1").async(
                (json) -> {
                    List<String> options = new ArrayList<>();
                    if (!(json.get("response_code").asInt() == 0)) {
                        channel.sendMessage("Error : try afterwards").queue();
                        return;
                    }
                    final JsonNode results = json.get("results").get(0);

                    final String category = results.get("category").asText();
                    final String difficulty = results.get("difficulty").asText();
                    final String question = results.get("question").asText();
                    final String correct_answer = results.get("correct_answer").asText();
                    final JsonNode incorrect_answers = results.get("incorrect_answers");

                    for (JsonNode j : incorrect_answers) {
                        options.add(j.asText());
                    }
                    options.add(correct_answer);

                    Collections.shuffle(options);

                    StringBuilder optionText = new StringBuilder();
                    AtomicInteger n = new AtomicInteger();
                    options.forEach(it -> {
                        n.set(n.incrementAndGet());
                        optionText.append(Utils.getEmojiFor(String.valueOf(n))).append(" ").append(it).append("\n");
                    });

                    final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                            .setTitle(question)
                            .setAuthor(String.format(
                                    "Category - %s\nDifficulty - %s",
                                    category, difficulty
                            )).setDescription(optionText);

                    channel.sendMessage(embed.build()).queue(
                            message -> {
                                for (String i : "1234".split("")) {
                                    message.addReaction(Utils.getEmojiFor(i)).queue();
                                }
                                waiter.waitForEvent(
                                        GuildMessageReactionAddEvent.class,
                                        e -> e.getMessageId().equals(message.getId()) && e.getMember().getUser().equals(ctx.getAuthor()),
                                        e -> {
                                            int answer = 0;
                                            for (String i : "1234".split("")) {
                                                if (e.getReaction().getReactionEmote().getAsReactionCode().equals(Utils.getEmojiFor(i))) {
                                                    answer = Integer.parseInt(i);
                                                }
                                            }

                                            message.clearReactions().queue();

                                            if (options.get(answer - 1).equals(correct_answer)) {
                                                e.getChannel().sendMessage("You got the answer correct !! 🎉").queue();
                                            } else {
                                                e.getChannel().sendMessage("You got it wrong 😔").queue();
                                            }
                                            e.getChannel().sendMessage("Correct Answer : " + correct_answer).queue();

                                        }, 30, TimeUnit.SECONDS, () -> {
                                            channel.sendMessage("You took too long to answer").queue();
                                            message.clearReactions().queue();
                                        }
                                );
                            }
                    );

                }
        );
    }

    @Override
    public String getName() {
        return "trivia";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sends a trivia question (answer it in 30 seconds)";
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
