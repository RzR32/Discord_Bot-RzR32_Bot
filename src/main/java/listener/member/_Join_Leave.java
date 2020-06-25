package listener.member;

import config.PropertiesFile;
import count.GamePlayingCount.CountNotPlayingGames;
import count.GamePlayingCount.CountOfflineMember;
import count.GamePlayingCount.ForwardPlayingGame;
import count._main_Counter;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class _Join_Leave extends ListenerAdapter {

    _main_Counter c = new _main_Counter();

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            c.getint(event.getGuild(), "membercount");

            /*refresh the counter*/
            for (Activity activity : Objects.requireNonNull(event.getMember()).getActivities()) {
                ForwardPlayingGame._message(event.getGuild(), activity);
            }
            CountOfflineMember._message(event.getGuild());
            CountNotPlayingGames._message(event.getGuild());
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            c.getint(event.getGuild(), "membercount");

            /*refresh the counter*/
            for (Activity activity : Objects.requireNonNull(event.getMember()).getActivities()) {
                ForwardPlayingGame._message(event.getGuild(), activity);
            }
            CountOfflineMember._message(event.getGuild());
            CountNotPlayingGames._message(event.getGuild());
        }
    }
}