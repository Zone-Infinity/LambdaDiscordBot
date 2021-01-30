package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.CommandManager;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.events.Listener;
import bot.java.lambda.utils.Utils;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class BotInfoCommand implements ICommand {
    CommandManager manager;

    public BotInfoCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final JDA jda = ctx.getJDA();
        final User selfUser = ctx.getSelfUser();

        final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setTitle("🤖 Bot Info", jda.getInviteUrl(Permission.MANAGE_WEBHOOKS, Permission.MESSAGE_MANAGE, Permission.VOICE_SPEAK))
                .setAuthor("Zone Infinity λ7763", "https://images-ext-2.discordapp.net/external/A_7uQjeR6Y8ryVWMfwJT32Kkd_3oFYvVNoHBxTjI02A/https/cdn.discordapp.com/avatars/722854351600615465/883407867f1bf7dc0b7a7bf489b37c57.png", selfUser.getEffectiveAvatarUrl())
                .addField("General 👓", "```css\n" +
                        "Owner : [" + Utils.getZoneInfinityAsTag(jda) + "]\n" +
                        "Library : [JDA]\n" +
                        "Prefix : [" + Listener.getPrefix(ctx.getGuild().getId()) + "]\n" +
                        "Command Number : [" + (manager.getCommands().stream().filter(it -> it.getHelpCategory() != HelpCategory.OWNER).count()) + "]\n```", false)
                .addField("Client 😎", "```css\n" +
                        "Client ID : [" + selfUser.getId() + "]\n" +
                        "Servers : [" + jda.getGuilds().size() + "]\n" +
                        "Users : [" + jda.getUsers().size() + "]\n" +
                        "Average Users per Server : [" + (jda.getUsers().size() / jda.getGuilds().size()) + "]\n" +
                        "WS Ping : [" + jda.getGatewayPing() + "ms]\n" +
                        "Uptime : [" + Utils.getUptime().split(";")[0] + "]\n```", false)
                .addField("Links 🔗", "**[Github](https://github.com/Zone-Infinity/LambdaDiscordBot) • [TopGG](https://top.gg/bot/752052866809593906) • [Support Server](https://discord.com/invite/XCNehWVrH7)**", false);

        channel.sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "botinfo";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sends the bot info";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }
}
