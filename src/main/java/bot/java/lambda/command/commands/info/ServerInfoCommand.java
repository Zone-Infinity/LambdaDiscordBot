package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.time.format.DateTimeFormatter;

public class ServerInfoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        Guild guild = ctx.getGuild();

        String generalInfo = String.format(
                "> **Owner** : <@!%s>\n> **Region** : %s\n> **Creation Date** : %s\n> **Verification Level** : %s",
                guild.getOwnerId(),
                guild.getRegion().getName(),
                guild.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME),
                convertVerificationLevel(guild.getVerificationLevel())
        );

        String numberInfo = String.format(
            "> **Total Roles** : %s\n> **Total Members** : %s\n> **Categories** : %s\n> **Text Channels** : %s\n> **Voice Channels** : %s",
            guild.getRoleCache().size(),
            guild.getMemberCount(),
            guild.getCategories().size(),
            guild.getTextChannels().size(),
            guild.getVoiceChannels().size()
        );

        EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setTitle("Server info for "+guild.getName())
                .setThumbnail(guild.getIconUrl())
                .addField("General Info",generalInfo,false)
                .addField("Counts Info",numberInfo,false);

        ctx.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "serverinfo";
    }

    @Override
    public String getHelp() {
        return "Shows information about the server";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }

    private String convertVerificationLevel(Guild.VerificationLevel lvl){
        String[] names = lvl.name().toLowerCase().split("_");
        StringBuilder out = new StringBuilder();

        for(String name : names){
            out.append(Character.toUpperCase(name.charAt(0))).append(name.substring(1)).append(" ");
        }

        return out.toString().trim();
    }
}
