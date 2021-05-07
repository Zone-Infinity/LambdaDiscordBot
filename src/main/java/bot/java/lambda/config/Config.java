package bot.java.lambda.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public class Config {

    private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key) {
        return dotenv.get(key.toUpperCase());
        // return System.getenv(key.toUpperCase());
    }

    public static final List<Long> OWNER_IDS = List.of(
            757050742379905056L,
            754696003361177620L,
            616969228972458008L,
            731719097938870354L,
            Long.parseLong(Config.get("owner_id"))
    );

}
