import config.CheckKey;
import config.PropertiesFile;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import other.ConsoleColor;
import other.LogBack;
/*
import listener
 */
import listener.commands.*;
import listener.create_delete.*;
import listener.other.*;

import javax.security.auth.login.LoginException;

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
            ck.checking();
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
            > create_delete
             */
            builder.addEventListeners(new Category_create_delete());
            builder.addEventListeners(new Role_create_delete());
            builder.addEventListeners(new TextChannel_create_delete());
            builder.addEventListeners(new VoiceChannel_create_delete());
            /*
            > commands
             */
            builder.addEventListeners(new GameRole_Command());
            builder.addEventListeners(new Blacklist_Command());
            builder.addEventListeners(new Member_Commands());
            builder.addEventListeners(new Owner_Commands());
            /*
            set status "listen" to ">help"
             */
            builder.setActivity(Activity.listening(">help"));
            /*
            finaly, create the Bot
             */
            builder.build();
            LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + "Bot wird gestartet..." + ConsoleColor.reset, "info");
            LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + "Versuche zu Verbinden..." + ConsoleColor.reset, "info");
        } catch (LoginException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }
}