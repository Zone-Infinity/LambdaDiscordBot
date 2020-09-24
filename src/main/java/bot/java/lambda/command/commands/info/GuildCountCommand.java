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

import java.util.List;

public class GuildCountCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final int guilds = ctx.getJDA().getGuilds().size();
        ctx.getChannel().sendMessage("Currently the bot is in : "+guilds+" guilds").queue();
    }

    @Override
    public String getName() {
        return "guildcount";
    }

    @Override
    public String getHelp() {
        return "Sends the number of guilds the bot is in\n" +
                "Aliases : {gcount, gc}";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }

    @Override
    public List<String> getAliases() {
        return List.of("gcount","gc");
    }
}
