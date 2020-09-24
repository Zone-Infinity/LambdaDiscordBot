package bot.java.lambda.command.commands.Owner;

import bot.java.lambda.Config;
import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

public class LeaveCommand implements ICommand {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getAuthor().getIdLong()!=Long.parseLong(Config.get("owner_id"))) {
            return;
        }
        TextChannel globalAuditsChannel = ctx.getJDA().getTextChannelById(753995632556900544L);
        if(!ctx.getArgs().isEmpty()){
            ctx.getJDA().getGuildById(ctx.getArgs().get(0)).leave().queue(
                    (success)-> {
                        globalAuditsChannel.sendMessage("```Left " + ctx.getJDA().getGuildById(ctx.getArgs().get(0)) + "```").queue();
                        ctx.getChannel().sendMessage("```Leaving " + ctx.getJDA().getGuildById(ctx.getArgs().get(0)) + "```").queue();
                    },
                    (failure)-> {
                        ctx.getChannel().sendMessage("Failed to Leave this server").queue();
                        failure.fillInStackTrace();
                    }
            );
            return;
        }

        ctx.getChannel().sendMessage("Leaving the server...").queue();
        ctx.getGuild().leave().queue(
                (success)-> {
                    globalAuditsChannel.sendMessage("```Left " + ctx.getGuild() + "```").queue();
                    ctx.getChannel().sendMessage("```Leaving " + ctx.getGuild() + "```").queue();
                },
                (failure)-> {
                    ctx.getChannel().sendMessage("Failed to Leave this server").queue();
                    failure.fillInStackTrace();
                }
        );
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "Leaves the server";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.OWNER;
    }
}
