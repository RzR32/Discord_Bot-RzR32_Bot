package count.GamePlayingCount;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.awt.*;

public class CountOfflineMember {
    /*
    for each member that is offline > count
    */
    public static void _message(Guild guild) {
        if (!_main_GamePlayingCount.b_deleting) {
            try {
                EmbedBuilder builder = new EmbedBuilder().setColor(new Color(255, 0, 0)).setTitle("Offline");
                int x = 0;
                TextChannel channel = guild.getTextChannelById(_main_GamePlayingCount.channel_id);
                for (Message message : channel.getIterableHistory()) {
                    if (message.getEmbeds().get(0).getTitle().equalsIgnoreCase("Offline")) {
                        for (Member member : guild.getMembers()) {
                            if (member.getOnlineStatus() == OnlineStatus.OFFLINE) {
                                x++;
                            }
                        }
                        if (x == 0) {
                            try {
                                message.delete().complete();
                            } catch (ErrorResponseException e_response) {
                                return;
                            }
                        } else if (x == 1) {
                            try {
                                message.editMessage(builder.setDescription("**" + x + "** Member, ist Offline").build()).complete();
                            } catch (ErrorResponseException e_response) {
                                return;
                            }
                        } else {
                            try {
                                message.editMessage(builder.setDescription("**" + x + "** Member, sind Offline").build()).complete();
                            } catch (ErrorResponseException e_response) {
                                return;
                            }
                        }
                        return;
                    }
                }
                for (Member member : guild.getMembers()) {
                    if (member.getOnlineStatus() == OnlineStatus.OFFLINE) {
                        x++;
                    }
                }
                if (x == 0) {
                } else if (x == 1) {
                    try {
                        channel.sendMessage(builder.setDescription("**" + x + "** Member, ist Offline").build()).complete();
                    } catch (ErrorResponseException ignored) {
                    }
                } else {
                    try {
                        channel.sendMessage(builder.setDescription("**" + x + "** Member, sind Offline").build()).complete();
                    } catch (ErrorResponseException ignored) {
                    }
                }
            } catch (ErrorResponseException e) {
                _main_GamePlayingCount.LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
            }
        } else {
            _main_GamePlayingCount.P.pause(10000);
            _message(guild);
        }
    }
}