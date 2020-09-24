package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class EmojiCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        List<StringBuilder> listOfAllEmote = new ArrayList<>();
        int count = 0;
        int page = 0;
        for(Guild guild : ctx.getJDA().getGuilds()) {
            final List<Emote> emotes = guild.getEmotes();
            for (Emote emote : emotes) {
                if (count % 10 == 1 && count != 1) {
                    page++;
                }
                try {
                    listOfAllEmote.get(page).append(emote.getAsMention())
                            .append(" - ")
                            .append(emote.getName())
                            .append("\n");
                } catch (IndexOutOfBoundsException e) {
                    e.fillInStackTrace();
                    listOfAllEmote.add(new StringBuilder());
                }
                count++;
            }
        }

        if(args.isEmpty()){
            ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed()
                                    .setTitle("Emojis you can use : Page 1 out of "+listOfAllEmote.size())
                                    .setDescription(listOfAllEmote.get(0))
                                    .build()).queue();
            return;
        }


        try{
            ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed()
                    .setTitle("Emojis you can use : Page " + (Integer.parseInt(args.get(0))) + " out of " + listOfAllEmote.size())
                    .setDescription(listOfAllEmote.get((Integer.parseInt(args.get(0))) - 1))
                    .build()).queue();
        }catch (NumberFormatException e){
            e.fillInStackTrace();
            ctx.getChannel().sendMessage("Pls provide a page number").queue();
        }
    }

    @Override
    public String getName() {
        return "emojis";
    }

    @Override
    public String getHelp() {
        return "Shows you the list of Emojis of the Bot\n" +
                "Aliases : {emotes, allEmoji}";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }

    @Override
    public List<String> getAliases() {
        return List.of("emotes","allEmoji");
    }
}
