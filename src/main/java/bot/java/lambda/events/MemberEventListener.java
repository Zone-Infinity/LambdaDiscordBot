package bot.java.lambda.events;

import bot.java.lambda.config.GuildSettings;
import bot.java.lambda.database.DatabaseManager;
import bot.java.lambda.database.WelcomeSetting;
import bot.java.lambda.utils.ImageUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MemberEventListener extends ListenerAdapter {
    Logger LOGGER = LoggerFactory.getLogger(MemberEventListener.class);

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        final WelcomeSetting welcomeSetting = GuildSettings.WELCOME_SETTINGS.computeIfAbsent(event.getGuild().getIdLong(), DatabaseManager.INSTANCE::getWelcomeSettings);

        final String welcomeChannelId = welcomeSetting.getWelcomeChannelId();
        if (welcomeChannelId.equals("-1")) return;

        LOGGER.info("{} joined {}", event.getMember().getEffectiveName(), event.getGuild().getName());
        try {
            sendWelcomeMessage(event, welcomeSetting);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void sendWelcomeMessage(GuildMemberJoinEvent event, WelcomeSetting welcomeSetting) throws IOException {
        final Guild guild = event.getGuild();
        final Member member = event.getMember();

        final TextChannel welcomeChannel = guild.getTextChannelById(welcomeSetting.getWelcomeChannelId());
        final String welcomeMessage = welcomeSetting.getWelcomeMessage()
                .replaceAll("\\{user}|\\{user.name}", member.getEffectiveName())
                .replace("{guild}", guild.getName())
                .replace("{user.mention}", member.getAsMention());
        welcomeChannel.sendMessage(welcomeMessage)
                .addFile(ImageUtils.getBytes(getWelcomeImage(member.getUser(), guild, welcomeSetting)), "Welcome.png").queue();
    }

    public BufferedImage getWelcomeImage(User user, Guild guild, WelcomeSetting welcomeSetting) throws IOException {
        BufferedImage background = ImageIO.read(new File("database/background/" + welcomeSetting.getWelcomeBackgroundPath() + ".png"));
        final int backgroundWidth = background.getWidth();
        final int backgroundHeight = background.getHeight();

        BufferedImage image = new BufferedImage(backgroundWidth, backgroundHeight, BufferedImage.TYPE_INT_ARGB);

        String text = "Welcome " + user.getName() + " to " + guild.getName() + "!";
        BufferedImage avatar = ImageUtils.resize(ImageIO.read(new URL(user.getEffectiveAvatarUrl() + "?size=512")), backgroundWidth / 3, backgroundWidth / 3);

        int avatarX = backgroundWidth / 2 - avatar.getWidth(null) / 2;
        int avatarY = (backgroundHeight - 50) / 2 - avatar.getHeight(null) / 2;

        Graphics graphics = image.getGraphics();
        graphics.drawImage(avatar, avatarX, avatarY, null);

        Area clip = new Area(new RoundRectangle2D.Double(0, 0, backgroundWidth, backgroundHeight, 100, 100));
        Area avatarCircle = new Area(new Ellipse2D.Double(avatarX, avatarY, avatar.getWidth() - 1, avatar.getHeight() - 1));
        clip.subtract(avatarCircle);
        graphics.setClip(clip);

        graphics.drawImage(background, 0, 0, null);

        Font font = new Font(Font.SANS_SERIF, Font.BOLD, image.getWidth() / 30);
        graphics.setFont(font);
        FontMetrics metrics = graphics.getFontMetrics(font);
        graphics.drawString(text, backgroundWidth / 2 - metrics.stringWidth(text) / 2, backgroundHeight - (backgroundHeight / 5));
        graphics.setColor(new Color(0, 0, 0, 130));
        String memberNumber = "Member #" + (guild.getMemberCount() + 1);
        graphics.drawString(memberNumber, backgroundWidth / 2 - metrics.stringWidth(memberNumber) / 2, backgroundHeight - (backgroundHeight / 10));
        graphics.dispose();

        return image;
    }
}
