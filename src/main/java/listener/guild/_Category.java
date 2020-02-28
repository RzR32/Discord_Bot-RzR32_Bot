package listener.guild;

import config.PropertiesFile;
import count.Counter;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.category.GenericCategoryEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.AuditLog;

public class _Category extends ListenerAdapter {

    AuditLog AL = new AuditLog();
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

    @Override
    public void onGenericCategory(GenericCategoryEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            AL.GetEntry(event.getGuild(), event.getCategory().getId(), "Category", event.getCategory().getName());
        }
    }
}