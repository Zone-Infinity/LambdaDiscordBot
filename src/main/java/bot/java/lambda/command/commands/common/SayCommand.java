package bot.java.lambda.command.commands.common;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.WebhookAction;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SayCommand implements ICommand {

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

        final String join = String.join(" ", args);

        if (join.contains("@everyone") || join.contains("@here") || join.contains("<@&")) {
            channel.sendMessage("Please don't tell me to ping some roles or everyone").queue();
            return;
        }

        message.delete().queue();
        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder();
        channel.retrieveWebhooks().queue(
                webhooks -> {
                    //https://discordapp.com/api/webhooks/id/token
                    if (webhooks.isEmpty()) {
                        WebhookAction webhook = channel.createWebhook("Lambda");
                        webhook.queue(
                                web -> {
                                    WebhookClientBuilder clientBuilder = new WebhookClientBuilder("https://discordapp.com/api/webhooks/" + web.getId() + "/" + web.getToken())
                                            .setThreadFactory(job -> {
                                                Thread thread = new Thread(job);
                                                thread.setName("Lambda-Webhook-Thread");
                                                thread.setDaemon(true);
                                                return thread;
                                            });
                                    WebhookClient client = clientBuilder.build();
                                    if (message.getMentionedMembers().size() > 0) {
                                        final User user = message.getMentionedMembers().get(0).getUser();
                                        messageBuilder.setUsername(user.getName())
                                                .setAvatarUrl(user.getEffectiveAvatarUrl().replaceFirst("gif", "png") + "?size=512")
                                                .setContent(String.join(" ", args.subList(1, args.size())));
                                        client.send(messageBuilder.build());
                                        return;
                                    }
                                    messageBuilder.setUsername(author.getName())
                                            .setAvatarUrl(author.getEffectiveAvatarUrl().replaceFirst("gif", "png") + "?size=512")
                                            .setContent(String.join(" ", args.subList(0, args.size())));
                                    client.send(messageBuilder.build());
                                    channel.deleteWebhookById(web.getId()).queueAfter(5, TimeUnit.SECONDS);
                                }
                        );
                        return;
                    }
                    WebhookClientBuilder clientBuilder = new WebhookClientBuilder("https://discordapp.com/api/webhooks/" + webhooks.get(0).getId() + "/" + webhooks.get(0).getToken())
                            .setThreadFactory(job -> {
                                Thread thread = new Thread(job);
                                thread.setName("Lambda-Webhook-Thread");
                                thread.setDaemon(true);
                                return thread;
                            });
                    WebhookClient client = clientBuilder.build();
                    if (message.getMentionedMembers().size() > 0) {
                        final User user = message.getMentionedMembers().get(0).getUser();
                        messageBuilder.setUsername(user.getName())
                                .setAvatarUrl(user.getEffectiveAvatarUrl().replaceFirst("gif", "png") + "?size=512")
                                .setContent(String.join(" ", args.subList(1, args.size())));
                        client.send(messageBuilder.build());
                        return;
                    }
                    messageBuilder.setUsername(author.getName())
                            .setAvatarUrl(author.getEffectiveAvatarUrl().replaceFirst("gif", "png") + "?size=512")
                            .setContent(String.join(" ", args.subList(0, args.size())));
                    client.send(messageBuilder.build());
                }
        );
    }

    @Override
    public String getName() {
        return "say";
    }

    @Override
    public String getHelp() {
        return "Says something for you\n" +
                "Usage : >say <sentence>\n" +
                "        >say <@user> <sentence>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }
}
