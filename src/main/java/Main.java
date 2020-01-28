import config.CheckKey;
import config.PropertiesFile;
import listener.commands.*;
import listener.guild._Category;
import listener.guild._Role;
import listener.guild._TextChannel;
import listener.guild._VoiceChannel;
import listener.member.Games_from_Member;
import listener.member.Member_join_leave;
import listener.member.Name_Change;
import listener.member.Status_from_Member;
import listener.other.Guild_Ready;
import listener.other.JDA_Events;
import listener.other.Message_Reaction;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import other.BackUp;
import other.ConsoleColor;
import other.LogBack;

/*
import listener
 */

public class Main {

    static LogBack LB = new LogBack();

    public static void main(String[] args) {
        try {
            LB.log(Thread.currentThread().getName(), "--------------------------------------------------", "info");
            JDABuilder builder = new JDABuilder(AccountType.BOT);
            /*
            check key in config file
             */
            LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "Checking for missing config key..." + ConsoleColor.reset, "info");
            CheckKey ck = new CheckKey();
            ck.StartChecking();
            LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "key check, done!" + ConsoleColor.reset, "info");
            /*
            Bot Token
             */
            if (PropertiesFile.readsPropertiesFile("TOKEN") == null || PropertiesFile.readsPropertiesFile("TOKEN").isEmpty()) {
                LB.log(Thread.currentThread().getName(), "No Token found to start the Bot!", "error");
                return;
            } else {
                builder.setToken(PropertiesFile.readsPropertiesFile("TOKEN"));
            }
            /*
            ADD LISTENER

            > other
             */
            builder.addEventListeners(new Games_from_Member());
            builder.addEventListeners(new Guild_Ready());
            builder.addEventListeners(new JDA_Events());
            builder.addEventListeners(new Member_join_leave());
            builder.addEventListeners(new Message_Reaction());
            builder.addEventListeners(new Name_Change());
            builder.addEventListeners(new Status_from_Member());
            /*
            > guild listener
             */
            builder.addEventListeners(new _Category());
            builder.addEventListeners(new _Role());
            builder.addEventListeners(new _TextChannel());
            builder.addEventListeners(new _VoiceChannel());
            /*
            > commands
             */
            builder.addEventListeners(new Blacklist_Command());
            builder.addEventListeners(new DEactivate_Commands());
            builder.addEventListeners(new DeleteMessage_Command());
            builder.addEventListeners(new GameRole_Command());
            builder.addEventListeners(new League_Command());
            builder.addEventListeners(new Member_Commands());
            builder.addEventListeners(new Owner_Commands());
            /*
            set status "listen" to ">help"
             */
            builder.setActivity(Activity.listening(">help"));
            /*
            I don´t know...
             */
            builder.setAutoReconnect(true);
            /*
            create Backup
            */
            BackUp BU = new BackUp();
            BU.timer();
            /*
            finaly, create the Bot
             */
            builder.build();

            LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + "Bot wird gestartet..." + ConsoleColor.reset, "info");
            LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + "Versuche zu Verbinden..." + ConsoleColor.reset, "info");
        } catch (Exception e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }
}