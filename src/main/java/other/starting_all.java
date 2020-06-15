package other;

import check_create.CheckCategory;
import check_create.CheckChannel;
import config._Check.CheckFiles_Folder;
import config._Check.CheckKey;
import count._main_Counter;
import net.dv8tion.jda.api.entities.Guild;

public class starting_all {

    private static final LogBack LB = new LogBack();
    private static final Pause P = new Pause();

    public static void make_start(Guild guild) {
        /*
        check files/folder
        */
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "Checking for missing files/folder..." + ConsoleColor.reset, "info");
        CheckFiles_Folder C_Files = new CheckFiles_Folder();
        C_Files.checkingFiles();
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "file check, done!" + ConsoleColor.reset, "info");
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + ConsoleColor.reset, "info");

        /*
        check key´s
        */
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "Checking for missing (config) key..." + ConsoleColor.reset, "info");
        CheckKey CK = new CheckKey();
        CK.StartChecking();
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "key check, done!" + ConsoleColor.reset, "info");
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + ConsoleColor.reset, "info");

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
        start the BackUp timer
        (also call GPC)
        */
        BackUp BU = new BackUp();
        BU.timer_backup(guild);

        LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + " > Bot gestartet!" + ConsoleColor.reset, "info");
    }
}