package bot.java.lambda.command.commandCategory;

public enum ImageUtil {
    BLACK_AND_WHITE("b&w", "Black and White"),
    BLUR("blur", "Blurred "),
    DARKEN("darken", "Darkened"),
    INVERT("invert", "Inverted"),
    PIXELATE("pixelate", "Pixelated");

    private final String url;
    private final String done;

    ImageUtil(String url, String done) {
        this.url = url;
        this.done = done;
    }

    public String getUrl() {
        return url;
    }

    public String getDone() {
        return done;
    }
}
