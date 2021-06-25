package bot.java.lambda.command.commands.admin;

import bot.java.lambda.Constant;
import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;
import groovy.lang.GroovyShell;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.Map;

@CommandHandler
public class EvalCommand implements ICommand {
    private final GroovyShell engine;
    private final String imports;

    public EvalCommand() {
        this.engine = new GroovyShell();
        this.imports = """
                import java.io.*
                import java.lang.*
                import java.util.*
                import java.util.concurrent.*
                import net.dv8tion.jda.core.*
                import net.dv8tion.jda.core.entities.*
                import net.dv8tion.jda.core.entities.impl.*
                import net.dv8tion.jda.core.managers.*
                import net.dv8tion.jda.core.managers.impl.*
                import net.dv8tion.jda.core.utils.*
                import net.dv8tion.jda.api.*
                import net.dv8tion.jda.api.entities.*
                import net.dv8tion.jda.api.managers.*
                import net.dv8tion.jda.api.managers.impl.*
                import net.dv8tion.jda.api.utils.*
                import com.jagrosh.jdautilities.commons.utils.*
                import com.jagrosh.jdautilities.commons.waiter.*
                import me.duncte123.botcommons.*
                import me.duncte123.botcommons.messaging.*
                import me.duncte123.botcommons.text.*
                import me.duncte123.botcommons.web.*
                import bot.java.lambda.*
                import bot.java.lambda.config.*
                import bot.java.lambda.database.*
                import bot.java.lambda.utils.*
                """;
    }

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        final List<String> args = ctx.getArgs();
        final Message message = ctx.getMessage();

        final Map<String, Object> variables = Map.of(
                "ctx", ctx,
                "args", args,
                "event", ctx.getEvent(),
                "message", message,
                "channel", channel,
                "jda", ctx.getJDA(),
                "guild", ctx.getGuild(),
                "member", ctx.getMember(),
                "author", ctx.getAuthor()
        );

        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments\n" +
                    "Please specify your codes, also these are some variables already there : " + variables.keySet()).queue();
            return;
        }

        try {
            variables.forEach(engine::setVariable);
            engine.setVariable("vars", variables.keySet());

            String script = imports + message.getContentRaw().split("\\s+", 2)[1];
            Object out = engine.evaluate(script);

            channel.sendMessage(out == null ? "Executed without Error" : out.toString()).queue();
            message.addReaction(Constant.Emote.LAMBDA_SUCCESS.asReaction).queue();
        } catch (Exception e) {
            channel.sendMessage("```" + e.getMessage() + "```").queue();
            message.addReaction(Constant.Emote.LAMBDA_FAILURE.asReaction).queue();
        }
    }

    @Override
    public String getName() {
        return "eval";
    }

    @Override
    public String getHelp(String prefix) {
        return "Takes groovy code and evaluate it";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.OWNER;
    }

    @Override
    public int getCoolDown() {
        return 0;
    }

}
