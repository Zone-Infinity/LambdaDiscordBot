package bot.java.lambda.database;

public final record WelcomeSetting(String message, String channelId, String background) {
    public final WelcomeSetting withMessage(final String message) {
        return new WelcomeSetting(message, channelId, background);
    }

    public final WelcomeSetting withChannelId(final String channelId) {
        return new WelcomeSetting(message, channelId, background);
    }

    public final WelcomeSetting withBackground(final String background) {
        return new WelcomeSetting(message, channelId, background);
    }
}
