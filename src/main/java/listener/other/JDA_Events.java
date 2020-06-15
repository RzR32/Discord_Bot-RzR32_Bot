package listener.other;

import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.ResumedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import other.ConsoleColor;
import other.LogBack;

public class JDA_Events extends ListenerAdapter {

    LogBack LB = new LogBack();

    @Override
    public void onDisconnect(@NotNull DisconnectEvent event) {
        LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + " > Verbindung verloren!" + ConsoleColor.reset, "error");
    }

    @Override
    public void onResume(@NotNull ResumedEvent event) {
        LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + " > Verbindung wiederhergestellt! (Resumed)" + ConsoleColor.reset, "info");
    }

    @Override
    public void onReconnect(@NotNull ReconnectedEvent event) {
        LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + " > Verbindung wiederhergestellt! (Reconnected)" + ConsoleColor.reset, "info");
        LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + " > Maybe the TwitchFollower Timer is now broken!" + ConsoleColor.reset, "info");
    }
}