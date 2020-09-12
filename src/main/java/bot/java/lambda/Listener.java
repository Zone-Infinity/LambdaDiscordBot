package bot.java.lambda;

import bot.java.lambda.command.CommandManager;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER  = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();
    private TextChannel globalAuditsChannel;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} is ready",event.getJDA().getSelfUser().getAsTag());
        final int guilds = event.getGuildTotalCount();
        event.getJDA().getPresence().setActivity(Activity.watching(guilds+" servers"+" | "+Config.get("prefix")+"help | Contact Zone_Infinity#7763 for help"));
        event.getJDA().getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);

    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        globalAuditsChannel = event.getGuild().getTextChannelById(753995632556900544L);
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        globalAuditsChannel = event.getGuild().getTextChannelById(753995632556900544L);
    }

    @Override
    public void onGuildBan(@NotNull GuildBanEvent event) {
        globalAuditsChannel = event.getGuild().getTextChannelById(753995632556900544L);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if(user.isBot() || event.isWebhookMessage()){
            return;
        }

        String prefix  = this.getPrefix();
        String raw = event.getMessage().getContentRaw();

        if(raw.equalsIgnoreCase(event.getJDA().getSelfUser().getAsMention())){
            event.getChannel().sendMessageFormat("Hi %s , my prefix is %s",event.getAuthor(),Config.get("prefix")).queue();
        }

        if(raw.equalsIgnoreCase(prefix+"guilds")){
            final List<Guild> guilds = event.getJDA().getGuilds();
            StringBuilder guildList = new StringBuilder();
            guildList.append("```");
            for (Guild guild : guilds)
                guildList.append("- ").append(guild.getName()).append("\n");
            guildList.append("```");
            event.getChannel().sendMessage(guildList).queue();
        }

        if(raw.equalsIgnoreCase(prefix+"close")){
            if(event.getAuthor().getId().equals(Config.get("owner_id"))){
                event.getChannel().sendMessage("Shutting Down").queue();
                event.getMessage().addReaction("✅").queue();
                LOGGER.info("Shutting Down");

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.fillInStackTrace();
                }

                event.getJDA().shutdown();
                BotCommons.shutdown(event.getJDA());
                System.exit(0);
            }else{
                event.getChannel().sendMessage("You cant do this").queue();
            }
        }


        if(raw.startsWith(prefix)){
            manager.handle(event, prefix);
        }
    }

    private String getPrefix(){
        return Config.get("prefix");
    }
}

