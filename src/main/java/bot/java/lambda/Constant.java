package bot.java.lambda;

public class Constant {

    public enum Emote {
        PING_PONG("<:PingPong:755716114834522135>"),
        NEXT_TRACK("<:NextTrack:755716597842182164>"),
        MUSIC("<:Music:755716546827124787>"),
        LAMBDA_WHITE("<:LambdaWhite:755717368386289721>"),
        LAMBDA_VERIFIED("<:LambdaVerified:755715351798480897>"),
        LAMBDA_SUCCESS("<:LambdaTickYes:755716208191602738>"),
        LAMBDA_FAILURE("<:LambdaTickNo:755716160472875079>"),
        LAMBDA_SHIELD("<:LambdaShield:755717454696677418>"),
        LAMBDA_PING("<:LambdaPing:780988909433389066>"),
        LAMBDA_LOCK("<:LambdaLock:755716500643512370>"),
        LAMBDA_INFO("<:LambdaInfo:755717519410724884>"),
        LAMBDA_ERROR("<:LambdaError:755717248311754793>"),
        LAMBDA_DEVELOPMENT("<:LambdaDevelopment:795869472480428063>"),
        LAMBDA_BLACK("<:LambdaBlack:755717304989384714>"),
        LAMBDA("<:Lambda:755712363256348722>"),
        VIDEO_GAME("<:VideoGame:755715386980171796>");

        public final String emote;
        public final String name;
        public final String id;
        public final String asReaction;

        Emote(String emote) {
            this.emote = emote;
            this.name = emote.replaceAll("<:(\\w+):\\d+>", "$1");
            this.id = emote.replaceAll("<:\\w+:(\\d+)>", "$1");
            this.asReaction = emote.replaceAll("^<>$", "");
        }
    }
}
