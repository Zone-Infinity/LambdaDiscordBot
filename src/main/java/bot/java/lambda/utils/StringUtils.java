package bot.java.lambda.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {
    private static final String normalText = "abcdefghijklmnopqrstuvwxyz_,.?!/\\ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789&<>";
    private static final String flippedText = "ɐqɔpǝɟɓɥᴉɾʞlɯuodbɹsʇnʌʍxʎz‾'˙¿¡/\\ⱯᗺƆᗡƎℲ⅁HIᒋꓘ⅂WNOԀტᴚS⊥∩ΛMX⅄Z0⇂ᘔƐㄣ5986⅋><";

    public static final List<String> profanityWords = new ArrayList<>();

    public static String flipText(String text) {
        final String flipped = text.chars()
                .mapToObj(it -> {
                    String c = String.valueOf((char) it);
                    return normalText.contains(c)
                            ? String.valueOf(flippedText.charAt(normalText.indexOf(c)))
                            : c;
                })
                .collect(Collectors.joining());
        return new StringBuilder(flipped).reverse().toString();
    }

    public static boolean hasProfanity(String text) {
        return profanityWords.stream().anyMatch(text::contains);
    }
}
