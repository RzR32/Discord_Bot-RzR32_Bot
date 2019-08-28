package listener.other;

import check_create.CheckCategory;
import check_create.CheckChannel;
import config.CheckFiles_Folder;
import count.GamePlayingCount;
import other.ConsoleColor;
import other.LogBack;
import other.Members;
import count.*;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Guild_Ready extends ListenerAdapter {

    LogBack LB = new LogBack();

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + " > Verbunden!" + ConsoleColor.reset, "info");
        /*
        check files/folder
         */
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "Checking for missing files/folder..." + ConsoleColor.reset, "info");
        CheckFiles_Folder C_Files = new CheckFiles_Folder();
        C_Files.checkingFiles();
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "file check, done!" + ConsoleColor.reset, "info");
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + ConsoleColor.reset, "info");

        /*
        check the category
         */
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "Checking for Category´s..." + ConsoleColor.reset, "info");
        CheckCategory C_Category = new CheckCategory();
        C_Category.StartChecking(event.getGuild());
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "category check, done!" + ConsoleColor.reset, "info");
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + ConsoleColor.reset, "info");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException error) {
            LB.log(Thread.currentThread().getName(), error.getMessage(), "error");
        }

        /*
        check for counter channel
         */
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "Checking for counter Channel´s..." + ConsoleColor.reset, "info");
        Counter c = new Counter();
        c.StartCounter(event.getGuild());
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "counter Channel check, done!!" + ConsoleColor.reset, "info");
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + ConsoleColor.reset, "info");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException error) {
            LB.log(Thread.currentThread().getName(), error.getMessage(), "error");
        }

        /*
        check the other channel
         */
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "Checking for other Channel´s..." + ConsoleColor.reset, "info");
        CheckChannel C_Channel = new CheckChannel();
        C_Channel.StartChecking(event.getGuild());
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "other Channel check, done!" + ConsoleColor.reset, "info");
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + ConsoleColor.reset, "info");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException error) {
            LB.log(Thread.currentThread().getName(), error.getMessage(), "error");
        }

        /*
        check for new member or if some member leaved
         */
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "Checking for new Member or leaved Member..." + ConsoleColor.reset, "info");
        Members members = new Members();
        members.Member_CheckMembersOnGuild(event.getGuild());
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "Member check, done!" + ConsoleColor.reset, "info");
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + ConsoleColor.reset, "info");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException error) {
            LB.log(Thread.currentThread().getName(), error.getMessage(), "error");
        }

        /*
        GamePlayingCount
         */
        GamePlayingCount GPC = new GamePlayingCount();
        GPC.startCounter(event.getGuild());
        LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + " > Bot gestartet!" + ConsoleColor.reset, "info");
    }
}