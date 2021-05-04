package bot.java.lambda.events;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDAEventListener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDAEventListener.class);
    public static String botStatusChannelID = "770225299396624394";

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        final TextChannel channel = event.getJDA().getTextChannelById(botStatusChannelID);
        if (channel == null) return;
        channel.sendMessage("`Started`").queue();

        LOGGER.info("Started");
    }

    @Override
    public void onReconnected(@NotNull ReconnectedEvent event) {
        final TextChannel channel = event.getJDA().getTextChannelById(botStatusChannelID);
        if (channel == null) return;
        channel.sendMessage("`Reconnected`").queue();

        LOGGER.info("Reconnected");
    }

    @Override
    public void onDisconnect(@NotNull DisconnectEvent event) {
        final TextChannel channel = event.getJDA().getTextChannelById(botStatusChannelID);
        if (channel == null) return;
        channel.sendMessage("`Disconnected`").queue();

        LOGGER.info("Disconnected");
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        final TextChannel channel = event.getJDA().getTextChannelById(botStatusChannelID);
        if (channel == null) return;
        channel.sendMessage("`Shutdown`").queue();
        LOGGER.info("Shutdown successful");
    }

    @Override
    public void onStatusChange(@NotNull StatusChangeEvent event) {
        LOGGER.info("Status changed from {} to {}", event.getOldStatus().name(), event.getNewStatus().name());
    }
}
