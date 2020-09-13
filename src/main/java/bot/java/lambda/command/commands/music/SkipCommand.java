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

package bot.java.lambda.command.commands.music;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import bot.java.lambda.command.commands.music.lavaplayer.TrackScheduler;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ConstantConditions")
public class SkipCommand implements ICommand {
    EventWaiter waiter;
    public SkipCommand(EventWaiter waiter){
        this.waiter = waiter;
    }
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getMusicManager(ctx.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        AudioPlayer player = musicManager.audioPlayer;

        if(!ctx.getMember().getVoiceState().inVoiceChannel()){
            channel.sendMessage("You are not in the voice channel").queue();
            return;
        }

        if(player.getPlayingTrack()==null){
            channel.sendMessage("The player isn't playing anything").queue();
            return;
        }

        final GuildVoiceState voiceState = ctx.getSelfMember().getVoiceState();
        final int size = voiceState.getChannel().getMembers().size();

        if(size < 2){
            try {
                scheduler.nextTrack();
            }catch (IllegalStateException e){
                e.fillInStackTrace();
            }
            return;
        }

        channel.sendMessage("Skip more").queue(
                message -> checkReaction(channel,ctx,scheduler)
        );
    }

    public void checkReaction(TextChannel channel, CommandContext ctx, TrackScheduler scheduler){
        final GuildVoiceState voiceState = ctx.getSelfMember().getVoiceState();
        final int size = voiceState.getChannel().getMembers().size();

        for (int i=0;i<=size-3;i++){
            int finalI = i;
            waiter.waitForEvent(MessageReceivedEvent.class,
                    e -> e.getChannel().equals(channel),
                    e -> {
                        if(e.getMessage().getContentRaw().equalsIgnoreCase(">skip"))
                            channel.sendMessage(finalI + " out of " + (size - 2)).queue();
                    },
                    3, TimeUnit.MINUTES, () -> channel.sendMessage("Time up !! can't skip").queue());
            if(i==size-2) {
                channel.sendMessage("Track Skipped ").queue();
                try {
                    scheduler.nextTrack();
                } catch (IllegalStateException e) {
                    e.fillInStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "Skips the current song";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }

    @Override
    public List<String> getAliases() {
        return List.of("s","skipsong");
    }
}
