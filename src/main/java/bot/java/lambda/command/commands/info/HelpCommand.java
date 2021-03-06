package bot.java.lambda.command.commands.info;

import bot.java.lambda.CommandManager;
import bot.java.lambda.Constant;
import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.category.HelpCategory;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;
import bot.java.lambda.config.GuildSettings;
import bot.java.lambda.database.DatabaseManager;
import bot.java.lambda.utils.Discord;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@CommandHandler
public record HelpCommand(CommandManager manager) implements ICommand {

    private String getCategoryHelp(HelpCategory category) {
        String[] cmds = manager.getCommands()
                .stream()
                .filter(cmd -> cmd.getHelpCategory() == category)
                .map(cmd -> '`' + cmd.getName() + '`')
                .sorted()
                .toArray(String[]::new);
        return IntStream.range(0, cmds.length)
                .mapToObj(idx -> idx != 0 && idx % 4 == 0 ? "\n" + cmds[idx] : cmds[idx])
                .collect(Collectors.joining("|"));
    }

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        String prefix = GuildSettings.PREFIXES.computeIfAbsent(ctx.getGuild().getIdLong(), DatabaseManager.INSTANCE::getPrefix);
        final HelpCategory[] categories = HelpCategory.values();
        String[] descEmote = {
                Constant.Emote.LAMBDA_BLACK.emote,
                Constant.Emote.VIDEO_GAME.emote,
                Constant.Emote.LAMBDA_INFO.emote,
                Constant.Emote.MUSIC.emote,
                "\uD83C\uDF89",
                "<:Adorable:755717988677845033>",
                "\uD83D\uDEE0",
                "⚙️"
        };
        final List<String> categoryList = IntStream.range(0, categories.length - 3)
                .mapToObj(index -> {
                    final HelpCategory category = categories[index];
                    return "**" + descEmote[index] + " " + category.getCategory() + "** : `" + category.getDescription() + "`";
                }).collect(Collectors.toList());

        if (args.isEmpty()) {
            final String desc = "`" + prefix + "help <category>`\n" + String.join("\n", categoryList);

            final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                    .setThumbnail(null)
                    .setTitle("**λ** Help")
                    .setDescription("**[Invite Me](https://discord.com/api/oauth2/authorize?client_id=752052866809593906&permissions=540375616&scope=bot)** | **Join our [Support Server](https://discord.com/invite/XCNehWVrH7)**\n" +
                            "**Bot prefix** : ``" + prefix + "``\n " +
                            "```A Fun Bot which has many commands       \n" +
                            "It provides you with some Common commands\n" +
                            "Some Fun and most important MUSIC !! \uD83D\uDE04 \n" +
                            "If you have any confusion about the bot, \n" +
                            "   Contact " + Discord.getZoneInfinityAsTag(ctx.getJDA()) + "           \n" +
                            "      for help, bugs and suggestions    ```\n" +
                            "**Take a look on these commands** " + Constant.Emote.LAMBDA_WHITE.emote)
                    .addField("Command Categories", desc, true)
                    .setFooter("Total Commands : " + (manager.getCommands().stream().filter(it -> it.getHelpCategory() != HelpCategory.OWNER).count()), "https://media.discordapp.net/attachments/751297245068132472/753934986943528980/1tNXllYx93ipMLK44F6QWQw-removebg-preview.png");

            channel.sendMessage(embed.build()).queue();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            HelpCategory category = HelpCategory.VAR_FOR_USE.getCommand(search);
            String categoryName = category.getCategory();

            if (category == HelpCategory.UNKNOWN) {
                channel.sendMessage("Nothing found for " + search).queue();
                return;
            }
            String finalCategoryName = categoryName;
            categoryName = categoryList.stream().filter(it -> it.contains(finalCategoryName)).collect(Collectors.toList()).get(0);

            channel.sendMessage(EmbedUtils.getDefaultEmbed()
                    .setThumbnail(null)
                    .setTitle(categoryName)
                    .setDescription("```" + category.getDescription() + "```")
                    .addField("Commands", getCategoryHelp(category), true).build()).queue();

            return;
        }

        channel.sendMessage("Command```prolog\n" + command.getHelp(prefix).toUpperCase() + "\n" +
                "ALIASES : " + command.getAliases() + "```").queue();

    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows the list with commands in the bot\n" +
                "Usage : " + prefix + "help <command>";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }

    @Override
    public List<String> getAliases() {
        return List.of("commands", "cmd", "elp");
    }
}

