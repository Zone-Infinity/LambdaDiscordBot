package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;

import java.util.Objects;

public class DistractorCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        ctx.getMessage().delete().queue();
        ctx.getChannel().sendMessage(Objects.requireNonNull(ctx.getJDA().getGuildById(755433534495391805L)).getEmotesByName("Distractor", true).get(0).getAsMention() + "").queue();
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
        return HelpCategory.FUN;
    }
}
