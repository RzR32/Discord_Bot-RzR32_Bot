package listener.create_delete;

import config.PropertiesFile;
import count.Counter;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Role_create_delete extends ListenerAdapter {

    Counter c = new Counter();

    @Override
    public void onRoleCreate(RoleCreateEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            c.getint(event.getGuild(), "rolecount");
        }
    }

    @Override
    public void onRoleDelete(RoleDeleteEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            c.getint(event.getGuild(), "rolecount");
        }
    }
}