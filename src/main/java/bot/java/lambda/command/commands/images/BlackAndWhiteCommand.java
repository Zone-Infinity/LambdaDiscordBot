package bot.java.lambda.command.commands.images;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.utils.Utils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class BlackAndWhiteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();

        if (args.isEmpty()) {
            channel.sendMessage("Please provide a URL").queue();
            return;
        }

        if (Utils.isNotUrl(args.get(0))) {
            final Message message = ctx.getMessage();
            if (message.getMentionedMembers().isEmpty()) {
                channel.sendMessage("Provide the correct image url").queue();
                return;
            }
            final String effectiveAvatarUrl = message.getMentionedMembers().get(0).getUser().getEffectiveAvatarUrl();
            try {
                b_wImage(channel, effectiveAvatarUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            b_wImage(channel, args.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void b_wImage(TextChannel channel, String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody req = new FormBody.Builder()
                .add("image", url)
                .build();

        Request request = new Request.Builder()
                .url("https://apis.duncte123.me/filters/b&w")
                .post(req)
                .build();

        Call call = client.newCall(request);
        try (Response res = call.execute()) {
            if (res.code() == 429) {
                channel.sendMessage("I've been  rate-limited.\nPlease try again later.").queue();
                return;
            }

            if (res.code() == 422) {
                channel.sendMessage("Image URL provided does not exists").queue();
                return;
            }

            assert res.body() != null;
            channel.sendMessage("Here's you black and white image")
                    .addFile(Objects.requireNonNull(res.body()).bytes(), "Aleph.png").queue();
        }
    }

    @Override
    public String getName() {
        return "b&w";
    }

    @Override
    public String getHelp() {
        return "Black and Whites your image";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.IMAGES;
    }
}
