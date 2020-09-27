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

import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum HelpCategory {
    COM("Common","Common Commands for the server members"),
    ANIMAL("Animal","Animal Commands"),
    FUN("Fun","Fun Commands which include games and images"),
    INFO("Info","Info Commands for info of the bot , server and members"),
    MUSIC("Music","Music Commands when you are in a Voice Channel"),
    GAME("Game","Commands to play Small fun games"),
    VAR_FOR_USE("",""),
    UNKNOWN("UNKNOWN","UNKNOWN"),
    OWNER("Owner","Only Owners can use these commands and normal members can't see these commands"),
    SECRET("Secret","Secret Commands You have to find out , for hints {>hint <number>}");

    private final String category;
    private final String description;
    private static final List<HelpCategory> AllHelpCategory = List.of(COM, INFO, FUN, MUSIC);

    HelpCategory(String category, String description){
        this.category = category;
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public List<HelpCategory> getAllCategory(){
        return AllHelpCategory;
    }

    public @NotNull HelpCategory getCommand(String search){
        String searchLower = search.toLowerCase();

        for (HelpCategory category : getAllCategory()) {
            if(category.getCategory().equalsIgnoreCase(searchLower)){
                return category;
            }
        }
        return UNKNOWN;
    }
}
