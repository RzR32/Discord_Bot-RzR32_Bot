package listener.member;

import count.GamePlayingCount;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.ConsoleColor;
import other.LogBack;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Status_from_Member extends ListenerAdapter {

    LogBack LB = new LogBack();

    /**
     * logs Status of User
     */
    @Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
        String username = event.getMember().getEffectiveName();

        SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss.SSS");
        Date D = new Date();
        String date = SDF.format(D);

        String s_prefix = ConsoleColor.red + date + " " + ConsoleColor.green + "[" + Thread.currentThread().getName() + "] " + ConsoleColor.yellow + "INFO - " + ConsoleColor.backblue + "STATUS" + ConsoleColor.reset + ConsoleColor.cyan + " > " + username + ConsoleColor.reset;
        String s_suffix = " (davor " + event.getOldOnlineStatus() + ")" + ConsoleColor.reset;

        if (event.getNewOnlineStatus() == OnlineStatus.ONLINE) {
            System.out.println(s_prefix + ConsoleColor.green + " ist nun online" + s_suffix);
        } else if (event.getNewOnlineStatus() == OnlineStatus.IDLE) {
            System.out.println(s_prefix + ConsoleColor.yellow + " ist nun abwesend" + s_suffix);
        } else if (event.getNewOnlineStatus() == OnlineStatus.DO_NOT_DISTURB) {
            System.out.println(s_prefix + ConsoleColor.Bred + " ist nun beschäftigt" + s_suffix);
        } else if (event.getNewOnlineStatus() == OnlineStatus.OFFLINE) {
            System.out.println(s_prefix + ConsoleColor.Bblack + " ist nun offline" + s_suffix);
        }
        GamePlayingCount GPC = new GamePlayingCount();
        GPC.CountOfflineMember(event.getGuild());
        GPC.CountNotPlayingGames(event.getGuild());
    }
}