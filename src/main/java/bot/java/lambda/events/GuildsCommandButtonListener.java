package bot.java.lambda.events;

import bot.java.lambda.utils.Discord;
import bot.java.lambda.utils.StringUtils;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("ConstantConditions")
public class GuildsCommandButtonListener extends ListenerAdapter {
    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        final String[] id = event.getComponentId().split(":");
        String userId = id[0];
        String buttonId = id[1];

        if (!event.getMember().getId().equals(userId))
            return;

        final Message message = event.getMessage();

        switch (buttonId) {
            case "delete" -> {
                event.deferEdit().queue();
                message.delete().queue();
            }
            case "done" -> {
                final Button[] disabledButton = message.getButtons().stream().map(Button::asDisabled).toArray(Button[]::new);
                event.deferEdit().setActionRow(disabledButton).queue();
            }
            case "next" -> {
                event.deferEdit().queue();
                editMessage(message, true);
            }
            case "previous" -> {
                event.deferEdit().queue();
                editMessage(message, false);
            }
        }
    }

    private void editMessage(Message msg, boolean next) {
        int page = Integer.parseInt(msg.getEmbeds().get(0).getFooter().getText());
        page = next ? page + 1 : page - 1;

        final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setDescription("```" + StringUtils.getGuildTable(Discord.getGuildsList(msg.getJDA()).get(page - 1), page) + "```")
                .setFooter(String.valueOf(page));

        msg.editMessage(embed.build()).queue();
    }
}
