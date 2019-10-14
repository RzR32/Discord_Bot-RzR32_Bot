package listener.other;

import count.GamePlayingCount;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.ConsoleColor;
import other.LogBack;

public class Status_from_Member extends ListenerAdapter {

    LogBack LB = new LogBack();

    /**
     * logs Status of User
     */
    @Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
        String username = event.getMember().getEffectiveName();
        if (event.getNewOnlineStatus() == OnlineStatus.ONLINE) {
            LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "STATUS" + ConsoleColor.reset + ConsoleColor.cyan + " > " + username + ConsoleColor.reset + ConsoleColor.green +
                    " ist nun online" + ConsoleColor.reset + " (davor " + event.getOldOnlineStatus() + ")","info");
        } else if (event.getNewOnlineStatus() == OnlineStatus.IDLE) {
            LB.log(Thread.currentThread().getName(),ConsoleColor.backblue + "STATUS" + ConsoleColor.reset + ConsoleColor.cyan + " > " + username + ConsoleColor.reset + ConsoleColor.yellow +
                    " ist nun abwesend" + ConsoleColor.reset + " (davor " + event.getOldOnlineStatus() + ")","info");
        } else if (event.getNewOnlineStatus() == OnlineStatus.DO_NOT_DISTURB) {
            LB.log(Thread.currentThread().getName(),ConsoleColor.backblue + "STATUS" + ConsoleColor.reset + ConsoleColor.cyan + " > " + username + ConsoleColor.reset + ConsoleColor.Bred +
                    " ist nun beschäftigt" + ConsoleColor.reset + " (davor " + event.getOldOnlineStatus() + ")","info");
        } else if (event.getNewOnlineStatus() == OnlineStatus.OFFLINE) {
            LB.log(Thread.currentThread().getName(),ConsoleColor.backblue + "STATUS" + ConsoleColor.reset + ConsoleColor.cyan + " > " + username + ConsoleColor.reset + ConsoleColor.Bblack +
                    " ist nun offline" + ConsoleColor.reset + " (davor " + event.getOldOnlineStatus() + ")","info");
        }
        GamePlayingCount GPC = new GamePlayingCount();
        GPC.CountOfflineMember(event.getGuild());
        GPC.CountNotPlayingGames(event.getGuild());
    }
}