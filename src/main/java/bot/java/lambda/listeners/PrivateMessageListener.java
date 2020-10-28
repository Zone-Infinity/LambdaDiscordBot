package bot.java.lambda.listeners;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class PrivateMessageListener extends ListenerAdapter {
    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {

        if(event.getAuthor().isBot())
            return;

        final String name = event.getAuthor().getName()+"#"+event.getAuthor().getDiscriminator();
        final String message = event.getMessage().getContentRaw();
        final String avatarUrl = event.getAuthor().getEffectiveAvatarUrl();

        final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setTitle(name)
                .setThumbnail(avatarUrl)
                .setDescription(message)
                .setFooter(event.getAuthor().getId());

        final TextChannel channel = event.getJDA().getTextChannelById("766902384584032287");

        if(channel==null)
            return;

        channel.sendMessage(embed.build()).queue();
    }
}
