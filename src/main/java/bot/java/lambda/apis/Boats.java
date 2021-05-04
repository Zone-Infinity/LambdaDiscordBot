package bot.java.lambda.apis;

import bot.java.lambda.config.Config;

public class Boats implements ServerCountSite {
    @Override
    public String getName() {
        return "Boats";
    }

    @Override
    public String getPostUrl() {
        return "https://discord.boats/api/bot/:id";
    }

    @Override
    public String getAuthorization() {
        return Config.get("Boat_Token");
    }

    @Override
    public String getServerCountName() {
        return "server_count";
    }
}
