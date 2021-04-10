package bot.java.lambda.utils;

import bot.java.lambda.config.Config;
import bot.java.lambda.database.SQLiteDataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtils {
    //public static String PrefixDotJson = DatabaseUtils.class.getResource("/database/prefix.json").getPath();
    public static String DatabaseDotDB = "database/database.db";

    public static String getPrefix(long guildID) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource
                .getConnection()
                // language=SQLITE-SQL
                .prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?")
        ) {
            preparedStatement.setString(1, String.valueOf(guildID));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("prefix");
                }
            }

            try (final PreparedStatement insertStatement = SQLiteDataSource
                    .getConnection()
                    // language=SQLITE-SQL
                    .prepareStatement("INSERT INTO guild_settings(guild_id) VALUES(?)")
            ) {
                insertStatement.setString(1, String.valueOf(guildID));

                insertStatement.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Config.get("prefix");
    }
}
