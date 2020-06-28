package other._stuff;

import config.PropertiesFile;
import count._main_Counter;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import other._guild.check.CheckCategory;
import other._guild.check.CheckChannel;
import twitch.FollowerCount;

public class starting_all {

    private final LogBack LB = new LogBack();
    private final Pause P = new Pause();

    public void make_start(Guild guild) {
        /*
        check the category
        */
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "Checking for Category´s..." + ConsoleColor.reset, "info");
        CheckCategory C_Category = new CheckCategory();
        C_Category.StartChecking(guild);
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "category check, done!" + ConsoleColor.reset, "info");
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + ConsoleColor.reset, "info");

        int pause_int = 2500;
        P.pause(pause_int);

        /*
        check for counter channel
        */
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "Checking for counter Channel´s..." + ConsoleColor.reset, "info");
        _main_Counter c = new _main_Counter();
        c.StartCounter(guild);
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "counter Channel check, done!" + ConsoleColor.reset, "info");
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + ConsoleColor.reset, "info");

        P.pause(pause_int);

        /*
        check the other channel
        */
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "Checking for other Channel´s..." + ConsoleColor.reset, "info");
        CheckChannel C_Channel = new CheckChannel();
        C_Channel.StartChecking(guild);
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "other Channel check, done!" + ConsoleColor.reset, "info");
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + ConsoleColor.reset, "info");

        P.pause(pause_int);

        /*
        call all timer, if bot start (not restart!)
        */
        if (PropertiesFile.readsPropertiesFile("bot_starting", "config").equals("true")) {
            BackUp BU = new BackUp();
            BU.timer_backup(guild);

            FollowerCount FC = new FollowerCount();
            FC.timer_twitch_member(guild);

            PropertiesFile.writePropertiesFile("bot_starting", "false", "config");
        }
        LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + " > Bot gestartet!" + ConsoleColor.reset, "info");

        guild.getJDA().getPresence().setActivity(Activity.listening(">help"));
    }
}