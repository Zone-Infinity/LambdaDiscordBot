package bot.java.lambda.command.commands.admin;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;
import bot.java.lambda.utils.Discord;
import bot.java.lambda.utils.StringUtils;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.List;

@CommandHandler
public class GuildsCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final String userId = ctx.getAuthor().getId();

        try {
            final List<String> args = ctx.getArgs();
            final int page = args.isEmpty() ? 1 : Integer.parseInt(args.get(0));
            final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                    .setDescription("```" + StringUtils.getGuildTable(Discord.getGuildsList(ctx.getJDA()).get(page - 1), page) + "```")
                    .setFooter(String.valueOf(page));

            ctx.getChannel().sendMessage(embed.build())
                    .setActionRow(
                            Button.primary(userId + ":previous", "Previous"),
                            Button.success(userId + ":done", "Done"),
                            Button.primary(userId + ":next", "Next"),
                            Button.danger(userId + ":delete", "Delete")
                    ).queue();
        } catch (NumberFormatException e) {
            ctx.getChannel().sendMessage("Provide a number").queue();
        } catch (IndexOutOfBoundsException e) {
            ctx.getChannel().sendMessage("No guilds on the page").queue();
        }
    }

    @Override
    public String getName() {
        return "guilds";
    }

    @Override
    public String getHelp(String prefix) {
        return "Send the guild it is in";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.OWNER;
    }
}
