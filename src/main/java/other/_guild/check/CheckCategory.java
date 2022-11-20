package other._guild.check;

import config.PropertiesFile;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import other._guild.create.CreateCategory;
import other._stuff.LogBack;

import java.util.ArrayList;

public class CheckCategory {

    private static final ArrayList<String> list_ID = new ArrayList<>() {{
        add("maincount");
        add("streamcategory");
        add("gamecategory");
    }};
    LogBack LB = new LogBack();

    public void StartChecking(Guild guild) {
        for (String category : list_ID) {
            checkingCategory(guild, category);
        }
    }

    public void checkingCategory(Guild guild, String category) {
        if (PropertiesFile.readsPropertiesFile(">" + category + "_on", "config").equals("true")) {
            String id = PropertiesFile.readsPropertiesFile(category, "config");
            if (id.isBlank() || id.isEmpty() || PropertiesFile.readsPropertiesFile(category, "config") == null) {
                LB.log(Thread.currentThread().getName(), "ID in config file for category *" + category + "* is null, search for another or create new", "warn");
                String s = null;
                switch (category) {
                    case "maincount":
                        s = "ServerStats";
                        break;
                    case "streamcategory":
                        s = "Stream";
                        break;
                    case "gamecategory":
                        s = "Game-Category";
                        break;
                }
                for (Category cat : guild.getCategories()) {
                    if (cat.getName().equals(s)) {
                        PropertiesFile.writePropertiesFile(category, cat.getId(), "config");
                        LB.log(Thread.currentThread().getName(), "Category found with the name *" + s + "*", "info");
                        return;
                    }
                }
                LB.log(Thread.currentThread().getName(), "Create new category *" + s + "*", "info");
                CreateCategory c = new CreateCategory();
                assert s != null;
                switch (s) {
                    case "ServerStats":
                        c.MainCount(guild);
                        break;
                    case "Stream":
                        c.MainStream(guild);
                        break;
                    case "Game-Category":
                        c.MainGames(guild);
                        break;
                }
            } else {
                Category guild_category = guild.getCategoryById(id);
                if (guild_category == null) {
                    LB.log(Thread.currentThread().getName(), "ID for *" + category + "* is not a category | or dont exists!", "error");
                    PropertiesFile.writePropertiesFile(category, "", "config");
                    checkingCategory(guild, category);
                }
            }
        }
    }
}