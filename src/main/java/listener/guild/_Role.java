package listener.guild;

import config.PropertiesFile;
import count.Counter;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.AuditLog;

public class _Role extends ListenerAdapter {

    AuditLog AL = new AuditLog();
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

    @Override
    public void onGenericRole(GenericRoleEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            AL.GetEntry(event.getGuild(), event.getRole().getId(), "Role", event.getRole().getName());
        }
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            for (Role role : event.getRoles()) {
                AL.GetEntry(event.getGuild(), event.getMember().getId(), "Role", role.getName());
            }
        }
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            for (Role role : event.getRoles()) {
                AL.GetEntry(event.getGuild(), event.getMember().getId(), "Role", role.getName());
            }
        }
    }
}