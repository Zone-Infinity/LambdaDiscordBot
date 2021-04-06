package bot.java.lambda.command.commands.admin;

import bot.java.lambda.apis.TopGG;
import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandType.ICommand;
import net.dv8tion.jda.api.JDA;

public class PostServerCountCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final JDA jda = ctx.getJDA();
        TopGG.postServerCount(jda, jda.getGuilds().size());
        ctx.getChannel().sendMessage("Posted Server Count").queue();
    }

    @Override
    public String getName() {
        return "postservercount";
    }

    @Override
    public String getHelp(String prefix) {
        return "Posts Server Count on Bot Lists";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.OWNER;
    }
}
