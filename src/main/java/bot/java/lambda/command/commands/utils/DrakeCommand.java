package bot.java.lambda.command.commands.utils;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

@CommandHandler
public class DrakeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final String join = String.join(" ", ctx.getArgs());
        final String[] split = join.split("\\|");

        if (split.length < 2) {
            channel.sendMessage("Please provide the 2 arguments").queue();
            return;
        }

        try {
            generateMeme(channel, split[0], split[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateMeme(TextChannel channel, String top, String bottom) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody req = new FormBody.Builder()
                .add("top", top)
                .add("bottom", bottom)
                .build();

        Request request = new Request.Builder()
                .url("https://apis.duncte123.me/memes/drakememe/")
                .post(req)
                .build();

        Call call = client.newCall(request);
        try (Response res = call.execute()) {
            if (res.code() == 429) {
                channel.sendMessage("I've been  rate-limited.\nPlease try again later.").queue();
                return;
            }

            assert res.body() != null;
            channel.sendMessage("Generated Successfully").addFile(Objects.requireNonNull(res.body()).bytes(), "Aleph.png").queue();
        }
    }

    @Override
    public String getName() {
        return "drake";
    }

    @Override
    public String getHelp(String prefix) {
        return "Generates a drake meme\n" +
                "Usage : " + prefix + "drake <top_text> | <bottom_text>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.UTIL;
    }
}
