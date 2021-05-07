package bot.java.lambda;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.commands.music.lavaplayer.GuildMusicManager;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import bot.java.lambda.command.type.ICommand;
import bot.java.lambda.config.CommandLoader;
import bot.java.lambda.config.Config;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();
    private final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);
    private final Bot bot;

    public CommandManager(Bot bot, EventWaiter waiter) {
        CommandLoader loader = new CommandLoader("bot.java.lambda.command.commands");
        loader.loadCommands();

        this.bot = bot;
        if (loader.addAllCommands(this, waiter)) {
            LOGGER.info("Added All Commands Successfully");
        }
    }

    public void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));
        if (nameFound)
            throw new IllegalArgumentException("A command with this name is already present : " + cmd.getName());

        boolean aliasFound = this.commands.stream().anyMatch((it) -> it.getAliases().stream().anyMatch((alias) -> cmd.getAliases().contains(alias)));
        if (aliasFound)
            throw new IllegalArgumentException("A command with a alias is already present : " + cmd.getName() + " : " + cmd.getAliases());

        commands.add(cmd);
        LOGGER.info("Added " + cmd.getClass().getName());
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
        final Message message = event.getMessage();
        String[] split = message.getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix) + "|" + guild.getSelfMember().getAsMention() + "( +)?", "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            final long userId = user.getIdLong();

            if (cmd.getHelpCategory() == (HelpCategory.OWNER) && !(Config.OWNER_IDS.contains(userId))) {
                System.out.println("Non-Owner used Owner Command");
                return;
            }

            if (cmd.containsCoolDown(userId) && !Config.OWNER_IDS.contains(userId)) {
                long secondsLeft = ((cmd.getCoolDown(userId) / 1000) + cmd.getCoolDown()) - (System.currentTimeMillis() / 1000);
                if (secondsLeft > 0) {
                    message.reply("You cant use that commands for another " + secondsLeft + " seconds!").mentionRepliedUser(false).queue();
                    return;
                }
            }

            List<String> args = Arrays.asList(split).subList(1, split.length);
            CommandContext ctx = new CommandContext(bot, event, args);

            if (cmd.getHelpCategory() != HelpCategory.INFO)
                cmd.addCoolDown(userId);

            if (cmd.getHelpCategory() == (HelpCategory.MUSIC)) {
                final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
                musicManager.setLastChannelId(channel.getIdLong());
            }

            cmd.handle(ctx);
        }
    }
}
