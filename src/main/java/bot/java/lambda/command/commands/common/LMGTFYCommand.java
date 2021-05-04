package bot.java.lambda.command.commands.common;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;

import java.util.List;

@CommandHandler
public class LMGTFYCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage("Missing Arguments").queue();
            return;
        }
        String link = String.join("+", ctx.getArgs());
        ctx.getChannel().sendMessage("http://lmgtfy.com/?q=" + link).queue();
    }

    @Override
    public String getName() {
        return "lmgtfy";
    }

    @Override
    public String getHelp(String prefix) {
        return "Googles things for u";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }

    @Override
    public List<String> getAliases() {
        return List.of("google");
    }
}
