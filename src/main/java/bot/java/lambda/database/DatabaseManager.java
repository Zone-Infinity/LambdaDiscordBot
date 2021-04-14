package bot.java.lambda.database;

public interface DatabaseManager {
    DatabaseManager INSTANCE = new SQLiteDataSource();

    String getPrefix(long guildId);

    void setPrefix(long guildId, String newPrefix);

    String getWelcomeChannelId(long guildId);

    void setWelcomeChannelId(long guildId, String newWelcomeChannelId);

    String getWelcomeMessage(long guildId);

    void setWelcomeMessage(long guildId, String newWelcomeMessage);

    String getWelcomeBackground(long guildId);

    void setWelcomeBackground(long guildId, String newWelcomeBackground);

    WelcomeSetting getWelcomeSettings(long guildId);
}
