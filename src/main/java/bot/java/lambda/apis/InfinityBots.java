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

