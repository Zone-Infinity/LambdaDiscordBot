/*
 * Copyright 2020 Zone-Infinity
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package bot.java.lambda;

import bot.java.lambda.command.CommandManager;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
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
import java.util.Random;
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

    public EventWaiter getWaiter(){
        return waiter;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} is ready",event.getJDA().getSelfUser().getAsTag());
        event.getJDA().getPresence().setActivity(Activity.playing(Config.get("prefix")+"help | Contact Zone_Infinity#7763 for help"));
        event.getJDA().getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);

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
        globalAuditsChannel.sendMessage("```Removed to "+event.getGuild()+"```").queue();
    }

    @Override
    public void onGuildBan(@NotNull GuildBanEvent event) {
        globalAuditsChannel = event.getGuild().getTextChannelById(753995632556900544L);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
                
        Random random = new Random();
        int i = random.nextInt(1000);
        if(i==99 && !event.getAuthor().isBot() && !event.isWebhookMessage()){
            try {
                Emote emoji = event.getJDA().getGuildById(740228383446925402L).getEmotesByName("Distractor", true).get(0);
                event.getChannel().sendMessage("" + emoji.getAsMention()).queue();
            }catch (Exception e){
                e.fillInStackTrace();
            }
        }

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
                            .append(":")
                            .append(guild.getMemberCount())
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
                    e -> event.getChannel().sendMessage("Hello, `"+e.getMessage().getContentRaw()+"`! I'm `"+e.getJDA().getSelfUser().getName()+"`!").queue(),
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
            manager.handle(event, prefix);
        }
    }

    private String getPrefix(){
        return Config.get("prefix");
    }
}

