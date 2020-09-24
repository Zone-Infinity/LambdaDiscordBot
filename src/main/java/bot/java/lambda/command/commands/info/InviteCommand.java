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

package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class InviteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        final String asTag = ctx.getAuthor().getAsTag();
        final String author = "Requested by "+asTag.substring(0,asTag.length()-5)+"λ"+ asTag.substring(asTag.length()-4);
        final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setAuthor(author)
                .setTitle("<:LambdaLock:755716500643512370> Click here to invite me","https://discord.com/api/oauth2/authorize?client_id=752052866809593906&permissions=540375616&scope=bot")
                .setDescription("\uD83D\uDE0A Here you go , invite me to as many servers as possible\n" +
                        "And help me becoming a verified bot <:Verified:753964332001394748>")
                .setThumbnail(ctx.getAuthor().getAvatarUrl());
        channel.sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getHelp() {
        return "Gives u a invite link of the bot";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }
}
