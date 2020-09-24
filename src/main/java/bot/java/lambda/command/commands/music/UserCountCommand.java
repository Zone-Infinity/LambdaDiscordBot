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
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class UserCountCommand implements ICommand {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member member = ctx.getMember();
        final GuildVoiceState voiceState = member.getVoiceState();
        if(!voiceState.inVoiceChannel()){
            channel.sendMessage("You need to be in a voice channel for this to work").queue();
            return;
        }
        final int size = voiceState.getChannel().getMembers().size();
        channel.sendMessage("Users in your voice channel : "+size).queue();

    }

    @Override
    public String getName() {
        return "user";
    }

    @Override
    public String getHelp() {
        return "Gives you the number of users there in your voice channel\n" +
                "Aliases : {u}";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }

    @Override
    public List<String> getAliases() {
        return List.of("u");
    }
}
