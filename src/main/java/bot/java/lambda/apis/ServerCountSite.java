package bot.java.lambda.apis;

import me.duncte123.botcommons.web.WebUtils;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ServerCountSite {
    default void postServerCount(long serverCount) {
        final String name = getName();
        Logger LOGGER = LoggerFactory.getLogger(name);

        final Request request = getRequest(serverCount);

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
                e -> LOGGER.error(e.getMessage(), e)
        );
    }

    Request getRequest(long serverCount);

    String getName();

    String getPostUrl();
}
