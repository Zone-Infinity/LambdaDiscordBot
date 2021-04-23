package bot.java.lambda.apis;

import bot.java.lambda.config.Config;

public class TopGG implements ServerCountSite {
    @Override
    public String getName() {
        return "TopGG";
    }

    @Override
    public String getPostUrl() {
        return "https://top.gg/api//bots/752052866809593906/stats";
    }

    @Override
    public String getAuthorization() {
        return Config.get("TopGG_Token");
    }

    @Override
    public String getServerCountName() {
        return "server_count";
    }
}
