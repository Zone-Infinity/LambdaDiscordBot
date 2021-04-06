package bot.java.lambda.command.commands.music;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandType.ICommand;
import bot.java.lambda.command.commands.music.lavaplayer.PlayerManager;
import bot.java.lambda.command.commands.music.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.entities.TextChannel;

public class ShuffleCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final TrackScheduler scheduler = PlayerManager.getInstance().getMusicManager(ctx.getGuild()).scheduler;

        if (scheduler.queue.isEmpty()) {
            channel.sendMessage("There are no songs to shuffle").queue();
            return;
        }

        scheduler.shuffle();
        channel.sendMessage("The queue has been shuffled").queue();

    }

    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shuffles the current queue";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.MUSIC;
    }

    @Override
    public int getCoolDown() {
        return 5;
    }
}
