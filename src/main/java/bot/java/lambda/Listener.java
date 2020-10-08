package bot.java.lambda;

import bot.java.lambda.command.CommandManager;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
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
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER  = LoggerFactory.getLogger(Listener.class);
    private TextChannel globalAuditsChannel;
    private final CommandManager manager;

    private final EventWaiter waiter;

    public Listener(EventWaiter waiter){
        this.waiter = waiter;
        manager = new CommandManager(waiter);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} is ready",event.getJDA().getSelfUser().getAsTag());
        event.getJDA().getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        globalAuditsChannel = event.getJDA().getTextChannelById(758724135790051368L);
        assert globalAuditsChannel != null;
        globalAuditsChannel.sendMessage("```I am ready```").queue();

        Runnable task = () -> {
            event.getJDA().getPresence().setActivity(Activity.watching(event.getJDA().getGuilds().size() + " guilds | Contact Zone_Infinityλ7763 for help"));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            event.getJDA().getPresence().setActivity(Activity.watching(">help | Contact Zone_Infinityλ7763 for help"));
        };

        executor.scheduleWithFixedDelay(task, 0, 5, TimeUnit.SECONDS);

    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        globalAuditsChannel = event.getJDA().getTextChannelById(753995632556900544L);
        assert globalAuditsChannel != null;
        globalAuditsChannel.sendMessage("```Added to "+event.getGuild()+"```").queue();
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        globalAuditsChannel = event.getJDA().getTextChannelById(753995632556900544L);
        assert globalAuditsChannel != null;
        globalAuditsChannel.sendMessage("```Removed from "+event.getGuild()+"```").queue();
    }

    @Override
    public void onGuildBan(@NotNull GuildBanEvent event) {
        globalAuditsChannel = event.getGuild().getTextChannelById(753995632556900544L);
        assert globalAuditsChannel != null;
        globalAuditsChannel.sendMessage("```Banned from "+event.getGuild()+"```").queue();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if(user.isBot() || event.isWebhookMessage()){
            return;
        }

        String prefix  = this.getPrefix();
        String raw = event.getMessage().getContentRaw();

        if(raw.equalsIgnoreCase(prefix+"guilds")){
            if( event.getAuthor().getIdLong() == Long.parseLong(Config.get("owner_id"))) {
                final List<Guild> guilds = event.getJDA().getGuilds();
                StringBuilder guildList = new StringBuilder();
                guildList.append("```");
                for (Guild guild : guilds)
                    guildList.append("-")
                            .append(guild.getName())
                            .append(" : ")
                            .append(guild.getMemberCount())
                            .append(" : ")
                            .append(guild.getId())
                            .append("\n");
                guildList.append("```");
                event.getChannel().sendMessage(guildList).queue();
            }
        }

        if(raw.equalsIgnoreCase("hello") || raw.equalsIgnoreCase("hi") || raw.equalsIgnoreCase("hey") || raw.equalsIgnoreCase("helo")){
            event.getChannel().sendMessage("Hello. What is your name?").queue();

            waiter.waitForEvent(MessageReceivedEvent.class,
                    e -> e.getAuthor().equals(event.getAuthor())
                            && e.getChannel().equals(event.getChannel())
                            && !e.getMessage().equals(event.getMessage()),
                    e -> {
                        final String message = e.getMessage().getContentRaw();
                        String name = e.getGuild().getSelfMember().getNickname() == null ?e.getJDA().getSelfUser().getName():e.getGuild().getSelfMember().getNickname();
                        if(message.contains(name) || message.contains("Lambda")){
                            e.getChannel().sendMessage("<:Wot:755715077029625916> Eh , it's my name. Bruh!!").queue();
                            return;
                        }
                        event.getChannel().sendMessage("Hello, `" + message + "`! I'm `" + name + "`!").queue();
                    },
                    1, TimeUnit.MINUTES, () -> event.getChannel().sendMessage("Sorry, you took too long.").queue());
        }

        if(raw.equals("<@!752052866809593906>")){
            event.getChannel().sendMessageFormat("Hi %s , my prefix is %s",event.getAuthor(),Config.get("prefix")).queue();
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
            try {
                manager.handle(event, prefix);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private String getPrefix(){
        return Config.get("prefix");
    }
}

