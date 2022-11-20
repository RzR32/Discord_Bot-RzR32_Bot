package listener.guild;

import config.PropertiesFile;
import count._main_Counter;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import other._guild.AuditLog;

public class _Role extends ListenerAdapter {

    AuditLog AL = new AuditLog();
    _main_Counter c = new _main_Counter();

    @Override
    public void onRoleCreate(@NotNull RoleCreateEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            c.getint(event.getGuild(), "rolecount");
        }
    }

    @Override
    public void onRoleDelete(@NotNull RoleDeleteEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            c.getint(event.getGuild(), "rolecount");
        }
    }

    @Override
    public void onGenericRole(@NotNull GenericRoleEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            AL.GetEntry(event.getGuild(), event.getRole().getId(), "Role", event.getRole().getName());
        }
    }

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            for (Role role : event.getRoles()) {
                AL.GetEntry(event.getGuild(), event.getMember().getId(), "Role", role.getName());
            }
        }
    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            for (Role role : event.getRoles()) {
                AL.GetEntry(event.getGuild(), event.getMember().getId(), "Role", role.getName());
            }
        }
    }
}