package bot.java.lambda.apis;

import bot.java.lambda.config.Config;
import me.duncte123.botcommons.web.WebUtils;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinityBots {
    private static final Logger LOGGER = LoggerFactory.getLogger(InfinityBots.class);

    public static void postServerCount(long serverCount) {
        String postUrl = "https://api.infinitybotlist.com/bot/752052866809593906";

        RequestBody body = new FormBody.Builder()
                .add("servers", serverCount + "")
                .build();

        Request request = WebUtils.defaultRequest()
                .post(body)
                .addHeader("authorization", Config.get("InfinityBotList_Token"))
                .url(postUrl)
                .build();

        WebUtils.ins.prepareRaw(request, r -> r).async(
                response -> {
                    if (response.isSuccessful()) {
                        LOGGER.info("Posted Server Count to infinitybotlist.com");
                    } else if (response.code() == 429) {
                        LOGGER.info("Rate Limit on infinitybotlist.com : {}", response.message());
                    } else {
                        LOGGER.info("Couldn't Post Server Count on infinitybotlist.com : {}", response.message());
                    }
                }
        );
    }
}

