package bot.java.lambda.apis;

import bot.java.lambda.config.Config;

public class InfinityBots implements ServerCountSite {
    @Override
    public String getName() {
        return "InfinityBotList";
    }

    @Override
    public String getPostUrl() {
        return "https://api.infinitybotlist.com/bot/752052866809593906";
    }

    @Override
    public String getAuthorization() {
        return Config.get("InfinityBotList_Token");
    }

    @Override
    public String getServerCountName() {
        return "servers";
    }
}

