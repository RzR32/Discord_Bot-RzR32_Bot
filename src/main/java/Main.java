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
                     * import listener
                     * other
                     */
                    .addEventListeners(new Games_from_Member())
                    .addEventListeners(new Guild_Ready())
                    .addEventListeners(new JDA_Events())
                    .addEventListeners(new Member_join_leave())
                    .addEventListeners(new Message_Reaction())
                    .addEventListeners(new Name_Change())
                    .addEventListeners(new Status_from_Member())
                    /*
                     * guild listener
                     */
                    .addEventListeners(new _Category())
                    .addEventListeners(new _Role())
                    .addEventListeners(new _TextChannel())
                    .addEventListeners(new _VoiceChannel())
                    /*
                     * commands
                     */
                    .addEventListeners(new Blacklist_Command())
                    .addEventListeners(new DEactivate_Commands())
                    .addEventListeners(new DeleteMessage_Command())
                    .addEventListeners(new GameRole_Command())
                    .addEventListeners(new League_Command())
                    .addEventListeners(new Member_Commands())
                    .addEventListeners(new Owner_Commands())
                    /*
                     * set status/activity
                     */
                    .setActivity(Activity.listening(">help"))
                    /*
                     * build the bot
                     */
                    .build();

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