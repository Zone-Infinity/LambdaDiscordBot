package bot.java.lambda.events.audits;

import bot.java.lambda.utils.AuditUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateAvatarEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Objects;

public class UserEventListener extends ListenerAdapter {
    private static TextChannel auditChannel;
    private static Guild guild;
    private static final EmbedBuilder embed = new EmbedBuilder();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        auditChannel = event.getJDA().getTextChannelById(AuditUtils.auditChannelId);
        guild = event.getJDA().getGuildById(AuditUtils.lambdaGuildId);
    }

    @Override
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) {
        final User user = event.getUser();
        guild.getMembers().forEach(it -> {
            if (user.equals(it.getUser())) {
                String description = String.format(
                        "Old Name : %s\nNew Name : %s", event.getOldName(), event.getNewName()
                );
                embed.setTitle("User Update Name Event")
                        .setAuthor(user.getName() + "#" + user.getDiscriminator())
                        .setThumbnail(user.getEffectiveAvatarUrl())
                        .setDescription(description)
                        .setTimestamp(Instant.now());
                auditChannel.sendMessage(embed.build()).queue();
            }
        });
        embed.clear();
    }

    @Override
    public void onUserUpdateAvatar(@NotNull UserUpdateAvatarEvent event) {
        final User user = event.getUser();
        guild.getMembers().forEach(it -> {
            if (user.equals(it.getUser())) {
                String description = "Old Avatar : Thumbnail\nNew Avatar : Image";
                embed.setTitle("User Update Avatar Event")
                        .setAuthor(user.getName() + "#" + user.getDiscriminator())
                        .setThumbnail(event.getOldAvatarUrl())
                        .setImage(event.getNewAvatarUrl())
                        .setDescription(description)
                        .setTimestamp(Instant.now());
                auditChannel.sendMessage(embed.build()).queue();
            }
        });
        embed.clear();
    }

    @Override
    public void onUserActivityStart(@NotNull UserActivityStartEvent event) {
        final User user = event.getUser();
        final Activity activity = event.getNewActivity();
        guild.getMembers().forEach(it -> {
            if (user.equals(it.getUser())) {
                String description = String.format(
                        "%s%s Started\nType : %s\bis Rich : %b",
                        Objects.requireNonNull(activity.getEmoji()).getAsMention(),
                        activity.getName(),
                        activity.getType(),
                        activity.isRich()
                );
                embed.setTitle("User Activity Start Event")
                        .setAuthor(user.getName() + "#" + user.getDiscriminator())
                        .setThumbnail(user.getEffectiveAvatarUrl())
                        .setDescription(description)
                        .setTimestamp(Instant.now());
                auditChannel.sendMessage(embed.build()).queue();
            }
        });
        embed.clear();
    }

    @Override
    public void onUserActivityEnd(@NotNull UserActivityEndEvent event) {
        final User user = event.getUser();
        final Activity activity = event.getOldActivity();
        guild.getMembers().forEach(it -> {
            if (user.equals(it.getUser())) {
                String description = String.format(
                        "%s%s Ended\nType : %s\bis Rich : %b",
                        Objects.requireNonNull(activity.getEmoji()).getAsMention(),
                        activity.getName(),
                        activity.getType(),
                        activity.isRich()
                );
                embed.setTitle("User Activity Start Event")
                        .setAuthor(user.getName() + "#" + user.getDiscriminator())
                        .setThumbnail(user.getEffectiveAvatarUrl())
                        .setDescription(description)
                        .setTimestamp(Instant.now());
                auditChannel.sendMessage(embed.build()).queue();
            }
        });
        embed.clear();
    }

}
