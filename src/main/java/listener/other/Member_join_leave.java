package listener.other;

import config.PropertiesFile;
import count.Counter;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Member_join_leave extends ListenerAdapter {

    Counter c = new Counter();

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            c.getint(event.getGuild(), "membercount");
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            c.getint(event.getGuild(), "membercount");
        }
    }
}