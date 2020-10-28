package bot.java.lambda.command.commands.common;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class Field {
    public String Title;
    public String Description;
    public boolean Inline;

    public Field(String title, String description, boolean inline) {
        Title = title;
        Description = description;
        Inline = inline;
    }
}

public class EmbedCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        String title = null,
                titleUrl = null,
                description = null,
                thumbnail = null,
                image = null,
                author = null,
                authorUrl = null,
                authorAvatarUrl = null,
                footer = null,
                footerIconUrl = null;
        boolean timestamp = false;
        int color = Role.DEFAULT_COLOR_RAW;
        List<Field> fields = new ArrayList<>();
        EmbedBuilder embed = new EmbedBuilder();

        List<String> embedParts = List.of(
                "title", "description", "timestamp", "color", "thumbnail",
                "image", "author", "footer", "new field"
        );

        final String join = String.join(" ", ctx.getArgs());
        final String[] split = join.replaceAll("[\n]", "").split(";");

        for (String s : split) {
            final String[] part = s.split("=");
            final String partName = part[0];
            if (embedParts.contains(partName)) {
                final String[] groupSplit = part[1].replaceAll("[{'}]", "").split(":");
                if (partName.equalsIgnoreCase("title")) {
                    if (groupSplit.length == 1) {
                        title = groupSplit[0];
                    } else if (groupSplit.length == 2) {
                        title = groupSplit[0];
                        titleUrl = groupSplit[1];
                    }
                } else if (partName.equalsIgnoreCase("description")) {
                    description = part[1].replaceAll("'", "");
                } else if (partName.equalsIgnoreCase("timestamp")) {
                    timestamp = asBoolean(part[1]);
                } else if (partName.equalsIgnoreCase("color")) {
                    try {
                        color = Integer.parseInt(part[1]);
                    } catch (NumberFormatException e) {
                        color = Role.DEFAULT_COLOR_RAW;
                        e.fillInStackTrace();
                    }
                } else if (partName.equalsIgnoreCase("thumbnail")) {
                    thumbnail = new StringBuilder(part[1]).deleteCharAt(0).deleteCharAt(new StringBuilder(part[1]).length()-2).toString().trim();
                } else if (partName.equalsIgnoreCase("image")) {
                    image = new StringBuilder(part[1]).deleteCharAt(0).deleteCharAt(new StringBuilder(part[1]).length()-2).toString().trim();
                } else if (partName.equalsIgnoreCase("author")) {
                    if (groupSplit.length == 1) {
                        author = groupSplit[0];
                    } else if (groupSplit.length == 2) {
                        author = groupSplit[0];
                        authorUrl = groupSplit[1];
                    } else if (groupSplit.length == 3) {
                        author = groupSplit[0];
                        authorUrl = groupSplit[1];
                        authorAvatarUrl = groupSplit[2];
                    }
                } else if (partName.equalsIgnoreCase("footer")) {
                    if (groupSplit.length == 1) {
                        footer = groupSplit[0];
                    } else if (groupSplit.length == 2) {
                        footer = groupSplit[0];
                        footerIconUrl = groupSplit[1];
                    }
                } else if(partName.equalsIgnoreCase("new field")){
                    if(groupSplit.length==3) {
                        String fieldName = groupSplit[0];
                        String fieldDescription = groupSplit[1];
                        String fieldInline = groupSplit[2];
                        fields.add(new Field(fieldName, fieldDescription, asBoolean(fieldInline)));
                    }else if(groupSplit.length==2) {
                        if(groupSplit[0]==null) {
                            fields.add(new Field(null, null, asBoolean(groupSplit[1])));
                        }
                    }
                }
            }
        }

        fields.forEach(f -> embed.addField(f.Title,f.Description,f.Inline));

        embed.setTitle(title, titleUrl)
                .setDescription(description)
                .setTimestamp(timestamp ? Instant.now() : null)
                .setColor(color)
                .setThumbnail(thumbnail)
                .setImage(image)
                .setAuthor(author,authorUrl,authorAvatarUrl)
                .setFooter(footer,footerIconUrl);

        ctx.getChannel().sendMessage(embed.build()).queue();

    }

    private static boolean asBoolean(String s) {
        return s.equalsIgnoreCase("true");
    }

    @Override
    public String getName() {
        return "embed";
    }

    @Override
    public String getHelp() {
        return "Makes a embed for you\n" +
                "#Usage [All guidelines to make a embed]\n" +
                ".<o> [Optional]\n" +
                ".INT [Integer]\n" +
                ".BOOLEAN [true or false]\n" +
                "\n" +
                ">embed title={Title:'URL'<o>};\n" +
                "description='Description <o>';\n" +
                "timestamp={present:'BOOLEAN'};\n" +
                "color=[INT]<o>;\n" +
                "thumbnail='Thumbnail-URL'<o>;\n" +
                "image='Image-URL'<o>;\n" +
                "author={Author:'Author-URL'<o>:'Author-Avatar-URL'<o>};\n" +
                "footer={Footer:'Icon-URL'<o>};\n" +
                "new field={Title:'Description'}:{inline:'BOOLEAN'}; <o>\n" +
                "new ...; [You can add up till 10 fields]\n" +
                "new field=null:{inline:'BOOLEAN'}; [If u want a blank field]\n" +
                ";\n" +
                "\n" +
                "#Title is compulsory :)\n" +
                "\n" +
                "[Example embed] :\n" +
                "# will give later";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }
}
