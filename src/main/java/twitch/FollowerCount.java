package twitch;

import config.PropertiesFile;
import count._main_Counter;
import net.dv8tion.jda.api.entities.Guild;
import other._guild.Members;
import other._guild.check.CheckCategory;
import other._guild.check.CheckChannel;
import other._stuff.LogBack;

import java.util.concurrent.TimeUnit;

public class FollowerCount {

    public int twitch_timer = 5;
    LogBack LB = new LogBack();

    public void timer_twitch_member(Guild guild) {
        Runnable runnable = () -> {
            try {
                /*
                check for videos
                */
                Videos V = new Videos();
                V.VideoMessage(guild);
                /*
                check for clips
                */
                Clip C = new Clip();
                C.ClipMessage(guild);

                TimeUnit.MINUTES.sleep(twitch_timer);

                /*
                trigger this, do check for nick- / namechange
                */
                Members m = new Members();
                m.Member_CheckMemberOnFile(guild);

                if (PropertiesFile.readsPropertiesFile(">twitchcount_on", "config").equals("true") && PropertiesFile.readsPropertiesFile(">streamcategory_on", "config").equals("true")) {
                    /*
                    Check Category
                    */
                    CheckCategory C_Category = new CheckCategory();
                    C_Category.checkingCategory(guild, "streamcategory");
                    /*
                    Check Channel
                    */
                    CheckChannel C_CheckChannel = new CheckChannel();
                    C_CheckChannel.checkingChannel(guild, "twitchcount");
                    /*
                    Do the Counter
                    */
                    _main_Counter c = new _main_Counter();
                    c.getint(guild, "twitchcount");
                }
                timer_twitch_member(guild);
            } catch (NumberFormatException e) {
                LB.log(Thread.currentThread().getName(), "No Twitchname set, can be ignored (if not needed)", "warn");
            } catch (InterruptedException e) {
                LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
            }
        };
        new Thread(runnable).start();
    }

    public int getFollowerCount(String userid) {

        MakeRequest r = new MakeRequest();
        String[] list = r.doRequest("channels/" + userid);

        int count = 0;
        for (String string : list) {
            if (string.contains("followers")) {
                count = Integer.parseInt(string.substring(12));
            }
        }
        return count;
    }
}