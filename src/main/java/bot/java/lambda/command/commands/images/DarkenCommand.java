package bot.java.lambda.command.commands.images;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.command.Utils;
import net.dv8tion.jda.api.entities.TextChannel;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class DarkenCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();

        if(args.isEmpty()){
            channel.sendMessage("Please provide a URL").queue();
            return;
        }

        if (Utils.isNotUrl(args.get(0))) {
            channel.sendMessage("Provide the correct image url").queue();
            return;
        }

        try {
            darkenImage(channel, args.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void darkenImage(TextChannel channel, String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody req = new FormBody.Builder()
                .add("image", url)
                .build();

        Request request = new Request.Builder()
                .url("https://apis.duncte123.me/filters/darken")
                .post(req)
                .build();

        Call call = client.newCall(request);
        try ( Response res = call.execute()) {
            if (res.code() == 429) {
                channel.sendMessage("I've been  rate-limited.\nPlease try again later.").queue();
                return;
            }

            if (res.code() == 422){
                channel.sendMessage("Image URL provided does not exists").queue();
                return;
            }

            assert res.body() != null;
            channel.sendMessage("Here's you Darkened image")
                    .addFile(Objects.requireNonNull(res.body()).bytes(), "Aleph.png").queue();
        }
    }

    @Override
    public String getName() {
        return "darken";
    }

    @Override
    public String getHelp() {
        return "Darkens the supplied Image";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.IMAGES;
    }
}
