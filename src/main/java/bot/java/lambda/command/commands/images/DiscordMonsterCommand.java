package bot.java.lambda.command.commands.images;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class DiscordMonsterCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        WebUtils.ins.getJSONObject("https://apis.duncte123.me/animal/discord-monster").async(
                json -> {
                    if(!json.get("success").asBoolean()){
                        channel.sendMessage("Something went wrong").queue();
                        return;
                    }
                    final JsonNode data = json.get("data");
                    final String file = data.get("file").asText();
                    channel.sendMessage(EmbedUtils.getDefaultEmbed().setImage(file).build()).queue();
                }
        );
    }

    @Override
    public String getName() {
        return "discordMonster";
    }

    @Override
    public String getHelp() {
        return "Sends a spooky monster ";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.IMAGES;
    }

    @Override
    public List<String> getAliases() {
        return List.of("monster","discordMonsters","monsters");
    }
}
