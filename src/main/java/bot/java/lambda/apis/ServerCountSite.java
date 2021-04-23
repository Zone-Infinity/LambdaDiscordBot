package bot.java.lambda.apis;

import me.duncte123.botcommons.web.WebUtils;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ServerCountSite {
    default void postServerCount(long serverCount) {
        final String name = getName();
        Logger LOGGER = LoggerFactory.getLogger(name);
        String postUrl = getPostUrl();

        RequestBody body = new FormBody.Builder()
                .add(getServerCountName(), serverCount + "")
                .build();

        Request request = WebUtils.defaultRequest()
                .post(body)
                .addHeader("Authorization", getAuthorization())
                .url(postUrl)
                .build();

        WebUtils.ins.prepareRaw(request, r -> r).async(
                response -> {
                    if (response.isSuccessful()) {
                        LOGGER.info("Posted Server Count to {}", name);
                    } else if (response.code() == 429) {
                        LOGGER.info("Rate Limit on {} : {}", name, response.message());
                    } else {
                        LOGGER.info("Couldn't Post Server Count on {} : {}", name, response.message());
                    }
                },
                e -> LOGGER.error("Something went wrong Posting Server Count on {}", name, e)
        );
    }

    String getName();

    String getPostUrl();

    String getAuthorization();

    String getServerCountName();
}
