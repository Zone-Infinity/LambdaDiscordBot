package bot.java.lambda.command.commands.owner;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

public class TestCommand implements ICommand {
    EventWaiter waiter;

    public TestCommand(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void handle(CommandContext ctx) {

    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getHelp() {
        return "Testing Commands";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.OWNER;
    }
}
