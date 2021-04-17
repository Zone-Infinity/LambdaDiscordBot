package bot.java.lambda.command;

import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.commands.admin.CloseCommand;
import bot.java.lambda.command.commands.admin.EvalCommand;
import bot.java.lambda.command.commands.common.*;
import bot.java.lambda.command.commands.fun.*;
import bot.java.lambda.command.commands.games.*;
import bot.java.lambda.command.commands.images.*;
import bot.java.lambda.command.commands.info.*;
import bot.java.lambda.command.commands.music.*;
import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import bot.java.lambda.command.commands.settings.SetPrefixCommand;
import bot.java.lambda.command.commands.settings.SetWelcomeBackground;
import bot.java.lambda.command.commands.settings.SetWelcomeChannel;
import bot.java.lambda.command.commands.settings.SetWelcomeMessage;
import bot.java.lambda.command.commands.utils.*;
import bot.java.lambda.command.type.ICommand;
import bot.java.lambda.config.Config;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager(EventWaiter waiter) {

        // Owner Commands
        addCommand(new EvalCommand(waiter, this));
        // addCommand(new LeaveCommand());
        // addCommand(new GuildsCommand());
        addCommand(new CloseCommand());

        // Info Commands
        addCommand(new HelpCommand(this));
        addCommand(new EmoteUseCommand());
        // addCommand(new ServerInfoCommand());
        addCommand(new ServerEmojisCommand());
        addCommand(new ServerRolesCommand());
        addCommand(new AvatarCommand());
        addCommand(new IDCommand());
        // addCommand(new UserInfoCommand());
        addCommand(new BotInfoCommand(this));
        addCommand(new VoteCommand());

        // Common Commands
        addCommand(new PingCommand());
        addCommand(new InviteCommand());
        addCommand(new GenPassCommand());
        addCommand(new RandomCommand());
        addCommand(new SayCommand());
        addCommand(new EmojiCommand(waiter));
        addCommand(new DistractorCommand());
        addCommand(new LMGTFYCommand());
        addCommand(new UrbanCommand());
        addCommand(new PollCommand());
        addCommand(new UptimeCommand());
        addCommand(new ColorCommand());

        //Game Commands
        addCommand(new _8BallCommand());
        addCommand(new RollCommand());
        addCommand(new RPSCommand());
        addCommand(new CountCommand(waiter));
        addCommand(new TriviaCommand(waiter));

        // Fun Commands
        addCommand(new MemeCommand());
        addCommand(new JokeCommand());
        addCommand(new EmojifyCommand());
        addCommand(new EchoCommand());
        addCommand(new BoredCommand());
        addCommand(new AdviceCommand());
        addCommand(new FlipCommand());
        addCommand(new PixelCommand());
        //addCommand(new ChatCommand(waiter));

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
        addCommand(new LoopCommand());
        addCommand(new ShuffleCommand());
        addCommand(new VolumeCommand());

        // Utility Commands
        addCommand(new DefaultAvatarCommand());
        addCommand(new InvertCommand());
        addCommand(new BlackAndWhiteCommand());
        addCommand(new BlurCommand());
        addCommand(new PixelateCommand());
        addCommand(new DarkenCommand());
        addCommand(new DrakeCommand());
        addCommand(new Drake2Command());
        addCommand(new PasteCommand());
        addCommand(new ShortenUrlCommand());

        // Settings Commands
        addCommand(new SetPrefixCommand());
        addCommand(new SetWelcomeChannel());
        addCommand(new SetWelcomeMessage());
        addCommand(new SetWelcomeBackground(waiter));
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present : " + cmd.getName());
        }

        boolean aliasFound = this.commands.stream().anyMatch((it) -> it.getAliases().stream().anyMatch((alias) -> cmd.getAliases().contains(alias)));

        if (aliasFound) {
            throw new IllegalArgumentException("A command with a alias is already present : " + cmd.getName() + " : " + cmd.getAliases());
        }

        commands.add(cmd);
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        return this.commands.stream().filter(cmd -> cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)).findFirst().orElse(null);
    }

    public void handle(GuildMessageReceivedEvent event, String prefix) {
        final User user = event.getAuthor();
        final Guild guild = event.getGuild();
        final TextChannel channel = event.getChannel();
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix) + "|" + guild.getSelfMember().getAsMention() + "( +)?", "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            if ((cmd.getHelpCategory().equals(HelpCategory.OWNER) || cmd.getHelpCategory().equals(HelpCategory.UNKNOWN) || cmd.getHelpCategory().equals(HelpCategory.VAR_FOR_USE)) && !(Config.OWNER_IDS.contains(user.getId())))
                return;

            /*final List<User> users = coolDown.computeIfAbsent(cmd, it -> new ArrayList<>());
            if (users.contains(user) && !user.getId().equals(Config.get("owner_id"))) {
                channel.sendMessage("You have to wait " + cmd.getCoolDown() + " seconds before using this command").queue();
                return;
            }*/

            List<String> args = Arrays.asList(split).subList(1, split.length);
            CommandContext ctx = new CommandContext(event, args);

            /*if (Utils.hasProfanity(String.join(" ", args))) {
                channel.sendMessage("I don't reply to profanity").queue();
                return;
            }*/

            if (cmd.getHelpCategory().equals(HelpCategory.MUSIC)) {
                final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
                musicManager.setLastChannelId(channel.getIdLong());
            }

            cmd.handle(ctx);

            /*coolDown.get(cmd).add(user);
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            Runnable removeUser = () -> coolDown.get(cmd).remove(user);
            executor.schedule(removeUser, cmd.getCoolDown(), TimeUnit.SECONDS);*/

        }
    }
}
