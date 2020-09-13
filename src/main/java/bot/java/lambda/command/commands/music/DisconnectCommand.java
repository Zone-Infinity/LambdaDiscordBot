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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class DisconnectCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        if(!selfVoiceState.inVoiceChannel()){
            channel.sendMessage("I'm not in a voice channel").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(selfVoiceState.getChannel().getMembers().size()-1>1) {
            if(!member.hasPermission(Permission.MANAGE_SERVER)){
                channel.sendMessage("You don't have ADMIN Permission\n" +
                        "The bot will disconnect automatically if is inactive").queue();
                return;
            }
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        if(!memberVoiceState.inVoiceChannel()){
            if(selfVoiceState.getChannel().getMembers().size()-1==0){
                channel.sendMessage("👍🏻").queue();
            }
            else {
                channel.sendMessage("You need to be in a voice channel for this command to work").queue();
                return;
            }
        }

        channel.sendMessageFormat("Disconnected `\uD83D\uDD0A %s`", selfVoiceState.getChannel().getName()).queue();
        audioManager.closeAudioConnection();

    }

    @Override
    public String getName() {
        return "disconnect";
    }

    @Override
    public String getHelp() {
        return "Makes the bot disconnect from it's voice channel";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }

    @Override
    public List<String> getAliases() {
        return List.of("dis","d");
    }
}

