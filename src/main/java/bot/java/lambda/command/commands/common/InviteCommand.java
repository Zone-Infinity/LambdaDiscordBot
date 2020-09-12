package bot.java.lambda.command.commands.common;

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
        final String author = "Requested by "+asTag.substring(0,asTag.length()-5)+"λ"+ asTag.substring(asTag.length()-4);
        final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setAuthor(author)
                .setTitle("<:LockBlack:753960253061595186> Click here to invite me",ctx.getJDA().getInviteUrl())
                .setDescription("\uD83D\uDE0A Here you go , invite me to as many servers as possible\n" +
                        "And help me becoming a verified bot <:Verified:753964332001394748>")
                .setThumbnail(ctx.getAuthor().getAvatarUrl());
        channel.sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getHelp() {
        return "Gives u a invite link of the bot";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }
}
