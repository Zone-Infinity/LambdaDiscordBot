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


import bot.java.lambda.command.CommandManager;
import bot.java.lambda.Config;
import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements ICommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager){
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
                GameCmd = new ArrayList<>();
        StringBuilder FunBuild = new StringBuilder(),
                ComBuild = new StringBuilder(),
                InfoBuild = new StringBuilder(),
                MusicBuild = new StringBuilder(),
                GameBuild = new StringBuilder();

        if(args.isEmpty()){
            for(ICommand command : commands){
                if (command.getHelpCategory().equals(HelpCategory.FUN)) FunCmd.add(command.getName());
                else if (command.getHelpCategory().equals(HelpCategory.COM)) ComCmd.add(command.getName());
                else if (command.getHelpCategory().equals(HelpCategory.INFO)) InfoCmd.add(command.getName());
                else if (command.getHelpCategory().equals(HelpCategory.MUSIC)) MusicCmd.add(command.getName());
                else if (command.getHelpCategory().equals(HelpCategory.GAME)) GameCmd.add(command.getName());
            }

            FunBuild.append("`->`    ");
            ComBuild.append("`->`    ");
            InfoBuild.append("`->`    ");
            MusicBuild.append("`->`    ");
            GameBuild.append("`->`    ");

            int count = 0;
            for(String cmd : FunCmd){
                if(count%5==0 && count!=0){
                    FunBuild.append("\n");
                }
                FunBuild.append("`").append(cmd).append("` , ");
                count++;
            }
            count = 0;
            for(String cmd : ComCmd){
                if(count%5==0 && count!=0){
                    ComBuild.append("\n");
                }
                ComBuild.append("`").append(cmd).append("` , ");
                count++;
            }
            count = 0;
            for(String cmd : InfoCmd){
                if(count%5==0 && count!=0){
                    InfoBuild.append("\n");
                }
                InfoBuild.append("`").append(cmd).append("` , ");
                count++;
            }
            count = 0;
            for(String cmd : MusicCmd){
                if(count%5==0 && count!=0){
                    MusicBuild.append("\n");
                }
                MusicBuild.append("`").append(cmd).append("` , ");
                count++;
            }
            count = 0;
            for(String cmd : GameCmd){
                if(count%5==0 && count!=0){
                    GameBuild.append("\n");
                }
                GameBuild.append("`").append(cmd).append("` , ");
                count++;
            }
            FunBuild.delete(FunBuild.length()-2, FunBuild.length()-1);
            ComBuild.delete(ComBuild.length()-2, ComBuild.length()-1);
            InfoBuild.delete(InfoBuild.length()-2, InfoBuild.length()-1);
            MusicBuild.delete(MusicBuild.length()-2, MusicBuild.length()-1);
            GameBuild.delete(GameBuild.length()-2, GameBuild.length()-1);


            final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                    .setTitle("**λ** Help")
                    .setDescription("**Bot prefix** : ``"+ Config.get("prefix")+"``\n " +
                            "`A Fun Bot which has many commands       \n" +
                            "It provides you with some Common commands\n" +
                            "Some Fun and most important MUSIC !! \uD83D\uDE04 \n" +
                            "If you have any confusion about the bot, \n " +
                            "   Contact Zone_Infinity#7763           \n" +
                            "       for help, bugs and suggestions    `\n"+
                            "\n**Take a look on these commands** <:LambdaWhite:753958007196614706>")
                    .addField("<:LambdaBlack:753942805101019238> Commons",ComBuild.toString(),false)
                    .addField("\uD83C\uDF89 Fun",FunBuild.toString(),false)
                    .addField("<:VideoGame:755305135504097290> Games",GameBuild.toString(),false)
                    .addField("<:InfoLambda:753955328160170065> Info",InfoBuild.toString(),false)
                    .addField("\uD83D\uDD09 Music",MusicBuild.toString(),false)
                    .setFooter("Total Commands : "+manager.getCommands().size(),"https://media.discordapp.net/attachments/751297245068132472/753934986943528980/1tNXllYx93ipMLK44F6QWQw-removebg-preview.png");
            channel.sendMessage(embed.build()).queue();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if(command == null){
            HelpCategory category = HelpCategory.VAR_FOR_USE.getCommand(search);
            String categoryName = category.getCategory();

            if(category == HelpCategory.UNKNOWN) {
                channel.sendMessage("Nothing found for " + search).queue();
                return;
            }
            channel.sendMessageFormat("Command Category```%s\n" +
                    "%s ```",categoryName,category.getDescription()).queue();
            return;
        }

        channel.sendMessage("Command```"+command.getHelp()+"```").queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Shows the list with commands in the bot\n" +
                "Usage : <prefix> help <command>";
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

