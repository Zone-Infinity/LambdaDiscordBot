package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

public class ServerRolesCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final Guild guild = ctx.getGuild();
        StringBuilder roles = new StringBuilder();
        for (Role role : guild.getRoles()) {
            roles.append(role.getAsMention()).append(" | ");
        }

        ctx.getChannel().sendMessage(
                EmbedUtils.getDefaultEmbed()
                        .setTitle("Roles of this server")
                        .setDescription(roles)
                        .build()
        ).queue();
    }

    @Override
    public String getName() {
        return "serverroles";
    }

    @Override
    public String getHelp() {
        return "Shows the roles of the server";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }
}
