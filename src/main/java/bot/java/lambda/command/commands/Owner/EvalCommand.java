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

package bot.java.lambda.command.commands.Owner;

import bot.java.lambda.Config;
import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.HelpCategory;
import bot.java.lambda.command.ICommand;
//import groovy.lang.GroovyShell;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class EvalCommand implements ICommand {
    //private final GroovyShell engine;
    //private final String imports;

    public EvalCommand(){
        //this.engine = new GroovyShell();
        /*this.imports = "import java.io.*\n" +
                "import java.lang.*\n" +
                "import java.util.*\n" +
                "import java.util.concurrent.*\n" +
                "import net.dv8tion.jda.core.*\n" +
                "import net.dv8tion.jda.core.entities.*\n" +
                "import net.dv8tion.jda.core.entities.impl.*\n" +
                "import net.dv8tion.jda.core.managers.*\n" +
                "import net.dv8tion.jda.core.managers.impl.*\n" +
                "import net.dv8tion.jda.core.utils.*\n";*/
    }

    @Override
    public void handle(CommandContext ctx) {

        if (ctx.getAuthor().getIdLong()!=Long.parseLong(Config.get("owner_id"))) {
            return;
        }

        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();

        /*if(args.isEmpty()){
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        try{
            engine.setProperty("args", args);
            engine.setProperty("event", ctx.getEvent());
            engine.setProperty("message", message);
            engine.setProperty("channel", channel);
            engine.setProperty("jda", ctx.getJDA());
            engine.setProperty("guild", ctx.getGuild());
            engine.setProperty("member", ctx.getMember());

            String script = imports + message.getContentRaw().split("\\s+",2)[1];
            Object out = engine.evaluate(script);

            channel.sendMessage(out==null?"Executed without Error" : out.toString()).queue();

        }catch (Exception e){
            channel.sendMessage(e.getMessage()).queue();
        }*/
    }

    @Override
    public String getName() {
        return "eval";
    }

    @Override
    public String getHelp() {
        return "Takes groovy code and evaluate it";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.OWNER;
    }
}
