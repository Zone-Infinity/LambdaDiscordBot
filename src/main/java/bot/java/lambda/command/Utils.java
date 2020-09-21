package bot.java.lambda.command;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Utils {
    public String getAuthorRequested(GuildMessageReceivedEvent event){
        final String asTag = event.getAuthor().getAsTag();
        return "Requested by "+asTag.substring(0,asTag.length()-5)+"λ"+ asTag.substring(asTag.length()-4);
    }
    @SuppressWarnings("ConstantConditions")
    public Emote searchEmote(CommandContext ctx , String name){
        Guild guild = ctx.getJDA().getGuildById(755433534495391805L);
        final List<Emote> emotes = guild.getEmotes();
        for (Emote emote : emotes){
            if(emote.getName().equalsIgnoreCase(name)){
                return emote;
            }
        }
        return null;
    }
}
