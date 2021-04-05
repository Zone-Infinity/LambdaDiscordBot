package bot.java.lambda.config;

import bot.java.lambda.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Profanity {
    public static void loadProfanityList() {
        try {
            String profanityUrl = "https://raw.githubusercontent.com/RobertJGabriel/Google-profanity-words/master/list.txt";

            URL url = new URL(profanityUrl);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream()))) {

                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    Utils.profanityWords.add(inputLine);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
