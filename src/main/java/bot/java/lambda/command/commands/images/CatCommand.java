package bot.java.lambda.command.commands.images;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class CatCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        WebUtils.ins.getJSONObject("https://www.nekos.life/api/v2/img/meow").async(
                (json) -> {
                    final String url = json.get("url").asText();

                    final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                            .setImage(url);

                    channel.sendMessage(embed.build()).queue();
                }
        );
    }

    @Override
    public String getName() {
        return "cat";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives random image of cats UwU";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.IMAGES;
    }
}
