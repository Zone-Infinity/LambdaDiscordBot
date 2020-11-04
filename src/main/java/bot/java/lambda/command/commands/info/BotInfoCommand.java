package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.CommandManager;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
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
                .setDescription("")
                .addField("General 👓", "```css\n" +
                        "Owner : [Zone Infinity#7763]\n" +
                        "Library : [JDA]\n" +
                        "Prefix : [>]\n" +
                        "Command Number : [" + (manager.getCommands().stream().filter(it -> it.getHelpCategory() != HelpCategory.OWNER).count()) + "]\n```", false)
                .addField("Client 😎", "```css\n" +
                        "Client ID : [" + selfUser.getId() + "]\n" +
                        "Servers : [" + jda.getGuilds().size() + "]\n" +
                        "Users : [" + jda.getUsers().size() + "]\n" +
                        "WS Ping : [" + jda.getGatewayPing() + "ms]\n" +
                        "Uptime : [" + Utils.getUptime().split(";")[0] + "]\n```", false)
                .addField("Credits 🌍", "```css\n" +
                        "Host Provider : [4NG3L\uD83C\uDF41#2769]\n" +
                        "Helpers : [ duncte123(MenuDocs)  \n" +
                        "Loading BG#7962(AlphaBet)  \n" +
                        "R1zeN(MenuDocs)  \n" +
                        "Dioxin(AlphaBet) ]\n" +
                        "  and many more :D\n" +
                        "{Support : 'You can also be a helper by joining the support server which is a fun and music server too'}" +
                        "\n```", false);

        channel.sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "botinfo";
    }

    @Override
    public String getHelp() {
        return "Sends the bot info";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }
}
