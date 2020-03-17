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
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import other.BackUp;
import other.ConsoleColor;
import other.LogBack;

public class Main {

    static LogBack LB = new LogBack();

    public static void main(String[] args) {
        try {
            LB.log(Thread.currentThread().getName(), "--------------------------------------------------", "info");
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
            }
            //https://github.com/DV8FromTheWorld/JDA/wiki/3%29-Getting-Started#connecting-to-discord-with-a-bot-account
            JDA builder = JDABuilder.createDefault(PropertiesFile.readsPropertiesFile("TOKEN")).setActivity(Activity.listening(">help")).build();
            /*
            ADD LISTENER

            > other
             */
            builder.addEventListener(new Games_from_Member());
            builder.addEventListener(new Guild_Ready());
            builder.addEventListener(new JDA_Events());
            builder.addEventListener(new Member_join_leave());
            builder.addEventListener(new Message_Reaction());
            builder.addEventListener(new Name_Change());
            builder.addEventListener(new Status_from_Member());
            /*
            > guild listener
             */
            builder.addEventListener(new _Category());
            builder.addEventListener(new _Role());
            builder.addEventListener(new _TextChannel());
            builder.addEventListener(new _VoiceChannel());
            /*
            > commands
             */
            builder.addEventListener(new Blacklist_Command());
            builder.addEventListener(new DEactivate_Commands());
            builder.addEventListener(new DeleteMessage_Command());
            builder.addEventListener(new GameRole_Command());
            builder.addEventListener(new League_Command());
            builder.addEventListener(new Member_Commands());
            builder.addEventListener(new Owner_Commands());
            /*
            I don´t know...
             */
            builder.setAutoReconnect(true);
            /*
            create Backup
            */
            BackUp BU = new BackUp();
            BU.timer_backup();

            LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + "Bot wird gestartet..." + ConsoleColor.reset, "info");
            LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + "Versuche zu Verbinden..." + ConsoleColor.reset, "info");
        } catch (Exception e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }
}