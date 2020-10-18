package bot.java.lambda.command;

import bot.java.lambda.command.commands.Owner.EvalCommand;
import bot.java.lambda.command.commands.Owner.LeaveCommand;
import bot.java.lambda.command.commands.Owner.TestCommand;
import bot.java.lambda.command.commands.games.*;
import bot.java.lambda.command.commands.images.*;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
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

    public CommandManager(EventWaiter waiter){

        // Owner Commands
        addCommand(new EvalCommand());
        addCommand(new LeaveCommand());
        addCommand(new TestCommand(waiter));

        // Secret Commands


        // Info Commands
        addCommand(new HelpCommand(this));
        addCommand(new GuildCountCommand());
        addCommand(new EmoteUseCommand());
        addCommand(new GuildUserCountCommand());
        addCommand(new ServerInfoCommand());
        addCommand(new ServerEmojisCommand());
        addCommand(new ServerRolesCommand());
        addCommand(new AvatarCommand());

        // Common Commands
        addCommand(new PingCommand());
        addCommand(new InviteCommand());
        addCommand(new InstagramCommand());
        addCommand(new GenPassCommand());
        addCommand(new RandomCommand());
        addCommand(new SayCommand());
        addCommand(new EmojiCommand(waiter));
        addCommand(new DistractorCommand());
        addCommand(new LMGTFYCommand());
        addCommand(new UrbanCommand());
        addCommand(new PollCommand());
        addCommand(new UptimeCommand());

        //Game Commands
        addCommand(new _8BallCommand());
        addCommand(new RollCommand());
        addCommand(new RPSCommand());
        addCommand(new CountCommand(waiter));
        addCommand(new TriviaCommand(waiter));

        // Fun Commands
        addCommand(new MemeCommand());
        addCommand(new JokeCommand());
        addCommand(new GifCommand());
        addCommand(new EmojifyCommand());
        addCommand(new EchoCommand());
        addCommand(new BoredCommand());
        addCommand(new ProgrammingJokeCommand());
        addCommand(new AdviceCommand());

        // Image Commands
        addCommand(new CoffeeCommand());
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
        addCommand(new SkipCommand(waiter));
        addCommand(new StopCommand(waiter));
        addCommand(new UserCountCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new PlaylistCommand());
        addCommand(new ShuffleCommand());
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
