package bot.java.lambda.command.commands.settings;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.SettingCommand;
import bot.java.lambda.config.GuildSettings;
import bot.java.lambda.database.DatabaseManager;
import bot.java.lambda.database.Setting;
import bot.java.lambda.database.WelcomeSetting;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public record SetWelcomeBackground(EventWaiter waiter) implements SettingCommand {

    @Override
    public void updateSetting(CommandContext ctx) {
        final Guild guild = ctx.getGuild();
        final TextChannel channel = ctx.getChannel();

        final List<Message.Attachment> attachments = ctx.getMessage().getAttachments();

        if (attachments.isEmpty()) {
            channel.sendMessage("Upload Your Wallpaper (Expires after 60 seconds) : ").queue();
            waitForAttachment(ctx);

            return;
        }

        downloadAndSaveBackground(attachments.get(0), guild, file -> {
            updateSettingSilently(ctx, guild.getId());

            channel.sendMessage("New Welcome Image Background set to : ")
                    .addFile(file)
                    .queue();
        });
    }

    @Override
    public void updateSettingSilently(CommandContext ctx, String setting) {
        final long guildId = ctx.getGuild().getIdLong();

        final WelcomeSetting welcomeSetting = GuildSettings.WELCOME_SETTINGS.computeIfAbsent(guildId, DatabaseManager.INSTANCE::getWelcomeSettings);
        GuildSettings.WELCOME_SETTINGS.put(guildId, welcomeSetting.setWelcomeBackgroundPath(setting));
        DatabaseManager.INSTANCE.setWelcomeBackground(guildId, setting);
    }

    private void waitForAttachment(CommandContext ctx) {
        final User user = ctx.getAuthor();
        final TextChannel channel = ctx.getChannel();
        final Guild guild = ctx.getGuild();

        waiter.waitForEvent(
                GuildMessageReceivedEvent.class,
                it -> it.getChannel().equals(channel)
                        && it.getAuthor().equals(user),
                it -> {
                    final Message message = it.getMessage();
                    final List<Message.Attachment> attachments = message.getAttachments();

                    if (attachments.isEmpty()) {
                        channel.sendMessage("There was no Image found in your message.").queue();
                        return;
                    }

                    final Message.Attachment attachment = attachments.get(0);
                    downloadAndSaveBackground(attachment, guild,
                            file -> {
                                updateSettingSilently(ctx, guild.getId());

                                channel.sendMessage("New Welcome Image Background set to : ")
                                        .addFile(file)
                                        .queue();
                            }
                    );
                },
                60, TimeUnit.SECONDS, () -> channel.sendMessage("Sorry, you took too long to upload a Image.").queue()
        );
    }

    private void downloadAndSaveBackground(Message.Attachment attachment, Guild guild, Consumer<File> callback) {
        File file = new File("database/background/" + guild.getId() + ".png");

        final CompletableFuture<File> fileDownloaded = attachment.downloadToFile(file);
        fileDownloaded.thenAccept(f -> System.out.println("Saved background to " + f.getName()))
                .exceptionally(t ->
                        {
                            t.printStackTrace();
                            return null;
                        }
                );

        try {
            callback.accept(fileDownloaded.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Setting getSetting() {
        return Setting.WELCOME_BACKGROUND;
    }

    @Override
    public String getName() {
        return "setwelcomebackground";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sets the welcome background image for this server\n" +
                "Usage: " + prefix + "setwelcomebackground " +
                "[With Background Image Uploaded / Upload Image After Using The Command] (Use Command For More)\n";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.SETTINGS;
    }

    @Override
    public List<String> getAliases() {
        return List.of("welcomebackground");
    }
}
