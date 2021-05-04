package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;
import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

@CommandHandler
public class UrbanCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        WebUtils.ins.getJSONObject("http://api.urbandictionary.com/v0/define?term=" + String.join(" ", args)).async(
                (json) -> {
                    final JsonNode list = json.get("list");
                    if (list.isEmpty()) {
                        channel.sendMessage("Nothing found").queue();
                        return;
                    }
                    final String definition = list.get(0).get("definition").asText();
                    final String author = list.get(0).get("author").asText();
                    channel.sendMessage(EmbedUtils.getDefaultEmbed()
                            .setTitle(String.join(" ", args), list.get(0).get("permalink").asText())
                            .setDescription(definition)
                            .addField("Example", "```" + list.get(0).get("example").asText() + "```", false)
                            .setFooter("\uD83D\uDC4D\uD83C\uDFFB " + list.get(0).get("thumbs_up") + " | \uD83D\uDC4E\uD83C\uDFFB " + list.get(0).get("thumbs_down"))
                            .setAuthor(author)
                            .build()).queue();
                }
        );
    }

    @Override
    public String getName() {
        return "urban";
    }

    @Override
    public String getHelp(String prefix) {
        return "Will give you funny meanings of the word u gave\n" +
                "Usage : " + prefix + "urban <words>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }

    @Override
    public int getCoolDown() {
        return 15;
    }
}
