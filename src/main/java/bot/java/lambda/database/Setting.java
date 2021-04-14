package bot.java.lambda.database;

import bot.java.lambda.config.Config;

public enum Setting {
    PREFIX("prefix", Config.get("prefix")),
    WELCOME_CHANNEL_ID("welcome_channel_id", "-1"),
    WELCOME_MESSAGE("welcome_message", Config.get("welcome_message")),
    WELCOME_BACKGROUND("welcome_background", "default");

    private final String name;
    private final String defaultValue;

    Setting(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
