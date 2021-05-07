package bot.java.lambda.apis;

import bot.java.lambda.config.Config;
import me.duncte123.botcommons.web.WebUtils;
import me.infinity.ibl.IBL;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class InfinityBots implements ServerCountSite {
    private final IBL ibl;

    public InfinityBots(IBL ibl) {
        this.ibl = ibl;
    }

    @Override
    public void postServerCount(long serverCount) {
        ibl.postStats(serverCount);
    }

    @Override
    public Request getRequest(long serverCount) {
        final String postUrl = getPostUrl().replace(":id", "752052866809593906");

        RequestBody body = new FormBody.Builder()
                .add("servers", serverCount + "")
                .add("shards", "1")
                .build();

        return WebUtils.defaultRequest()
                .post(body)
                .addHeader("authorization", Config.get("InfinityBotList_Token"))
                .url(postUrl)
                .build();
    }

    @Override
    public String getName() {
        return "InfinityBotList";
    }

    @Override
    public String getPostUrl() {
        return "https://api.infinitybotlist.com/bot/:id";
    }
}

