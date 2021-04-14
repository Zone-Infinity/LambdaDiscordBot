package bot.java.lambda.database;

public class WelcomeSetting {
    private String welcomeChannelId;
    private String welcomeMessage;
    private String welcomeBackgroundPath;

    WelcomeSetting(String welcomeChannelId, String welcomeMessage, String welcomeBackgroundPath) {
        this.welcomeChannelId = welcomeChannelId;
        this.welcomeMessage = welcomeMessage;
        this.welcomeBackgroundPath = welcomeBackgroundPath;
    }

    public String getWelcomeChannelId() {
        return welcomeChannelId;
    }

    public WelcomeSetting setWelcomeChannelId(String welcomeChannelId) {
        this.welcomeChannelId = welcomeChannelId;
        return this;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public WelcomeSetting setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
        return this;
    }

    public String getWelcomeBackgroundPath() {
        return welcomeBackgroundPath;
    }

    public WelcomeSetting setWelcomeBackgroundPath(String welcomeBackgroundPath) {
        this.welcomeBackgroundPath = welcomeBackgroundPath;
        return this;
    }
}
