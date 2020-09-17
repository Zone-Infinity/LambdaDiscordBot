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

package bot.java.lambda.command.commands;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmojiAliases extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        final String message = event.getMessage().getContentRaw();
        final String[] split = message.split(" ");

        /*Guild guild = event.getJDA().getGuildById(740228383446925402L);
        final List<Emote> emotes = guild.getEmotes();
        StringBuilder allEmotes = new StringBuilder();
        for (Emote emote : emotes){
            allEmotes.append(emote.getAsMention())
                    .append(" - ")
                    .append(emote.getName())
                    .append("\n");
        }*/



    }
}
