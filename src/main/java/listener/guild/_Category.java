package listener.guild;

import config.PropertiesFile;
import count._main_Counter;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.category.GenericCategoryEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import other._guild.AuditLog;

public class _Category extends ListenerAdapter {

    AuditLog AL = new AuditLog();
    _main_Counter c = new _main_Counter();

    @Override
    public void onCategoryCreate(@NotNull CategoryCreateEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            c.getint(event.getGuild(), "categorycount");
        }
    }

    @Override
    public void onCategoryDelete(@NotNull CategoryDeleteEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            c.getint(event.getGuild(), "categorycount");
        }
    }

    @Override
    public void onGenericCategory(@NotNull GenericCategoryEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            AL.GetEntry(event.getGuild(), event.getCategory().getId(), "Category", event.getCategory().getName());
        }
    }
}