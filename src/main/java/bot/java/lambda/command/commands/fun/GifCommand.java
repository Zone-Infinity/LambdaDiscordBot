package bot.java.lambda.command.commands.fun;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.exception.GiphyException;
import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;

public class GifCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        Giphy giphy = new Giphy("GXoUS4S4b5TNuKEAcTYoV7tyBZ77jGdM");

        if(ctx.getArgs().isEmpty()){
            ctx.getChannel().sendMessage("Missing Arguments").queue();
        }
        try {
            if(ctx.getArgs().get(0).equalsIgnoreCase("trend")){
                ctx.getChannel().sendMessage(giphy.trend().getDataList().get(0).getImages().getOriginal().getUrl()).queue();
                return;
            }
            ctx.getChannel().sendMessage(giphy.searchRandom(String.join(" ",ctx.getArgs())).getData().getUrl()).queue();
        } catch (GiphyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "gif";
    }

    @Override
    public String getHelp() {
        return "Returns a random gif\n" +
                "Usage : >gif <tags> \n" +
                "Trending Gifs : >gif trend";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.FUN;
    }
}
