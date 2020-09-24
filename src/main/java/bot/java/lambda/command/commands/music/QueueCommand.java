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
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class QueueCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getMusicManager(ctx.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();

        EmbedBuilder builder = EmbedUtils.getDefaultEmbed();

        if(queue.isEmpty()){
            builder.setThumbnail("https://images-ext-2.discordapp.net/external/frvcoRmUh1i7edHL92fPYF3qwlg8ZQ-Hh6v1tFeRF6s/%3Fv%3D1/https/cdn.discordapp.com/emojis/753940153780928522.png")
                    .setTitle("The queue is empty")
                    .setDescription("Use -play <song_url> for adding song in the queue");
            channel.sendMessage(builder.build()).queue();
            return;
        }

        int trackCount = Math.min(queue.size(), 20);
        List<AudioTrack> tracks = new ArrayList<>(queue);

        builder.setTitle("Current Queue (Total : "+queue.size()+")");


        for(AudioTrack track : tracks){
            AudioTrackInfo info = track.getInfo();

            builder.appendDescription(String.format(
                    "|` %s -> %s `\n",
                    info.title,
                    info.author
            ));
        }
        channel.sendMessage(builder.build()).queue();
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getHelp() {
        return "Shows the current queue for the music player\n" +
                "Aliases : {q, songlist}";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }

    @Override
    public List<String> getAliases() {
        return List.of("q","songlist");
    }
}
