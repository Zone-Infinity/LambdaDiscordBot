package bot.java.lambda.command.commands.utils;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.menudocs.paste.PasteClient;
import org.menudocs.paste.PasteClientBuilder;
import org.menudocs.paste.PasteHost;

import java.util.List;

public class PasteCommand implements ICommand {
    private final PasteClient client = new PasteClientBuilder()
            .setUserAgent("Example paste client")
            .setDefaultExpiry("10m")
            .setPasteHost(PasteHost.MENUDOCS)
            .build();

    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();

        if (args.size() < 2) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        final String language = args.get(0);
        final String contentRaw = ctx.getMessage().getContentRaw();
        final int index = contentRaw.indexOf(language) + language.length();
        final String body = contentRaw.substring(index).trim();

        client.createPaste(language, body).async(
                (id) -> client.getPaste(id).async((paste) -> {
                    EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                            .setTitle("Paste " + id, paste.getPasteUrl())
                            .setDescription("```")
                            .appendDescription(paste.getLanguage().getId())
                            .appendDescription("\n")
                            .appendDescription(paste.getBody())
                            .appendDescription("```");

                    channel.sendMessage(builder.build()).queue();
                })
        );

    }

    @Override
    public String getName() {
        return "paste";
    }

    @Override
    public String getHelp(String prefix) {
        return "Creates a paste on the menudocs paste\n" +
                "Usage : " + prefix + "paste <language> <text>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.UTIL;
    }

    @Override
    public int getCoolDown() {
        return 60;
    }
}
