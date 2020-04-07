package listener.member;

import count.GamePlayingCount;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.ConsoleColor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Status_from_Member extends ListenerAdapter {
    /**
     * logs Status of User
     */
    @Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
        /*
        new status - color and string set
        */
        String s_mid_new = null;
        String s_mid_color_new = null;
        if (event.getNewOnlineStatus() == OnlineStatus.ONLINE) {
            s_mid_color_new = ConsoleColor.green;
            s_mid_new = "online";
        } else if (event.getNewOnlineStatus() == OnlineStatus.IDLE) {
            s_mid_color_new = ConsoleColor.yellow;
            s_mid_new = "abwesend";
        } else if (event.getNewOnlineStatus() == OnlineStatus.DO_NOT_DISTURB) {
            s_mid_color_new = ConsoleColor.red;
            s_mid_new = "beschäftigt";
        } else if (event.getNewOnlineStatus() == OnlineStatus.OFFLINE) {
            s_mid_color_new = ConsoleColor.Bblack;
            s_mid_new = "offline";
        }

        /*
        old status - color and string set
        */
        String s_mid_old = null;
        String s_mid_color_old = null;
        if (event.getOldOnlineStatus() == OnlineStatus.ONLINE) {
            s_mid_color_old = ConsoleColor.green;
            s_mid_old = "online";
        } else if (event.getOldOnlineStatus() == OnlineStatus.IDLE) {
            s_mid_color_old = ConsoleColor.yellow;
            s_mid_old = "abwesend";
        } else if (event.getOldOnlineStatus() == OnlineStatus.DO_NOT_DISTURB) {
            s_mid_color_old = ConsoleColor.red;
            s_mid_old = "beschäftigt";
        } else if (event.getOldOnlineStatus() == OnlineStatus.OFFLINE) {
            s_mid_color_old = ConsoleColor.Bblack;
            s_mid_old = "offline";
        }
        String username = event.getMember().getEffectiveName();

        /*
        Console OutPut like Logback
        */
        SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss.SSS");
        Date D = new Date();
        String date = SDF.format(D);
        String color = ConsoleColor.Bmagenta;

        String s_prefix = ConsoleColor.red + date + " " + ConsoleColor.green + "[" + Thread.currentThread().getName() + "] " + ConsoleColor.yellow + "INFO - " + ConsoleColor.backblue + "STATUS" + ConsoleColor.reset + " > " + ConsoleColor.cyan + username + ConsoleColor.reset;
        String s_suffix_old_status = color + " (davor " + s_mid_color_old + s_mid_old + color + ")";

        System.out.println(s_prefix + color + " ist nun " + s_mid_color_new + s_mid_new + s_suffix_old_status + ConsoleColor.reset);

        GamePlayingCount GPC = new GamePlayingCount();
        GPC.CountOfflineMember(event.getGuild());
        GPC.CountNotPlayingGames(event.getGuild());
    }
}