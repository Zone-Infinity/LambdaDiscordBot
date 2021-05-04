package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

@CommandHandler
public class PixelCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        int width = 200, height = 200;

        BufferedImage image;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = image.createGraphics();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                graphics.setPaint(new Color(new Random().nextInt(256 * 256 * 256)));
                graphics.fillRect(x, y, x, y);
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            ctx.getChannel().sendMessage("Something went wrong").queue();
            e.fillInStackTrace();
            return;
        }

        ctx.getChannel().sendFile(outputStream.toByteArray(), "gg.png").queue();

    }

    @Override
    public String getName() {
        return "pixel";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives you a random pixel image (200 x 200)";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.FUN;
    }

    @Override
    public int getCoolDown() {
        return 15;
    }
}
