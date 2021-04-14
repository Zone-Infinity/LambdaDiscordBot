package bot.java.lambda.config;

import bot.java.lambda.database.WelcomeSetting;

import java.util.HashMap;
import java.util.Map;

public class GuildSettings {

    public static final Map<Long, WelcomeSetting> WELCOME_SETTINGS = new HashMap<>();
    public static final Map<Long, String> PREFIXES = new HashMap<>();

}
