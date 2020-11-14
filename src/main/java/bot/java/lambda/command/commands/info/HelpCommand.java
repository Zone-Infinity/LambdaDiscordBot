package bot.java.lambda.command.commands.info;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.CommandManager;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.events.Listener;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HelpCommand implements ICommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        final List<ICommand> commands = manager.getCommands();
        List<String> FunCmd = new ArrayList<>(),
                ComCmd = new ArrayList<>(),
                InfoCmd = new ArrayList<>(),
                MusicCmd = new ArrayList<>(),
                GameCmd = new ArrayList<>(),
                ImagesCmd = new ArrayList<>(),
                UtilsCmd = new ArrayList<>();
        StringBuilder FunBuild = new StringBuilder(),
                ComBuild = new StringBuilder(),
                InfoBuild = new StringBuilder(),
                MusicBuild = new StringBuilder(),
                GameBuild = new StringBuilder(),
                ImagesBuild = new StringBuilder(),
                UtilsBuild = new StringBuilder();

        if (args.isEmpty()) {
            for (ICommand command : commands) {
                if (command.getHelpCategory().equals(HelpCategory.FUN)) FunCmd.add(command.getName());
                else if (command.getHelpCategory().equals(HelpCategory.COM)) ComCmd.add(command.getName());
                else if (command.getHelpCategory().equals(HelpCategory.INFO)) InfoCmd.add(command.getName());
                else if (command.getHelpCategory().equals(HelpCategory.MUSIC)) MusicCmd.add(command.getName());
                else if (command.getHelpCategory().equals(HelpCategory.GAME)) GameCmd.add(command.getName());
                else if (command.getHelpCategory().equals(HelpCategory.IMAGES)) ImagesCmd.add(command.getName());
                else if (command.getHelpCategory().equals(HelpCategory.UTIL)) UtilsCmd.add(command.getName());
            }

            Collections.sort(FunCmd);
            Collections.sort(ComCmd);
            Collections.sort(InfoCmd);
            Collections.sort(MusicCmd);
            Collections.sort(GameCmd);
            Collections.sort(ImagesCmd);
            Collections.sort(UtilsCmd);

            int count = 0;
            for (String cmd : FunCmd) {
                if (count % 4 == 0 && count != 0) {
                    FunBuild.append("\n");
                }
                FunBuild.append("`").append(cmd).append("`|");
                count++;
            }
            count = 0;
            for (String cmd : ComCmd) {
                if (count % 4 == 0 && count != 0) {
                    ComBuild.append("\n");
                }
                ComBuild.append("`").append(cmd).append("`|");
                count++;
            }
            count = 0;
            for (String cmd : InfoCmd) {
                if (count % 4 == 0 && count != 0) {
                    InfoBuild.append("\n");
                }
                InfoBuild.append("`").append(cmd).append("`|");
                count++;
            }
            count = 0;
            for (String cmd : MusicCmd) {
                if (count % 4 == 0 && count != 0) {
                    MusicBuild.append("\n");
                }
                MusicBuild.append("`").append(cmd).append("`|");
                count++;
            }
            count = 0;
            for (String cmd : GameCmd) {
                if (count % 4 == 0 && count != 0) {
                    GameBuild.append("\n");
                }
                GameBuild.append("`").append(cmd).append("`|");
                count++;
            }
            count = 0;
            for (String cmd : ImagesCmd) {
                if (count % 4 == 0 && count != 0) {
                    ImagesBuild.append("\n");
                }
                ImagesBuild.append("`").append(cmd).append("`|");
                count++;
            }
            count = 0;
            for (String cmd : UtilsCmd) {
                if (count % 4 == 0 && count != 0) {
                    UtilsBuild.append("\n");
                }
                UtilsBuild.append("`").append(cmd).append("`|");
                count++;
            }
            FunBuild.deleteCharAt(FunBuild.length() - 1);
            GameBuild.deleteCharAt(GameBuild.length() - 1);
            InfoBuild.deleteCharAt(InfoBuild.length() - 1);
            MusicBuild.deleteCharAt(MusicBuild.length() - 1);
            ComBuild.deleteCharAt(ComBuild.length() - 1);
            ImagesBuild.deleteCharAt(ImagesBuild.length() - 1);
            UtilsBuild.deleteCharAt(UtilsBuild.length() - 1);

            String prefix = Listener.getPrefix(ctx.getGuild().getId());

            final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                    .setThumbnail(null)
                    .setTitle("**λ** Help")
                    .setDescription("**Join our [Support Server](https://discord.com/invite/XCNehWVrH7)**\n" +
                            "**Bot prefix** : ``" + prefix + "``\n " +
                            "```A Fun Bot which has many commands       \n" +
                            "It provides you with some Common commands\n" +
                            "Some Fun and most important MUSIC !! \uD83D\uDE04 \n" +
                            "If you have any confusion about the bot, \n" +
                            "   Contact Zone_Infinity#7763           \n" +
                            "      for help, bugs and suggestions    ```\n" +
                            "**Take a look on these commands** <:LambdaWhite:755717368386289721>")
                    .addField("<:LambdaBlack:755717304989384714>  Commons", ComBuild.toString(), true)
                    .addField("<:VideoGame:755715386980171796> Games", GameBuild.toString(), true)
                    .addBlankField(true)
                    .addField("<:LambdaInfo:755717519410724884> Info", InfoBuild.toString(), true)
                    .addField("<:Music:755716546827124787> Music", MusicBuild.toString(), true)
                    .addBlankField(true)
                    .addField("\uD83C\uDF89 Fun", FunBuild.toString(), true)
                    .addField("<:Adorable:755717988677845033> Images", ImagesBuild.toString(), true)
                    .addBlankField(true)
                    .addField("\uD83D\uDEE0️ Utils", UtilsBuild.toString(), true)
                    .setFooter("Total Commands : " + (manager.getCommands().stream().filter(it -> it.getHelpCategory() != HelpCategory.OWNER).count()), "https://media.discordapp.net/attachments/751297245068132472/753934986943528980/1tNXllYx93ipMLK44F6QWQw-removebg-preview.png");

            ctx.getAuthor().openPrivateChannel().queue(
                    privateChannel -> privateChannel.sendMessage(embed.build()).queue()
            );

            channel.sendMessage("Check Your DM").queue();
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

            channel.sendMessageFormat("Command Category```%s\n" +
                    "%s ```", categoryName, category.getDescription()).queue();
            return;
        }

        channel.sendMessage("Command```prolog\n" + command.getHelp(Listener.getPrefix(ctx.getGuild().getId())).toUpperCase() + "\n" +
                "Aliases : " + command.getAliases() + "```").queue();

    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows the list with commands in the bot\n" +
                "Usage : <prefix> help <command>\n" +
                "Aliases : {commands, cmd}";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.INFO;
    }

    @Override
    public List<String> getAliases() {
        return List.of("commands", "cmd");
    }
}

