package bot.java.lambda.apis;

import bot.java.lambda.config.Config;
import me.duncte123.botcommons.web.WebUtils;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Boats implements ServerCountSite {
    @Override
    public Request getRequest(long serverCount) {
        final String postUrl = getPostUrl().replace(":id", "752052866809593906");

        RequestBody body = new FormBody.Builder()
                .add("server_count", serverCount + "")
                .build();

        return WebUtils.defaultRequest()
                .post(body)
                .addHeader("Authorization", Config.get("Boat_Token"))
                .url(postUrl)
                .build();
    }

    @Override
    public String getName() {
        return "Boats";
    }

    @Override
    public String getPostUrl() {
        return "https://discord.boats/api/bot/:id";
    }
}
