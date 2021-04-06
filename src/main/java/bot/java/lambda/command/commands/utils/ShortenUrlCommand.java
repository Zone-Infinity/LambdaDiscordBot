package bot.java.lambda.command.commands.utils;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandType.ICommand;
import bot.java.lambda.utils.Utils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class ShortenUrlCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final String url = String.join("", ctx.getArgs());
        if (Utils.isNotUrl(url)) {
            channel.sendMessage("Provide a valid URL").queue();
            return;
        }

        String apiUrl = "https://tinyurl.com/api-create.php?url=";

        try {
            URL shortUrl = new URL(apiUrl + url);
            Scanner scanner = new Scanner(shortUrl.openStream());
            final String shortenedUrl = scanner.nextLine();
            channel.sendMessage("Here's your shortened url : " + (shortenedUrl)).queue(
                    msg -> msg.getEmbeds().clear()
            );
        } catch (IOException e) {
            channel.sendMessage("Something went wrong").queue();
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "shortenurl";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shortens the provided URL";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.UTIL;
    }

    @Override
    public int getCoolDown() {
        return 30;
    }

    @Override
    public List<String> getAliases() {
        return List.of("surl", "shorturl");
    }
}
