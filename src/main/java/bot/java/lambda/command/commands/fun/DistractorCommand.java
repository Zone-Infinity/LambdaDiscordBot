package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;

@CommandHandler
public class DistractorCommand implements ICommand {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        ctx.getMessage().delete().queue();
        ctx.getChannel().sendMessage(ctx.getJDA().getEmoteById(749479039915261972L).getAsMention()).queue();
    }

    @Override
    public String getName() {
        return "distract";
    }

    @Override
    public String getHelp(String prefix) {
        return "Distracts Users";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.FUN;
    }
}
