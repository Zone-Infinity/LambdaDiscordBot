package bot.java.lambda.command.commands.utils;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandType.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class DefaultAvatarCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        if (ctx.getArgs().isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        final String join = String.join(" ", ctx.getArgs());
        final String[] split = join.split("\\|");

        int inner = -1;
        int outer = 0;

        try {
            if (split.length == 1) {
                final String[] args = split[0].trim().split(",");
                if (args.length == 1) {
                    final String arg = args[0].trim();
                    if (arg.startsWith("#")) {
                        outer = new Color(Integer.parseInt(arg.replace("#", ""), 16)).getRGB();
                    } else {
                        outer = new Color(Integer.parseInt(arg)).getRGB();
                    }
                } else if (args.length == 3) {
                    final int r = Integer.parseInt(args[0].trim());
                    final int g = Integer.parseInt(args[1].trim());
                    final int b = Integer.parseInt(args[2].trim());

                    outer = new Color(r, g, b).getRGB();
                }
            } else if (split.length == 2) {
                final String[] args = split[0].trim().split(",");

                if (args.length == 1) {
                    final String arg = args[0].trim();
                    if (args[0].startsWith("#")) {
                        outer = new Color(Integer.parseInt(arg.replace("#", ""), 16)).getRGB();
                    } else {
                        outer = new Color(Integer.parseInt(arg)).getRGB();
                    }
                } else if (args.length == 3) {
                    final int r = Integer.parseInt(args[0]);
                    final int g = Integer.parseInt(args[1]);
                    final int b = Integer.parseInt(args[2]);

                    outer = new Color(r, g, b).getRGB();
                }

                final String[] args2 = split[1].trim().split(",");
                if (args2.length == 1) {
                    final String arg = args2[0].trim();
                    if (args2[0].startsWith("#")) {
                        inner = new Color(Integer.parseInt(arg.replace("#", ""), 16)).getRGB();
                    } else {
                        inner = new Color(Integer.parseInt(arg)).getRGB();
                    }
                } else if (args2.length == 3) {
                    final int r = Integer.parseInt(args2[0]);
                    final int g = Integer.parseInt(args2[1]);
                    final int b = Integer.parseInt(args2[2]);

                    inner = new Color(r, g, b).getRGB();
                }

            }
        } catch (NumberFormatException e) {
            channel.sendMessage("Arguments should be in RGB or #HexCode Format").queue();
            e.fillInStackTrace();
            return;
        }

        try {
            BufferedImage defaultAvatar = ImageIO.read(DefaultAvatarCommand.class.getResource("/images/defaultPfp.png"));
            BufferedImage newAvatar = new BufferedImage(defaultAvatar.getWidth(), defaultAvatar.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < defaultAvatar.getHeight(); i++) {
                for (int j = 0; j < defaultAvatar.getWidth(); j++) {
                    final int rgb = defaultAvatar.getRGB(j, i);
                    if (rgb != -1) {
                        newAvatar.setRGB(j, i, outer);
                    } else {
                        newAvatar.setRGB(j, i, inner);
                    }
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(newAvatar, "png", out);

            channel.sendMessage("Generated Successfully").addFile(out.toByteArray(), "NewAvatar.png").queue();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "defaultavatar";
    }

    @Override
    public String getHelp(String prefix) {
        return "Makes you a custom Default Avatar\n" +
                "Usage : " + prefix + "defaultavatar [<r>,<g>,<b> / rgb / #hex] | [<r>,<g>,<b> / rgb / #hex]";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.UTIL;
    }

    @Override
    public List<String> getAliases() {
        return List.of("defaulta", "davatar", "da");
    }
}
