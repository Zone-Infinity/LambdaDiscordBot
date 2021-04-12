package bot.java.lambda.database;

public interface DatabaseManager {
    DatabaseManager INSTANCE = new SQLiteDataSource();

    String getPrefix(long guildId);

    void setPrefix(long guildId, String newPrefix);
}
