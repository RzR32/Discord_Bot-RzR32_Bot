import config.PropertiesFile;
import config._Check.CheckFiles_Folder;
import config._Check.CheckKey;
import listener.guild.*;
import listener.member._Activity;
import listener.member._Join_Leave;
import listener.member._NameChange;
import listener.member._Status;
import listener.other.Guild_Ready;
import listener.other.JDA_Events;
import listener.other.Message_Reaction;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import other._stuff.BackUp;
import other._stuff.ConsoleColor;
import other._stuff.LogBack;

public class Main {

    static LogBack LB = new LogBack();

    public static void main(String[] args) {
        try {
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
            Bot Token
            */
            if (PropertiesFile.readsPropertiesFile("TOKEN", "discordtoken") == null || PropertiesFile.readsPropertiesFile("TOKEN", "discordtoken").isEmpty()) {
                LB.log(Thread.currentThread().getName(), "No Token found to start the Bot!", "error");
                return;
            }
            LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + "Bot wird gestartet..." + ConsoleColor.reset, "info");
            /*
            create Bot with needed GatewayIntent´s...
            */
            JDABuilder.create(
                    PropertiesFile.readsPropertiesFile("TOKEN", "discordtoken"),
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
                    .setRelativeRateLimit(false)
                    .setActivity(Activity.watching("starting..."))
                    /*
                    IMPORT Listener

                    commands listener
                    */
                    .addEventListeners(new listener.commands.member._main_member())
                    .addEventListeners(new listener.commands.owner._main_owner())
                    /*
                    guild listener
                    */
                    .addEventListeners(new _Category())
                    .addEventListeners(new _Role())
                    .addEventListeners(new _TextChannel())
                    .addEventListeners(new _VoiceChannel())
                    .addEventListeners(new _Emote())
                    /*
                    member listener
                    */
                    .addEventListeners(new _Activity())
                    .addEventListeners(new _Join_Leave())
                    .addEventListeners(new _NameChange())
                    .addEventListeners(new _Status())
                    /*
                    other listener
                     */
                    .addEventListeners(new Guild_Ready())
                    .addEventListeners(new JDA_Events())
                    .addEventListeners(new Message_Reaction())
                    /*
                    build the bot
                    */
                    .build();
            /*
            make a backup (dont start the time yet)
            */
            BackUp BU = new BackUp();
            BU.makeBackUp();

            LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + " > Verbunden!" + ConsoleColor.reset, "info");
        } catch (Exception e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }
}