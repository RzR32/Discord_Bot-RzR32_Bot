package count;

import check_create.CheckCategory;
import check_create.CheckChannel;
import config.PropertiesFile;
import net.dv8tion.jda.api.entities.Guild;
import other.LogBack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class TwitchFollowerCount extends Thread {

    LogBack LB = new LogBack();

    private String channel = "TwitchFollower";
    private String id = "twitchcount";

    public void timer(Guild guild) {
        Runnable runnable = () -> {
            try {
                TimeUnit.MINUTES.sleep(30);
                TwitchFollower(guild);
            } catch (Exception e) {
                LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
            }
        };
        new Thread(runnable).start();
    }

    public void TwitchFollower(Guild guild) {
        try {
            if (PropertiesFile.readsPropertiesFile(">twitchcount_on").equals("true") && PropertiesFile.readsPropertiesFile(">streamcategory_on").equals("true")) {
                URL url = new URL("https://api.crunchprank.net/twitch/followcount/" + PropertiesFile.readsPropertiesFile("twitchname"));
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                if ((line = in.readLine()) != null) {
                /*
                check category
                 */
                    CheckCategory C_Category = new CheckCategory();
                    C_Category.checkingCategory(guild, "streamcategory");
                /*
                check channel
                 */
                    CheckChannel C_CheckChannel = new CheckChannel();
                    C_CheckChannel.checkingChannel(guild, id);
                /*
                size
                 */
                    int size = Integer.parseInt(line);
                /*
                go to counter
                 */
                    Counter c = new Counter();
                    c.change(guild, channel, id, size);
                    timer(guild);
                }
            }
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        } catch (NumberFormatException e) {
            LB.log(Thread.currentThread().getName(), "No Twitchname set, can be ignored", "warn");
        }
    }
}