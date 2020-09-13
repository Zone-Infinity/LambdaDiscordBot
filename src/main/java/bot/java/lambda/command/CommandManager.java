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

package bot.java.lambda.command;

import bot.java.lambda.Bot;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import bot.java.lambda.command.commands.common.*;
import bot.java.lambda.command.commands.fun.*;
import bot.java.lambda.command.commands.info.*;
import bot.java.lambda.command.commands.music.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager(){
        Bot bot = new Bot();

        // Info Commands
        addCommand(new HelpCommand(this));
        addCommand(new GuildCountCommand());

        // Common Commands
        addCommand(new PingCommand());
        addCommand(new InviteCommand());
        addCommand(new InstagramCommand());
        addCommand(new GenPassCommand());
        addCommand(new RollCommand());
        addCommand(new RandomCommand());
        addCommand(new EvalCommand());

        // Fun Commands
        addCommand(new MemeCommand());
        addCommand(new JokeCommand());
        addCommand(new CatCommand());
        addCommand(new DogCommand());
        addCommand(new LlamaCommand());
        addCommand(new DuckCommand());
        addCommand(new AlpacaCommand());
        addCommand(new SealCommand());
        addCommand(new CamelCommand());
        addCommand(new LizardCommand());
        addCommand(new FoxCommand());
        addCommand(new BirdCommand());
        addCommand(new WolfCommand());
        addCommand(new PandaCommand());

        // Music Commands
        addCommand(new JoinCommand());
        addCommand(new DisconnectCommand());
        addCommand(new PlayCommand());
        addCommand(new QueueCommand());
        addCommand(new SkipCommand(bot.getWaiter()));
        addCommand(new StopCommand());
        addCommand(new UserCountCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new PlaylistCommand());

    }

    private void addCommand(ICommand cmd){
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if(nameFound){
            throw new IllegalArgumentException("A command with this name is already present");
        }

        commands.add(cmd);
    }

    public List<ICommand> getCommands(){
        return commands;
    }

    @Nullable
    public ICommand getCommand(String search){
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if(cmd.getName().equals(searchLower)||cmd.getAliases().contains(searchLower)){
                return cmd;
            }
        }

        return null;

    }

    public void handle(GuildMessageReceivedEvent event, String prefix){
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if(cmd != null){
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        }
    }

}
