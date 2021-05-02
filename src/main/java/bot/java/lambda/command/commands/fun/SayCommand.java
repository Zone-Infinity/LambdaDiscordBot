package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.ICommand;
import bot.java.lambda.utils.Discord;
import bot.java.lambda.utils.Utils;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Webhook;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SayCommand implements ICommand {

    @SuppressWarnings("ignored")
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        final User author = ctx.getAuthor();
        final Message message = ctx.getMessage();

        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        if (!ctx.getSelfMember().hasPermission(Permission.MANAGE_WEBHOOKS) && !ctx.getSelfMember().hasPermission(channel, Permission.MANAGE_WEBHOOKS)) {
            channel.sendMessage("I don't have MANAGE WEBHOOK Permission").queue();
            return;
        }

        final List<User> mentionedUsers = message.getMentionedUsers();

        String string = Discord.replaceAllMention(message);

        final String regularContent = Discord.replaceAllEmojiString(string.replaceFirst(">say", ""), ctx);
        final String mentionedContent = Discord.replaceAllEmojiString(string.replaceFirst(">say", "").replaceFirst("<@![0-9]{18}>", ""), ctx);

        channel.retrieveWebhooks().queue(
                webhooks -> {
                    // https://discordapp.com/api/webhooks/id/token
                    if (webhooks.isEmpty()) {
                        channel.createWebhook("Lambda").queue(
                                webhook -> {
                                    if (mentionedUsers.size() > 0) {
                                        sendWebhookMessage(mentionedUsers.get(0), mentionedContent, webhook.getIdLong(), webhook.getToken());
                                        return;
                                    }
                                    sendWebhookMessage(author, regularContent, webhook.getIdLong(), webhook.getToken());
                                    webhook.delete().queueAfter(5, TimeUnit.SECONDS);
                                }
                        );
                        return;
                    }

                    final Webhook webhook = webhooks.get(0);

                    if (mentionedUsers.size() > 0) {
                        sendWebhookMessage(mentionedUsers.get(0), mentionedContent, webhook.getIdLong(), webhook.getToken());
                        return;
                    }
                    sendWebhookMessage(author, regularContent, webhook.getIdLong(), webhook.getToken());
                }
        );
    }

    private void sendWebhookMessage(User author, String content, long id, String token) {
        final WebhookClient client = WebhookClient.withId(id, token);

        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder()
                .setUsername(author.getName())
                .setAvatarUrl(author.getEffectiveAvatarUrl().replaceFirst("gif", "png") + "?size=512")
                .setContent(content);

        client.send(messageBuilder.build());
    }

    @Override
    public String getName() {
        return "say";
    }

    @Override
    public String getHelp(String prefix) {
        return "Says something for you\n" +
                "Usage : " + prefix + "say <msg>\n" +
                "        " + prefix + "say <@user> <msg>\n" +
                "Using Emojis : put emoji name between ::\n" +
                "Example : :Lambda:";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.FUN;
    }

    @Override
    public int getCoolDown() {
        return 30;
    }
}
