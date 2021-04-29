package bot.java.lambda.config;

import bot.java.lambda.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Profanity {
    private static final String PROFANITY_URL = "https://raw.githubusercontent.com/RobertJGabriel/Google-profanity-words/master/list.txt";

    public static void loadProfanityList() {
        try {
            final URL url = new URL(PROFANITY_URL);
            final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            in.lines().forEach(Utils.profanityWords::add);
            in.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
