package bot.java.lambda.command.commands.common;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.config.Config;
import bot.java.lambda.utils.Utils;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.regex.Pattern;

public class PollCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();

        String[] split = message.getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Config.get("prefix")) + getName(), "")
                .split(";");

        if (split.length < 3) {
            channel.sendMessage("Need at least 3 arguments").queue();
            return;
        }

        if (split.length > 11) {
            channel.sendMessage("Can't poll question with more than 10 answers").queue();
            return;
        }

        String question = split[0];
        String[] options = new String[split.length - 1];
        int i = 0;
        for (int s = 1; s <= split.length - 1; s++) {
            options[i] = split[s];
            i++;
        }
        StringBuilder optionSB = new StringBuilder();
        int count = 1;
        for (String option : options) {
            optionSB.append(Utils.getEmojiFor(String.valueOf(count)))
                    .append(" ")
                    .append(option)
                    .append("\n");
            count++;
        }

        EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setAuthor("🔢 Poll")
                .setTitle("❓ " + question)
                .setDescription(optionSB);

        channel.sendMessage(embed.build()).queue(
                msg -> {
                    for (int a = 1; a <= options.length; a++) {
                        msg.addReaction(Utils.getEmojiFor(String.valueOf(a))).queue();
                    }
                }
        );

    }

    @Override
    public String getName() {
        return "poll";
    }

    @Override
    public String getHelp(String prefix) {
        return "Does a Poll\n" +
                "Usage : " + Config.get("prefix") + "poll <question> ; <option1> ; <option2> ; ....\n" +
                "Condition : There must be at least 2 options , Maximum number of Options are 9";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }
}
