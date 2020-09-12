package bot.java.lambda.command;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Utils {
    public String getAuthorRequested(GuildMessageReceivedEvent event){
        final String asTag = event.getAuthor().getAsTag();
        return "Requested by "+asTag.substring(0,asTag.length()-5)+"λ"+ asTag.substring(asTag.length()-4);
    }
}
