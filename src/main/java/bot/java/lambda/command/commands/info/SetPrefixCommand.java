package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandType.ICommand;
import bot.java.lambda.utils.DatabaseUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SetPrefixCommand implements ICommand {
    @SuppressWarnings("unchecked")
    @Override
    public void handle(CommandContext ctx) {
        final String prefixes = DatabaseUtils.PrefixDotJson;
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        final Member member = ctx.getMember();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            channel.sendMessage("You don't have MANAGE SERVER Permission").queue();
            return;
        }

        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        final String join = String.join("", args);

        try (FileWriter file = new FileWriter(prefixes)) {
            try (FileReader reader = new FileReader(prefixes)) {
                JSONParser jsonParser = new JSONParser();

                try {
                    Object jsonFile = jsonParser.parse(reader);
                    JSONObject prefixList = (JSONObject) jsonFile;

                    prefixList.put(ctx.getGuild().getId(), join);
                    file.write(prefixList.toJSONString());
                } catch (ParseException e) {
                    JSONObject prefixList = new JSONObject();

                    prefixList.put(ctx.getGuild().getId(), join);
                    file.write(prefixList.toJSONString());

                    e.fillInStackTrace();
                }
                channel.sendMessage("Prefix set to `" + join + "`").queue();
            }
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sets the prefix for this server\n" +
                "Usage: " + prefix + "setprefix <prefix>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }

    @Override
    public int getCoolDown() {
        return 60;
    }

    @Override
    public List<String> getAliases() {
        return List.of("prefix");
    }
}
