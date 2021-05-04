package bot.java.lambda.command.commands.settings;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.SettingCommand;
import bot.java.lambda.config.GuildSettings;
import bot.java.lambda.database.DatabaseManager;
import bot.java.lambda.database.Setting;
import bot.java.lambda.database.WelcomeSetting;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

@CommandHandler
public class SetWelcomeChannel implements SettingCommand {
    @Override
    public void updateSetting(CommandContext ctx) {
        final Guild guild = ctx.getGuild();
        String channelId;


        final List<TextChannel> mentionedChannels = ctx.getMessage().getMentionedChannels();

        if (mentionedChannels.isEmpty()) {
            channelId = ctx.getArgs().get(0);
            if (guild.getTextChannelById(channelId) == null) {
                ctx.getChannel().sendMessage("No TextChannel exists with this ID - `" + channelId + "`").queue();
                return;
            }
        } else {
            channelId = mentionedChannels.get(0).getId();
        }

        updateSettingSilently(ctx, channelId);

        ctx.getChannel().sendMessage("New Welcome Channel set to : <#" + channelId + ">").queue();
    }

    @Override
    public void updateSettingSilently(CommandContext ctx, String setting) {
        final Guild guild = ctx.getGuild();
        final long guildId = guild.getIdLong();

        final WelcomeSetting welcomeSetting = GuildSettings.WELCOME_SETTINGS.computeIfAbsent(guildId, DatabaseManager.INSTANCE::getWelcomeSettings);
        GuildSettings.WELCOME_SETTINGS.put(guildId, welcomeSetting.withChannelId(setting));
        DatabaseManager.INSTANCE.setWelcomeChannelId(guildId, setting);
    }

    @Override
    public Setting getSetting() {
        return Setting.WELCOME_CHANNEL_ID;
    }

    @Override
    public String getName() {
        return "setwelcomechannel";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sets the welcome channel for this server\n" +
                "Usage: " + prefix + "setwelcomechannel <#channel/channelId>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.SETTINGS;
    }
}
