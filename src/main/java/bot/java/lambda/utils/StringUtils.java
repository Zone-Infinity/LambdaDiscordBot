package bot.java.lambda.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StringUtils {
    private static final String NORMAL_TEXT = "abcdefghijklmnopqrstuvwxyz_,.?!/\\ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789&<> ɐqɔpǝɟɓɥᴉɾʞlɯuodbɹsʇnʌʍxʎz‾'˙¿¡/\\ⱯᗺƆᗡƎℲ⅁HIᒋꓘ⅂WNOԀტᴚS⊥∩ΛMX⅄Z0⇂ᘔƐㄣ59ㄥ86⅋><";
    private static final String FLIPPED_TEXT = "ɐqɔpǝɟɓɥᴉɾʞlɯuodbɹsʇnʌʍxʎz‾'˙¿¡/\\ⱯᗺƆᗡƎℲ⅁HIᒋꓘ⅂WNOԀტᴚS⊥∩ΛMX⅄Z0⇂ᘔƐㄣ59ㄥ86⅋>< abcdefghijklmnopqrstuvwxyz_,.?!/\\ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789&<>";
    private static final Map<Character, Character> LETTER_PAIRS = IntStream.range(0, NORMAL_TEXT.length())
        .boxed()
        .collect(Collectors.toMap(NORMAL_TEXT::charAt, FLIPPED_TEXT::charAt));

    private static final String PROFANITY_URL = "https://raw.githubusercontent.com/RobertJGabriel/Google-profanity-words/master/list.txt";
    private static final List<String> profanityWords = new ArrayList<>();
    static {
        try {
            final URL url = new URL(PROFANITY_URL);
            final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            in.lines().forEach(profanityWords::add);
            in.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static String flipText(String text) {
        return text.chars()
            .mapToObj(p -> {
                final char c = (char) p;
                return LETTER_PAIRS.getOrDefault(c, c);
            })
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .reverse()
            .toString();
    }

    public static boolean hasProfanity(String text) {
        return profanityWords.stream().anyMatch(text::contains);
    }
}
