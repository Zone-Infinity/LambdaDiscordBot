package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class InviteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        final String asTag = ctx.getAuthor().getAsTag();
        final String author = "Requested by " + asTag.substring(0, asTag.length() - 5) + "λ" + asTag.substring(asTag.length() - 4);
        final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setAuthor(author)
                .setTitle("<:LambdaLock:755716500643512370> Click here to invite me", "https://discord.com/api/oauth2/authorize?client_id=752052866809593906&permissions=540375616&scope=bot")
                .setDescription("<:go:734650199472865310> **Join our [Support Server](https://discord.com/invite/XCNehWVrH7)**\n" +
                        "Here you go , invite me to as many servers as possible\n" +
                        "And help me becoming a verified bot <:Verified:755715351798480897>")
                .setThumbnail(ctx.getAuthor().getAvatarUrl())
                .setImage("https://discord.com/api/guilds/755433534495391805/widget.png?style=banner3");
        channel.sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives u a invite link of the bot";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }
}
