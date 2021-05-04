package bot.java.lambda.command.category;

public enum Images {
    ALPACA("/animal/alpaca"),
    BIRD("/animal/bird"),
    CAMEL("/animal/camel"),
    // CAT("https://www.nekos.life/api/v2/img/meow"),
    // COFFEE("https://coffee.alexflipnote.dev/random.json"),
    DOG("/animal/dog"),
    DUCK("/animal/duck"),
    FOX("/animal/fox"),
    LIZARD("/animal/lizard"),
    LLAMA("/animal/llama"),
    PANDA("/animal/panda"),
    SEAL("/animal/seal"),
    WOLF("/animal/wolf");

    private static final String baseDuncteUrl = "https://apis.duncte123.me";

    private final String url;

    Images(String url) {
        this.url = baseDuncteUrl + url;
    }

    public String getUrl() {
        return url;
    }
}
