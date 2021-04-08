package bot.java.lambda.command.category;

import bot.java.lambda.utils.UrlUtils;

public enum Images {
    ALPACA(UrlUtils.baseDuncteUrl + "/animal/alpaca"),
    BIRD(UrlUtils.baseDuncteUrl + "/animal/bird"),
    CAMEL(UrlUtils.baseDuncteUrl + "/animal/camel"),
    // CAT("https://www.nekos.life/api/v2/img/meow"),
    // COFFEE("https://coffee.alexflipnote.dev/random.json"),
    DOG(UrlUtils.baseDuncteUrl + "/animal/dog"),
    DUCK(UrlUtils.baseDuncteUrl + "/animal/duck"),
    FOX(UrlUtils.baseDuncteUrl + "/animal/fox"),
    LIZARD(UrlUtils.baseDuncteUrl + "/animal/lizard"),
    LLAMA(UrlUtils.baseDuncteUrl + "/animal/llama"),
    PANDA(UrlUtils.baseDuncteUrl + "/animal/panda"),
    SEAL(UrlUtils.baseDuncteUrl + "/animal/seal"),
    WOLF(UrlUtils.baseDuncteUrl + "/animal/wolf");

    private final String url;

    Images(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
