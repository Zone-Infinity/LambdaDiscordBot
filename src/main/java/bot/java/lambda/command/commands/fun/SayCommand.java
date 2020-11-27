package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.WebhookAction;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        if (!ctx.getSelfMember().hasPermission(Permission.MANAGE_WEBHOOKS) && !ctx.getSelfMember().hasPermission(channel, Permission.MANAGE_WEBHOOKS)) {
            channel.sendMessage("I don't have MANAGE WEBHOOK Permission").queue();
            return;
        }

        final List<Member> mentionedMembers = message.getMentionedMembers();

        final String regularContent = String.join(" ", args)
                .replaceAll("@everyone", "<:LambdaPing:780988909433389066>everyone")
                .replaceAll("@here", "<:LambdaPing:780988909433389066>here")
                .replaceAll("<@&[0-9]{18}>", "<:LambdaPing:780988909433389066>Role");

        final String mentionedContent = String.join(" ", args.subList(1, args.size()))
                .replaceAll("@everyone", "<:LambdaPing:780988909433389066>everyone")
                .replaceAll("@here", "<:LambdaPing:780988909433389066>here")
                .replaceAll("<@&[0-9]{18}>", "<:LambdaPing:780988909433389066>Role");

        final String s = args.get(0);
        boolean equals = false;

        Matcher matcher = Pattern.compile("<@![0-9]{18}>").matcher(String.join(" ", args));
        matcher.replaceFirst("");
        if (!mentionedMembers.isEmpty()) {
            equals = ("<@!" + mentionedMembers.get(0).getId() + ">").equals(s.substring(matcher.start(), matcher.end()));
        }

        final boolean finalEquals = equals;

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
                                    if (mentionedMembers.size() > 0 && finalEquals) {
                                        final User user = mentionedMembers.get(0).getUser();
                                        messageBuilder.setUsername(user.getName())
                                                .setAvatarUrl(user.getEffectiveAvatarUrl().replaceFirst("gif", "png") + "?size=512")
                                                .setContent(mentionedContent);
                                        client.send(messageBuilder.build());
                                        return;
                                    }
                                    messageBuilder.setUsername(author.getName())
                                            .setAvatarUrl(author.getEffectiveAvatarUrl().replaceFirst("gif", "png") + "?size=512")
                                            .setContent(regularContent);
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
                    if (mentionedMembers.size() > 0 && finalEquals) {
                        final User user = mentionedMembers.get(0).getUser();
                        messageBuilder.setUsername(user.getName())
                                .setAvatarUrl(user.getEffectiveAvatarUrl().replaceFirst("gif", "png") + "?size=512")
                                .setContent(mentionedContent);
                        client.send(messageBuilder.build());
                        return;
                    }
                    messageBuilder.setUsername(author.getName())
                            .setAvatarUrl(author.getEffectiveAvatarUrl().replaceFirst("gif", "png") + "?size=512")
                            .setContent(regularContent);
                    client.send(messageBuilder.build());
                }
        );
    }

    @Override
    public String getName() {
        return "say";
    }

    @Override
    public String getHelp(String prefix) {
        return "Says something for you\n" +
                "Usage : " + prefix + "say <msg>\n" +
                "        " + prefix + "say <@user> <msg>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.FUN;
    }
}
