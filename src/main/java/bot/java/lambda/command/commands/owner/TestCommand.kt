package bot.java.lambda.command.commands.owner

import bot.java.lambda.command.CommandContext
import bot.java.lambda.command.HelpCategory
import bot.java.lambda.command.ICommand

class TestCommand : ICommand {
    override fun handle(ctx: CommandContext) {

    }

    override fun getName(): String = "test"

    override fun getHelp(): String = "Test Command"

    override fun getHelpCategory(): HelpCategory = HelpCategory.OWNER
}