package bot.java.lambda.events.audits;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDAEventListener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDAEventListener.class);
    public static String botStatusChannelID = "770225299396624394";
    private static TextChannel botStatusChannel = null;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        if (botStatusChannel == null) botStatusChannel = event.getJDA().getTextChannelById(botStatusChannelID);

        botStatusChannel.sendMessage("`Started`").queue();
        LOGGER.info("Started");
    }

    @Override
    public void onDisconnect(@NotNull DisconnectEvent event) {
        LOGGER.info("Disconnected");
    }

    @Override
    public void onReconnected(@NotNull ReconnectedEvent event) {
        if (botStatusChannel == null) botStatusChannel = event.getJDA().getTextChannelById(botStatusChannelID);

        botStatusChannel.sendMessage("`Reconnected`").queue();
        LOGGER.info("Reconnected");
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        LOGGER.info("Shutdown successful");
    }

    @Override
    public void onStatusChange(@NotNull StatusChangeEvent event) {
        if (botStatusChannel == null) botStatusChannel = event.getJDA().getTextChannelById(botStatusChannelID);

        botStatusChannel.sendMessageFormat("- Status changed from `%s` to `%s`"
                , event.getOldStatus().name(), event.getNewStatus().name()).queue();
        LOGGER.info("Status changed from {} to {}", event.getOldStatus().name(), event.getNewStatus().name());
    }
}
