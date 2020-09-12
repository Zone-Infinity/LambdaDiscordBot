package bot.java.lambda.command;

import me.duncte123.botcommons.commands.ICommandContext;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.Random;

public class CommandContext implements ICommandContext {
    private final GuildMessageReceivedEvent event;
    private final List<String> args;

    public CommandContext(GuildMessageReceivedEvent event, List<String> args) {
        this.event = event;
        this.args = args;
        Utils u = new Utils();
        Random random = new Random();
        int r = random.nextInt(255),g = random.nextInt(255),b = random.nextInt(255);
        EmbedUtils.setEmbedBuilder(
                ()-> new EmbedBuilder()
                        .setThumbnail(event.getAuthor().getAvatarUrl())
                        .setAuthor(u.getAuthorRequested(event),"",event.getAuthor().getAvatarUrl())
                        .setColor(new Color(r, g, b))
                        .setFooter("Lambda Equations in java are amazing","https://media.discordapp.net/attachments/751297245068132472/753934986943528980/1tNXllYx93ipMLK44F6QWQw-removebg-preview.png")
                        .setTimestamp(Instant.now())
        );
    }

    @Override
    public Guild getGuild() {
        return this.getEvent().getGuild();
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

    public List<String> getArgs(){
        return this.args;
    }

}
