package bot.java.lambda.events;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PrivateMessageListener extends ListenerAdapter {

    public static List<User> blockedUsers = new ArrayList<>();

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {

        if (event.getAuthor().isBot())
            return;

        if (blockedUsers.contains(event.getAuthor())) {
            event.getMessage().addReaction(":TickNo:755716160472875079").queue();
            return;
        }

        final String name = event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator();
        final String message = event.getMessage().getContentRaw();
        final String avatarUrl = event.getAuthor().getEffectiveAvatarUrl();

        final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setAuthor(null, null, null)
                .setTitle(name)
                .setThumbnail(avatarUrl)
                .setDescription(message)
                .setFooter(event.getAuthor().getId());

        final TextChannel channel = event.getJDA().getTextChannelById("766902384584032287");

        if (channel == null)
            return;

        channel.sendMessage(embed.build()).queue();
        event.getMessage().addReaction(":TickYes:755716208191602738").queue();
    }
}
