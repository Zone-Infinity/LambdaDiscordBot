package bot.java.lambda.command.commands.admin;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.ICommand;
import bot.java.lambda.config.Config;
import me.duncte123.botcommons.BotCommons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseCommand implements ICommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloseCommand.class);

    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getAuthor().getId().equals(Config.get("owner_id"))) {
            ctx.getChannel().sendMessage("Shutting Down").queue();
            ctx.getMessage().addReaction("✅").queue();
            ctx.getMessage().removeReaction("✅", ctx.getSelfUser()).queue();
            LOGGER.info("Shutting Down");

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
