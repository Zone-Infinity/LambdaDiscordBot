package bot.java.lambda.command.commands.admin;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.config.Config;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Objects;

public class CloseCommand implements ICommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloseCommand.class);

    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getAuthor().getId().equals(Config.get("owner_id"))) {
            ctx.getChannel().sendMessage("Shutting Down").queue();
            ctx.getMessage().addReaction("✅").queue();
            ctx.getMessage().removeReaction("✅", ctx.getSelfUser()).queue();
            final Guild eventGuild = ctx.getGuild();
            LOGGER.info("Shutting Down");

            if (eventGuild.getId().equals("755433534495391805")) {
                final VoiceChannel create_vc = eventGuild.getVoiceChannelsByName("Create VC", true).get(0);
                create_vc.getManager().putPermissionOverride(Objects.requireNonNull(eventGuild.getRoleById(755433534495391805L)), Collections.emptyList(), Collections.singletonList(Permission.VOICE_CONNECT)).queue();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.fillInStackTrace();
            }

            BotCommons.shutdown(ctx.getJDA());
            System.exit(0);
        }
    }

    @Override
    public String getName() {
        return "close";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shutdowns the bot";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.OWNER;
    }
}
