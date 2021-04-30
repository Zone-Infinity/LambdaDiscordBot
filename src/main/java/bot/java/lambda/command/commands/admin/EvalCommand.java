package bot.java.lambda.command.commands.admin;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.ICommand;
import bot.java.lambda.config.Config;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import groovy.lang.GroovyShell;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class EvalCommand implements ICommand {
    private final GroovyShell engine;
    private final String imports;
    private final EventWaiter waiter;

    public EvalCommand(EventWaiter waiter) {
        this.waiter = waiter;
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
                import bot.java.lambda.utils.*
                """;
    }

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        if (ctx.getAuthor().getIdLong() != Long.parseLong(Config.get("owner_id")))
            return;

        final List<String> args = ctx.getArgs();
        final Message message = ctx.getMessage();
        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        try {
            engine.setVariable("ctx", ctx);
            engine.setVariable("args", args);
            engine.setVariable("event", ctx.getEvent());
            engine.setVariable("message", message);
            engine.setVariable("channel", channel);
            engine.setVariable("jda", ctx.getJDA());
            engine.setVariable("guild", ctx.getGuild());
            engine.setVariable("member", ctx.getMember());
            engine.setVariable("author", ctx.getAuthor());
            engine.setVariable("ins", WebUtils.ins);
            engine.setVariable("defaultEmbed", EmbedUtils.getDefaultEmbed());
            engine.setVariable("waiter", waiter);

            String script = imports + message.getContentRaw().split("\\s+", 2)[1];
            Object out = engine.evaluate(script);

            channel.sendMessage(out == null ? "Executed without Error" : out.toString()).queue();

        } catch (Exception e) {
            channel.sendMessage("```" + e.getMessage() + "```").queue();
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
