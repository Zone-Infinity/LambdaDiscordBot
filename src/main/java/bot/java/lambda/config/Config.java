package bot.java.lambda.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public class Config {

    private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key) {
        return dotenv.get(key.toUpperCase());

        // return System.getenv(key.toUpperCase());
    }

    public static final List<String> OWNER_IDS = List.of(Config.get("owner_id"), "754696003361177620", "757050742379905056", "616969228972458008");

}
