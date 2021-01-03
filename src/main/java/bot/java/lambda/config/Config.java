package bot.java.lambda.config;

//import io.github.cdimascio.dotenv.Dotenv;

public class Config {

    //private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key) {
        //return dotenv.get(key.toUpperCase());

        return System.getenv(key.toUpperCase());
    }

}
