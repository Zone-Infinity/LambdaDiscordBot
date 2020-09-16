package bot.java.lambda.command.commands.common;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;

public class DistractorCommand implements ICommand {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        ctx.getMessage().delete().queue();
        ctx.getChannel().sendMessage(ctx.getJDA().getGuildById(740228383446925402L).getEmotesByName("Distractor",true).get(0).getAsMention()+"").queue();
    }

    @Override
    public String getName() {
        return "distract";
    }

    @Override
    public String getHelp() {
        return "Distracts Users";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }
}
