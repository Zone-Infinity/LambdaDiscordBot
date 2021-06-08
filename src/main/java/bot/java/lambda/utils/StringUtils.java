package bot.java.lambda.utils;

import net.dv8tion.jda.api.entities.Guild;

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
    private static final List<Character> NORMAL_TEXT = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ ɐqɔpǝɟɓɥᴉɾʞlɯuodbɹsʇnʌʍxʎz ⱯᗺƆᗡƎℲ⅁HIᒋꓘ⅂WNOԀტᴚS⊥∩ΛMX⅄Z _,.?!/\\‾'˙¿¡/\\ 0⇂ᘔƐㄣ59ㄥ86⅋>< 0123456789&<>".chars().mapToObj(e -> (char) e).collect(Collectors.toSet()).stream().toList();
    private static final List<Character> FLIPPED_TEXT = "ɐqɔpǝɟɓɥᴉɾʞlɯuodbɹsʇnʌʍxʎz ⱯᗺƆᗡƎℲ⅁HIᒋꓘ⅂WNOԀტᴚS⊥∩ΛMX⅄Z abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ ‾'˙¿¡/\\_,.?!/\\ 0123456789&<> 0⇂ᘔƐㄣ59ㄥ86⅋><".chars().mapToObj(e -> (char) e).collect(Collectors.toSet()).stream().toList();

    private static final Map<Character, Character> LETTER_PAIRS = IntStream.range(0, NORMAL_TEXT.size())
            .boxed()
            .collect(Collectors.toMap(NORMAL_TEXT::get, FLIPPED_TEXT::get));

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

    public static String getGuildTable(List<Guild> guildList, int page) {
        StringBuilder table = new StringBuilder();
        final int guildSize = guildList.stream()
                .mapToInt(it -> Math.min(it.getName().length(), 22))
                .max()
                .orElse(0);
        final int memberSize = guildList.stream()
                .mapToInt(it -> String.valueOf(it.getMemberCount()).length())
                .max()
                .orElse(0);

        String rowFormat = "║%-" +
                (Math.max(5, String.valueOf(guildList.size()).length()) + 1) +
                "s║%-" +
                (Math.max(guildSize, 5) + 1) +
                "s║%-" +
                (Math.max(memberSize, 7) + 1) +
                "s║%-18s║%n";
        final String divider = String.format(rowFormat, "", "", "", "").replaceAll(" ", "═");

        table.append(String.format(rowFormat, "", "", "", "").replaceFirst("║", "╔").replaceFirst("║", "╦").replaceFirst("║", "╦").replaceFirst("║", "╦").replaceFirst("║", "╗").replaceAll(" ", "═"));
        table.append(String.format(rowFormat, "Rank ", "Name", "Members", "ID"));
        table.append(divider);

        for (int i = 0; i < guildList.size(); i++) {
            final Guild guild = guildList.get(i);
            final String name = guild.getName();
            table.append(String.format(rowFormat, ((page - 1) * 10 + i + 1) + ".", name.substring(0, Math.min(22, name.length())), guild.getMemberCount(), guild.getId()));
        }

        table.append(String.format(rowFormat, "", "", "", "").replaceFirst("║", "╚").replaceFirst("║", "╩").replaceFirst("║", "╩").replaceFirst("║", "╩").replaceFirst("║", "╝").replaceAll(" ", "═"));

        return table.toString();
    }
}
