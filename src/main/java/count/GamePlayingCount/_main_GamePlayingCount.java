package count.GamePlayingCount;

import config.PropertiesFile;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import other._guild.check.CheckCategory;
import other._guild.check.CheckChannel;
import other._stuff.ConsoleColor;
import other._stuff.LogBack;
import other._stuff.Pause;

public class _main_GamePlayingCount {

    public static final LogBack LB = new LogBack();
    public static final Pause P = new Pause();
    public static final CheckCategory C_Category = new CheckCategory();
    public static final CheckChannel C_Channel = new CheckChannel();
    public static String channel_id = PropertiesFile.readsPropertiesFile("playingcount", "config");
    public static boolean b_deleting = false;

    public static void checkID() {
        if (channel_id == null) {
            channel_id = PropertiesFile.readsPropertiesFile("playingcount", "config");
        }
    }

    /*
    delete all messages in the channel > then do CountPlayingCount (below)
    */
    private static void DeleteMessages(Guild guild) {
        checkID();
        try {
            b_deleting = true;

            C_Category.checkingCategory(guild, "gamecategory");
            C_Channel.checkingChannel(guild, "playingcount");

            LB.log(Thread.currentThread().getName(), ConsoleColor.backByellow + ConsoleColor.black + "GamePlayingCount" + ConsoleColor.reset + " > Delete old Message´s...", "info");

            TextChannel channel = guild.getTextChannelById(channel_id);
            for (Message message : channel.getIterableHistory()) {
                try {
                    message.delete().complete();
                } catch (ErrorResponseException e_response) {
                    return;
                }
            }

            P.pause(1000);

            b_deleting = false;

            LB.log(Thread.currentThread().getName(), ConsoleColor.backByellow + ConsoleColor.black + "GamePlayingCount" + ConsoleColor.reset + " > Done, Count Games...", "info");

            MessageBot._message(guild);
            CountOfflineMember._message(guild);
            CountNotPlayingGames._message(guild);
            CountPlayingGames._message(guild);

            LB.log(Thread.currentThread().getName(), ConsoleColor.backByellow + ConsoleColor.black + "GamePlayingCount" + ConsoleColor.reset + " > Finish!", "info");
        } catch (ErrorResponseException ignored) {
        }
    }

    public void startCounter(Guild guild) {
        try {
            if (PropertiesFile.readsPropertiesFile(">playingcount_on", "config").equals("true") && PropertiesFile.readsPropertiesFile(">gamecategory_on", "config").equals("true")) {
                Runnable runnable = () -> DeleteMessages(guild);
                new Thread(runnable).start();
            }
        } catch (NullPointerException e) {
            startCounter(guild);
        }
    }
}