package count.GamePlayingCount;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.awt.*;

public class CountNotPlayingGames {
    /*
    for each member, that is no activity´s > count
    */
    public static void _message(Guild guild) {
        if (!_main_GamePlayingCount.b_deleting) {
            try {
                EmbedBuilder builder = new EmbedBuilder().setColor(new Color(0, 0, 255)).setTitle("Online").setDescription("");
                int online_without_act = 0;  /*if member is not offline AND has no activity´s*/
                int online_with_act = 0;  /*if member is not offline IGNORE activity´s*/

                int playing = 0;
                int streaming = 0;
                int watching = 0;
                int listening = 0;
                int custom_status = 0;

                int online = 0;
                int idle = 0;
                int dnd = 0;
                TextChannel channel = guild.getTextChannelById(_main_GamePlayingCount.channel_id);

                /*count*/
                for (Member member : guild.getMembers()) {
                    for (Activity activity : member.getActivities()) {
                        if (activity.getType().equals(Activity.ActivityType.DEFAULT)) {
                            playing++;
                        }
                        if (activity.getType().equals(Activity.ActivityType.STREAMING)) {
                            streaming++;
                        }
                        if (activity.getType().equals(Activity.ActivityType.WATCHING)) {
                            watching++;
                        }
                        if (activity.getType().equals(Activity.ActivityType.LISTENING)) {
                            listening++;
                        }
                        if (activity.getType().equals(Activity.ActivityType.CUSTOM_STATUS)) {
                            custom_status++;
                        }
                    }


                    if (member.getOnlineStatus() != OnlineStatus.OFFLINE) {
                        online_with_act++;
                    }
                    if (member.getOnlineStatus() == OnlineStatus.ONLINE) {
                        online++;
                    }
                    if (member.getOnlineStatus() == OnlineStatus.IDLE) {
                        idle++;
                    }
                    if (member.getOnlineStatus() == OnlineStatus.DO_NOT_DISTURB) {
                        dnd++;
                    }
                    if (member.getOnlineStatus() != OnlineStatus.OFFLINE && member.getActivities().isEmpty()) {
                        online_without_act++;
                    }
                }

                if (online_with_act == 0) {
                    /*should not be triggered, cause the bot also count as 1*/
                    builder.getDescriptionBuilder().append("kein Member ist Online\n");
                } else if (online_with_act == 1) {
                    builder.getDescriptionBuilder().append("**").append(online_with_act).append("** Member, ist Online\n");
                } else {
                    builder.getDescriptionBuilder().append("**").append(online_with_act).append("** Member, sind Online\n");
                }

                if (online_without_act == 0) {
                    builder.getDescriptionBuilder().append("Niemand der Online ist, macht etwas\n");
                } else if (online_without_act == 1) {
                    builder.getDescriptionBuilder().append("**").append(online_without_act).append("** Member, macht gerade nichts\n");
                } else {
                    builder.getDescriptionBuilder().append("**").append(online_without_act).append("** Member, machen gerade nichts\n");
                }

                int z = online_with_act - online_without_act;
                if (z == 0) {
                    builder.getDescriptionBuilder().append("Alle die Online sind, machen etwas\n");
                } else if (z == 1) {
                    builder.getDescriptionBuilder().append("**").append(z).append("** Member, macht gerade etwas\n");
                } else if (z > 1) {
                    builder.getDescriptionBuilder().append("**").append(z).append("** Member, machen gerade etwas\n");
                }

                builder.getDescriptionBuilder().append("-+-+-+-+-+-+-+-+-+-+-\n");

                if (online == 0) {
                    builder.getDescriptionBuilder().append("**").append(online).append("** Member, sind davon Online\n");
                } else if (online == 1) {
                    builder.getDescriptionBuilder().append("**").append(online).append("** Member, ist davon Online\n");
                } else if (online > 1) {
                    builder.getDescriptionBuilder().append("**").append(online).append("** Member, sind davon Online\n");
                }

                if (idle == 0) {
                    builder.getDescriptionBuilder().append("**").append(idle).append("** Member, sind davon Idle\n");
                } else if (idle == 1) {
                    builder.getDescriptionBuilder().append("**").append(idle).append("** Member, ist davon Idle\n");
                } else if (idle > 1) {
                    builder.getDescriptionBuilder().append("**").append(idle).append("** Member, sind davon Idle\n");
                }

                if (dnd == 0) {
                    builder.getDescriptionBuilder().append("**").append(dnd).append("** Member, sind davon DnD\n");
                } else if (dnd == 1) {
                    builder.getDescriptionBuilder().append("**").append(dnd).append("** Member, ist davon DnD\n");
                } else if (dnd > 1) {
                    builder.getDescriptionBuilder().append("**").append(dnd).append("** Member, sind davon DnD\n");
                }

                builder.getDescriptionBuilder().append("-+-+-+-+-+-+-+-+-+-+-\n");

                if (playing == 0) {
                    builder.getDescriptionBuilder().append("**").append(playing).append("** Member davon, spielen\n");
                } else if (playing == 1) {
                    builder.getDescriptionBuilder().append("**").append(playing).append("** Member davon, spielt\n");
                } else if (playing > 1) {
                    builder.getDescriptionBuilder().append("**").append(playing).append("** Member davon, spielen\n");
                }

                if (streaming == 0) {
                    builder.getDescriptionBuilder().append("**").append(streaming).append("** Member davon, streamen\n");
                } else if (streaming == 1) {
                    builder.getDescriptionBuilder().append("**").append(streaming).append("** Member davon, streamt\n");
                } else if (streaming > 1) {
                    builder.getDescriptionBuilder().append("**").append(streaming).append("** Member davon, streamen\n");
                }

                if (watching == 0) {
                    builder.getDescriptionBuilder().append("**").append(watching).append("** Member davon, schauen\n");
                } else if (watching == 1) {
                    builder.getDescriptionBuilder().append("**").append(watching).append("** Member davon, schaut\n");
                } else if (watching > 1) {
                    builder.getDescriptionBuilder().append("**").append(watching).append("** Member davon, schauen\n");
                }

                if (listening == 0) {
                    builder.getDescriptionBuilder().append("**").append(listening).append("** Member davon, h\u00f6ren\n");
                } else if (listening == 1) {
                    builder.getDescriptionBuilder().append("**").append(listening).append("** Member davon, h\u00f6rt\n");
                } else if (listening > 1) {
                    builder.getDescriptionBuilder().append("**").append(listening).append("** Member davon, h\u00f6ren\n");
                }

                if (custom_status == 0) {
                    builder.getDescriptionBuilder().append("**").append(custom_status).append("** Member davon, haben Custom Status\n");
                } else if (custom_status == 1) {
                    builder.getDescriptionBuilder().append("**").append(custom_status).append("** Member davon, hat Custom Status\n");
                } else if (custom_status > 1) {
                    builder.getDescriptionBuilder().append("**").append(custom_status).append("** Member davon, haben Custom Status\n");
                }

                /*check if found*/
                for (Message message : channel.getIterableHistory()) {
                    if (message.getEmbeds().get(0).getTitle().equalsIgnoreCase("Online")) {

                        try {
                            message.editMessage(builder.build()).complete();
                        } catch (ErrorResponseException e_response) {
                            return;
                        }
                        return;
                    }
                }

                try {
                    channel.sendMessage(builder.build()).complete();
                } catch (ErrorResponseException ignored) {
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