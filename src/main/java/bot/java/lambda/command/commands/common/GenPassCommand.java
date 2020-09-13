package bot.java.lambda.command.commands.common;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class GenPassCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();

        if(args.isEmpty()){
            channel.sendMessage("Missing Arguments \n" +
                    "do `<prefix> help genPass`").queue();
            return;
        }

        WebUtils.ins.getJSONObject("http://apis.duncte123.me/random-string/"+args.get(0)).async(
                (json) -> {
                    if(!json.get("success").asBoolean()){
                        channel.sendMessage("Something went wrong, try again later").queue();
                        System.out.println(json);
                        return;
                    }
                    final String data = json.get("data").asText();

                    final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                            .setTitle("Here's your Password")
                            .setDescription("`"+data+"`");

                    channel.sendMessage(embed.build()).queue();
                }
        );
    }

    @Override
    public String getName() {
        return "genPass";
    }

    @Override
    public String getHelp() {
        return "Generate Random password for you\n" +
                "Usage : <prefix> genPass <length>\n" +
                "        <prefix> genPass <length> dm ";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.COM;
    }

    @Override
    public List<String> getAliases() {
        return List.of("generatePass","password");
    }

}
