package bot.java.lambda.command.commands.owner;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
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
            engine.setProperty("ctx", ctx);
            engine.setProperty("args", args);
            engine.setProperty("event", ctx.getEvent());
            engine.setProperty("message", message);
            engine.setProperty("channel", channel);
            engine.setProperty("jda", ctx.getJDA());
            engine.setProperty("guild", ctx.getGuild());
            engine.setProperty("member", ctx.getMember());
            engine.setProperty("author", ctx.getAuthor());
            engine.setProperty("ins", WebUtils.ins);
            engine.setProperty("defaultEmbed", EmbedUtils.getDefaultEmbed());
            engine.setProperty("waiter", waiter);

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
