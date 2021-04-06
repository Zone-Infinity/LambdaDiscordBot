package bot.java.lambda.command.commands.admin;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.CommandManager;
import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandType.ICommand;
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
    private final CommandManager manager;

    public EvalCommand(EventWaiter waiter, CommandManager manager) {
        this.waiter = waiter;
        this.engine = new GroovyShell();
        this.manager = manager;
        this.imports = "import java.io.*\n" +
                "import java.lang.*\n" +
                "import java.util.*\n" +
                "import java.util.concurrent.*\n" +
                "import net.dv8tion.jda.core.*\n" +
                "import net.dv8tion.jda.core.entities.*\n" +
                "import net.dv8tion.jda.core.entities.impl.*\n" +
                "import net.dv8tion.jda.core.managers.*\n" +
                "import net.dv8tion.jda.core.managers.impl.*\n" +
                "import net.dv8tion.jda.core.utils.*\n" +
                "import net.dv8tion.jda.api.*\n" +
                "import net.dv8tion.jda.api.entities.*\n" +
                "import net.dv8tion.jda.api.managers.*\n" +
                "import net.dv8tion.jda.api.managers.impl.*\n" +
                "import net.dv8tion.jda.api.utils.*\n" +
                "import com.jagrosh.jdautilities.commons.utils.*\n" +
                "import com.jagrosh.jdautilities.commons.waiter.*\n" +
                "import bot.java.lambda.utils.*\n" +
                "";
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
