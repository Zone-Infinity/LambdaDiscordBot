package bot.java.lambda.command.commands.settings;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.ICommand;
import bot.java.lambda.config.GuildSettings;
import bot.java.lambda.database.DatabaseManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class SetPrefixCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        final Member member = ctx.getMember();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            channel.sendMessage("You don't have MANAGE SERVER Permission").queue();
            return;
        }

        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        final String newPrefix = String.join("", args);
        updatePrefix(ctx.getGuild().getIdLong(), newPrefix);
        channel.sendMessage("New prefix set to `" + newPrefix + "`").queue();

    }

    private void updatePrefix(long guildId, String newPrefix) {
        GuildSettings.PREFIXES.put(guildId, newPrefix);

        DatabaseManager.INSTANCE.setPrefix(guildId, newPrefix);
    }

    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sets the prefix for this server\n" +
                "Usage: " + prefix + "setprefix <prefix>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.SETTINGS;
    }

    @Override
    public int getCoolDown() {
        return 60;
    }

    @Override
    public List<String> getAliases() {
        return List.of("prefix");
    }
}
