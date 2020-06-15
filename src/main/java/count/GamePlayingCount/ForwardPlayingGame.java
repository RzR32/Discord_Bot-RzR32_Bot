package count.GamePlayingCount;

import config.PropertiesFile;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

import java.awt.*;
import java.util.Objects;

public class ForwardPlayingGame {

    public static void _message(Guild guild, Activity activity) {
        if (!_main_GamePlayingCount.b_deleting) {
            try {
                if (PropertiesFile.readsPropertiesFile(">playingcount_on", "config").equals("true") && PropertiesFile.readsPropertiesFile(">gamecategory_on", "config").equals("true")) {
                    Color color;
                    String Color;
                    boolean bool_title;

                    if (activity.getType().equals(Activity.ActivityType.STREAMING)) {
                        Color = "stream";
                        color = new Color(255, 0, 255);
                    } else if (activity.getType().equals(Activity.ActivityType.LISTENING)) {
                        Color = "listen";
                        color = new Color(0, 255, 0);
                    } else if (activity.getType().equals(Activity.ActivityType.WATCHING)) {
                        Color = "watch";
                        color = new Color(255, 255, 0);
                    } else if (activity.getType().equals(Activity.ActivityType.DEFAULT)) {
                        Color = "default";
                        color = new Color(0, 240, 255);
                    } else {
                        Color = "custom";
                        color = new Color(255, 251, 255);
                    }

                    for (Message message : Objects.requireNonNull(guild.getTextChannelById(_main_GamePlayingCount.channel_id)).getIterableHistory()) {
                        if (Color.equals("stream")) {
                            bool_title = message.getEmbeds().get(0).getTitle().equals("Stream");
                        } else {
                            bool_title = message.getEmbeds().get(0).getTitle().equalsIgnoreCase(activity.getName());
                        }
                        if (bool_title) {
                            _Message._message(guild, activity, color, Color, message);
                            return;
                        }
                    }
                    _Message._message(guild, activity, color, Color, null);
                }
            } catch (Exception ignored) {
            }
        } else {
            _main_GamePlayingCount.P.pause(10000);
            _message(guild, activity);
        }
    }
}