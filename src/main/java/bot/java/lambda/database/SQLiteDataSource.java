package bot.java.lambda.database;

import bot.java.lambda.config.Config;
import bot.java.lambda.utils.DatabaseUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SQLiteDataSource implements DatabaseManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLiteDataSource.class);

    private final HikariDataSource ds;

    public SQLiteDataSource() {
        try {
            final File dbFile = new File(DatabaseUtils.DatabaseDotDB);

            if (!dbFile.exists()) {
                if (dbFile.createNewFile()) {
                    LOGGER.info("Created database file");
                } else {
                    LOGGER.info("Could not create database file");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + DatabaseUtils.DatabaseDotDB);
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(20);

        ds = new HikariDataSource(config);

        try (Connection connection = getConnection();
             final Statement statement = connection.createStatement()) {
            final String defaultPrefix = Config.get("prefix");
            final String defaultWelcomeMessage = Config.get("welcome_message");

            // Make flip channel , music logs on
            statement.execute("CREATE TABLE IF NOT EXISTS guild_settings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "prefix VARCHAR(255) NOT NULL DEFAULT '" + defaultPrefix + "'," +
                    "welcome_channel_id VARCHAR(20) NOT NULL DEFAULT '-1'," +
                    "welcome_message VARCHAR(1000) NOT NULL DEFAULT '" + defaultWelcomeMessage + "'," +
                    "welcome_background VARCHAR(255) NOT NULL DEFAULT 'default'" +
                    ");");

            LOGGER.info("Table initialised");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPrefix(long guildId) {
        return getSetting(Setting.PREFIX, guildId);
    }

    @Override
    public void setPrefix(long guildId, String newPrefix) {
        setSetting(Setting.PREFIX, guildId, newPrefix);
    }

    @Override
    public String getWelcomeChannelId(long guildId) {
        return getSetting(Setting.WELCOME_CHANNEL_ID, guildId);
    }

    @Override
    public void setWelcomeChannelId(long guildId, String newWelcomeChannelId) {
        setSetting(Setting.WELCOME_CHANNEL_ID, guildId, newWelcomeChannelId);
    }

    @Override
    public String getWelcomeMessage(long guildId) {
        return getSetting(Setting.WELCOME_MESSAGE, guildId);
    }

    @Override
    public void setWelcomeMessage(long guildId, String newWelcomeMessage) {
        setSetting(Setting.WELCOME_MESSAGE, guildId, newWelcomeMessage);
    }

    @Override
    public String getWelcomeBackground(long guildId) {
        return getSetting(Setting.WELCOME_BACKGROUND, guildId);
    }

    @Override
    public void setWelcomeBackground(long guildId, String newWelcomeBackground) {
        setSetting(Setting.WELCOME_BACKGROUND, guildId, newWelcomeBackground);
    }

    @Override
    public WelcomeSetting getWelcomeSettings(long guildId) {
        try (
             final PreparedStatement preparedStatement = getConnection()
                     .prepareStatement("SELECT welcome_channel_id, welcome_message, welcome_background FROM guild_settings WHERE guild_id = ?")
        ) {
            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String welcomeChannelId = resultSet.getString("welcome_channel_id");
                    String welcomeMessage = resultSet.getString("welcome_message");
                    String welcomeBackground = resultSet.getString("welcome_background");

                    return new WelcomeSetting(welcomeChannelId, welcomeMessage, welcomeBackground);
                }
            }

            try (
                 final PreparedStatement insertStatement = getConnection()
                         .prepareStatement("INSERT INTO guild_settings(guild_id) VALUES(?)")
            ) {
                insertStatement.setString(1, String.valueOf(guildId));

                insertStatement.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new WelcomeSetting("-1", Config.get("welcome_message"), "default");
    }

    public String getSetting(Setting setting, long guildId) {
        final String settingName = setting.getName();

        try (
             final PreparedStatement preparedStatement = getConnection()
                     .prepareStatement("SELECT " + settingName + " FROM guild_settings WHERE guild_id = ?")
        ) {
            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    final String settingValue = resultSet.getString(settingName);
                    return settingValue;
                }
            }

            try (
                 final PreparedStatement insertStatement = getConnection()
                         .prepareStatement("INSERT INTO guild_settings(guild_id) VALUES(?)")
            ) {
                insertStatement.setString(1, String.valueOf(guildId));

                insertStatement.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return setting.getDefaultValue();
    }

    public void setSetting(Setting setting, long guildId, String newValue) {
        try (
             final PreparedStatement preparedStatement = getConnection()
                     .prepareStatement("UPDATE guild_settings SET " + setting.getName() + " = ? WHERE guild_id = ?")
        ) {
            preparedStatement.setString(1, newValue);
            preparedStatement.setString(2, String.valueOf(guildId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}

