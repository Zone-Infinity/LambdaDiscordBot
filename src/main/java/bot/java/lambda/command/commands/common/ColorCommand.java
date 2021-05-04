package bot.java.lambda.command.commands.common;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@CommandHandler
public class ColorCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();

        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(Color.BLACK);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

        try {
            if (args.size() == 1) {
                if (args.get(0).startsWith("#")) {
                    graphics.setPaint(new Color(Integer.parseInt(args.get(0).replace("#", ""), 16)));
                } else {
                    graphics.setPaint(new Color(Integer.parseInt(args.get(0))));
                }
            } else if (args.size() == 3) {
                final int r = Integer.parseInt(args.get(0));
                final int g = Integer.parseInt(args.get(1));
                final int b = Integer.parseInt(args.get(2));

                graphics.setPaint(new Color(r, g, b));
            }
        } catch (NumberFormatException e) {
            channel.sendMessage("Your arguments should be in number or #HexCode").queue();
            e.fillInStackTrace();
            return;
        } catch (IllegalArgumentException e) {
            channel.sendMessage("Invalid Arguments").queue();
            e.fillInStackTrace();
            return;
        }

        graphics.fillRect(10, 10, image.getWidth() - (10 * 2), image.getHeight() - (10 * 2));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            channel.sendMessage("Something went wrong").queue();
            e.printStackTrace();
            return;
        }

        channel.sendFile(outputStream.toByteArray(), "gg.png").queue();
    }

    @Override
    public String getName() {
        return "color";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sends the Color Image\n" +
                "Usage : " + prefix + "color < <r> <g> <b> / rgb / #hex >";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }

    @Override
    public int getCoolDown() {
        return 10;
    }
}
