package bot.java.lambda.command.commandType;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.Images;
import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public interface ImageCommand extends ICommand {
    @Override
    default void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        // Works only with duncte api
        WebUtils.ins.getJSONObject(this.getImages().getUrl()).async(
                (json) -> {
                    if (!json.get("success").asBoolean()) {
                        channel.sendMessage("Something went wrong, try again later").queue();
                        System.out.println(json);
                        return;
                    }
                    final JsonNode data = json.get("data");
                    final String file = data.get("file").asText();

                    final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                            .setImage(file);

                    channel.sendMessage(embed.build()).queue();
                }
        );
    }

    Images getImages();
}
