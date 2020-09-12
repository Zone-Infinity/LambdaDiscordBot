package bot.java.lambda.command;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum HelpCategory {
    COM("Common","Common Commands for the server members"),
    FUN("Fun","Fun Commands which include games and images"),
    INFO("Info","Info Commands for info of the bot , server and members"),
    MUSIC("Music","Music Commands when you are in a Voice Channel"),
    VAR_FOR_USE("",""),
    UNKNOWN("UNKNOWN","UNKNOWN");

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
