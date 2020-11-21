package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class ChatCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();

        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        String url = "https://api.snowflakedev.xyz/chatbot?message=" + String.join("%20", args);

        WebUtils.ins.getJSONObject(url).async(
                (json) -> ctx.getMessage().reply(json.get("message").asText()).queue()
        );

    }

    @Override
    public String getName() {
        return "chat";
    }

    @Override
    public String getHelp(String prefix) {
        return "Chat with the bot\n" +
                "Usage : " + prefix + "chat <message>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.FUN;
    }

    @Override
    public int getCoolDown() {
        return 5;
    }
}
