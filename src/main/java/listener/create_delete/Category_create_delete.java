package listener.create_delete;

import config.PropertiesFile;
import count.Counter;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Category_create_delete extends ListenerAdapter {

    Counter c = new Counter();

    @Override
    public void onCategoryCreate(CategoryCreateEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            c.getint(event.getGuild(), "categorycount");
        }
    }

    @Override
    public void onCategoryDelete(CategoryDeleteEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            c.getint(event.getGuild(), "categorycount");
        }
    }
}