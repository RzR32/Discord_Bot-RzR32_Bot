import config.CheckKey;
import config.PropertiesFile;
import listener.commands.*;
import listener.guild._Category;
import listener.guild._Role;
import listener.guild._TextChannel;
import listener.guild._VoiceChannel;
import listener.member.Member_join_leave;
import listener.member.Name_Change;
import listener.other.Guild_Ready;
import listener.other.JDA_Events;
import listener.other.Message_Reaction;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
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
            /*
            create Bot with needed GatewayIntent´s...
            */
            JDABuilder.create(
                    PropertiesFile.readsPropertiesFile("TOKEN"),
                    //GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                    //GatewayIntent.DIRECT_MESSAGE_TYPING,
                    GatewayIntent.DIRECT_MESSAGES,
                    //GatewayIntent.GUILD_BANS,
                    GatewayIntent.GUILD_EMOJIS,
                    //GatewayIntent.GUILD_INVITES,
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGE_REACTIONS,
                    //GatewayIntent.GUILD_MESSAGE_TYPING,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_PRESENCES,
                    GatewayIntent.GUILD_VOICE_STATES)
                    /*
                    set Flag´s
                    */
                    .setIdle(true)
                    .setActivity(Activity.listening(">help"))
                    /*
                    import listener
                    other
                    */
                    .addEventListeners(new Guild_Ready())
                    .addEventListeners(new JDA_Events())
                    .addEventListeners(new Member_join_leave())
                    .addEventListeners(new Message_Reaction())
                    .addEventListeners(new Name_Change())
                    /*
                    guild listener
                    */
                    .addEventListeners(new _Category())
                    .addEventListeners(new _Role())
                    .addEventListeners(new _TextChannel())
                    .addEventListeners(new _VoiceChannel())
                    /*
                    commands
                    */
                    .addEventListeners(new Blacklist_Command())
                    .addEventListeners(new DEactivate_Commands())
                    .addEventListeners(new DeleteMessage_Command())
                    .addEventListeners(new GameRole_Command())
                    .addEventListeners(new League_Command())
                    .addEventListeners(new Member_Commands())
                    .addEventListeners(new Owner_Commands())
                    /*
                    build the bot
                    */
                    .build();
            /*
            make a backup (dont start the time yet)
            */
            BackUp BU = new BackUp();
            BU.makeBackUp();

            LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + "Bot wird gestartet..." + ConsoleColor.reset, "info");
            LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + "Versuche zu Verbinden..." + ConsoleColor.reset, "info");
        } catch (Exception e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }
}