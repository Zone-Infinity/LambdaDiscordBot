package bot.java.lambda.apis;

import me.infinity.ibl.IBL;
import okhttp3.Request;

public record InfinityBots(IBL ibl) implements ServerCountSite {

    @Override
    public void postServerCount(long serverCount) {
        ibl.postStats(serverCount);
    }

    @Override
    public Request getRequest(long serverCount) {
        /*
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
                */
        return null;
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

