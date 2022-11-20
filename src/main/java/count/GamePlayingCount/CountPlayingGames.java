package count.GamePlayingCount;

import listener.member._Activity;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.util.ArrayList;

public class CountPlayingGames {
    /*
    for each member get the GAME/STREAM/LISTEN/WATCH > count
    */
    public static void _message(Guild guild) {
        /*
        if the three default message was "found", do the code
        */
        if (!_main_GamePlayingCount.b_deleting) {
            for (Member member : guild.getMembers()) {
                if (!member.getActivities().isEmpty()) {
                    if (member.getOnlineStatus() != OnlineStatus.OFFLINE && !member.getUser().isBot()) {
                        for (Activity activity : member.getActivities()) {
                            _Activity GfM = new _Activity();
                            GfM.Forwarded(guild, "start", member, activity);
                        }
                    }
                }
            }
            checkforDoubleMessage(guild.getTextChannelById(_main_GamePlayingCount.channel_id));
        } else {
            _main_GamePlayingCount.P.pause(10000);
            _message(guild);
        }
    }

    private static void checkforDoubleMessage(TextChannel channel) {
        try {
            ArrayList<Message> list = new ArrayList<>();
            for (Message message : channel.getIterableHistory()) {
                String message_title = message.getEmbeds().get(0).getTitle();
                if (list.toString().contains(message_title)) {
                    message.delete().queue();
                } else {
                    list.add(message);
                }
            }
        } catch (ErrorResponseException ignored) {
        }
    }
}