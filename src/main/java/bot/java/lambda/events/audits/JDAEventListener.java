package bot.java.lambda.events.audits;

import bot.java.lambda.utils.AuditUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Objects;

public class JDAEventListener extends ListenerAdapter {
    private static TextChannel botStatusChannel;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        botStatusChannel = event.getJDA().getTextChannelById(AuditUtils.botStatusChannelID);
        if (botStatusChannel == null) return;
        botStatusChannel.sendMessage("`Started`").queue();
        System.out.println("Started");
    }

    @Override
    public void onDisconnect(@NotNull DisconnectEvent event) {
        final OffsetDateTime timeDisconnected = event.getTimeDisconnected();
        System.out.println(timeDisconnected.toString() + " Disconnected");
    }

    @Override
    public void onReconnect(@NotNull ReconnectedEvent event) {
        botStatusChannel = event.getJDA().getTextChannelById(AuditUtils.botStatusChannelID);
        if (botStatusChannel == null) return;
        botStatusChannel.sendMessage("`Reconnected`").queue();
        System.out.println("Reconnected");
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        System.out.println("Shut Downed");
    }

    @Override
    public void onStatusChange(@NotNull StatusChangeEvent event) {
        botStatusChannel = event.getJDA().getTextChannelById(AuditUtils.botStatusChannelID);
        if (botStatusChannel == null) return;
        botStatusChannel.sendMessageFormat("- Status changed from `%s` to `%s`"
                , event.getOldStatus().name(), event.getNewStatus().name()).queue();
        System.out.printf("Status changed from %s to %s%n", event.getOldStatus().name(), event.getNewStatus().name());
    }

    @Override
    public void onException(@NotNull ExceptionEvent event) {
        final Throwable cause = event.getCause();
        final EmbedBuilder embedBuilder = new EmbedBuilder()
                .addField("Exception", cause.getClass().getName(), false)
                .addField("Message", cause.getMessage(), false)
                .addField("Stack Trace", cause.getStackTrace().length > 1000 ? "Big enough" : Arrays.toString(cause.getStackTrace()), false);
        cause.fillInStackTrace();
        Objects.requireNonNull(event.getJDA().getTextChannelById(AuditUtils.errorChannelId)).sendMessage(embedBuilder.build()).queue();
    }

}
