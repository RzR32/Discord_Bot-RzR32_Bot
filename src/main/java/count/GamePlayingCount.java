package count;

import check_create.CheckCategory;
import check_create.CheckChannel;
import config.PropertiesFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import other.ConsoleColor;
import other.LogBack;

import java.awt.*;

public class GamePlayingCount extends Thread {

    LogBack LB = new LogBack();

    private String channel_id = PropertiesFile.readsPropertiesFile("playingcount");

    CheckCategory C_Category = new CheckCategory();
    CheckChannel C_Channel = new CheckChannel();

    public void startCounter(Guild guild) {
        if (PropertiesFile.readsPropertiesFile(">playingcount_on").equals("true") && PropertiesFile.readsPropertiesFile(">gamecategory_on").equals("true")) {
            Runnable runnable = () -> DeleteMessages(guild);
            new Thread(runnable).start();
        }
    }

    public void ForwardPlayingGame(Guild guild, String game, Activity.ActivityType Act_Type) {
        if (PropertiesFile.readsPropertiesFile(">playingcount_on").equals("true") && PropertiesFile.readsPropertiesFile(">gamecategory_on").equals("true")) {
            if (Act_Type.equals(Activity.ActivityType.STREAMING)) {
                CountGames(guild, "Stream", "stream");
            } else if (Act_Type.equals(Activity.ActivityType.LISTENING)) {
                CountGames(guild, game, "listen");
            } else if (Act_Type.equals(Activity.ActivityType.WATCHING)) {
                CountGames(guild, game, "watch");
            } else if (Act_Type.equals(Activity.ActivityType.DEFAULT)) {
                CountGames(guild, game, "default");
            }
            CountNotPlayingGames(guild);
        }
    }

    private void CountGames(Guild guild, String game, String Color) {
        EmbedBuilder builder = new EmbedBuilder();
        int x = 0;
        TextChannel channel = guild.getTextChannelById(channel_id);
        if (Color.equals("stream")) {
            builder.setColor(new Color(255, 0, 255));
        } else if (Color.equals("listen")) {
            builder.setColor(new Color(0, 255, 0));
        } else if (Color.equals("watch")) {
            builder.setColor(new Color(255, 255, 0));
        } else if (Color.equals("default")) {
            builder.setColor(new Color(0, 240, 255));
        }
        for (Message message : channel.getIterableHistory()) {
            if (message.getEmbeds().get(0).getTitle().equalsIgnoreCase(game)) {
                for (Member member : guild.getMembers()) {
                    if (!member.getActivities().isEmpty()) {
                        if (member.getOnlineStatus() != OnlineStatus.OFFLINE) {
                            if (Color.equals("stream")) {
                                if (member.getActivities().get(0).getType() == Activity.ActivityType.STREAMING) {
                                    x++;
                                }
                            } else {
                                if (member.getActivities().get(0).getName().equals(game)) {
                                    x++;
                                }
                            }
                        }
                    }
                }
                if (x <= 0) {
                    message.delete().complete();
                } else if (x == 1) {
                    if (Color.equals("default")) {
                        message.editMessage(builder.setTitle(game).setDescription("**" + x + "** Member, spielt gerade dieses Spiel.").build()).complete();
                    } else if (Color.equals("listen")) {
                        message.editMessage(builder.setTitle(game).setDescription("**" + x + "** Member, hört Musik").build()).complete();
                    } else if (Color.equals("watch")) {
                        message.editMessage(builder.setTitle(game).setDescription("**" + x + "** Member, schaut ein Film").build()).complete();
                    } else if (Color.equals("stream")) {
                        message.editMessage(builder.setTitle("Stream").setDescription("**" + x + "** Member, streamt").build()).complete();
                    }
                } else {
                    if (Color.equals("default")) {
                        message.editMessage(builder.setTitle(game).setDescription("**" + x + "** Member, spielen gerade dieses Spiel.").build()).complete();
                    } else if (Color.equals("listen")) {
                        message.editMessage(builder.setTitle(game).setDescription("**" + x + "** Member, hören Musik").build()).complete();
                    } else if (Color.equals("watch")) {
                        message.editMessage(builder.setTitle(game).setDescription("**" + x + "** Member, schauen Filme").build()).complete();
                    } else if (Color.equals("stream")) {
                        message.editMessage(builder.setTitle("Stream").setDescription("**" + x + "** Member, streamen").build()).complete();
                    }
                }
                return;
            }
        }
        for (Member member : guild.getMembers()) {
            if (!member.getActivities().isEmpty()) {
                if (member.getOnlineStatus() != OnlineStatus.OFFLINE && member.getActivities().get(0) != null) {
                    if (Color.equals("stream")) {
                        if (member.getActivities().get(0).getType() == Activity.ActivityType.STREAMING) {
                            x++;
                        }
                    } else {
                        if (member.getActivities().get(0).getName().equals(game)) {
                            x++;
                        }
                    }
                }
            }
        }
        if (x <= 0) {
        } else if (x == 1) {
            if (Color.equals("default")) {
                channel.sendMessage(builder.setTitle(game).setDescription("**" + x + "** Member, spielt gerade dieses Spiel.").build()).complete();
            } else if (Color.equals("listen")) {
                channel.sendMessage(builder.setTitle(game).setDescription("**" + x + "** Member, hört Musik").build()).complete();
            } else if (Color.equals("watch")) {
                channel.sendMessage(builder.setTitle(game).setDescription("**" + x + "** Member, schaut ein Film").build()).complete();
            } else if (Color.equals("stream")) {
                channel.sendMessage(builder.setTitle("Stream").setDescription("**" + x + "** Member, streamt").build()).complete();
            }
        } else {
            if (Color.equals("default")) {
                channel.sendMessage(builder.setTitle(game).setDescription("**" + x + "** Member, spielen gerade dieses Spiel.").build()).complete();
            } else if (Color.equals("listen")) {
                channel.sendMessage(builder.setTitle(game).setDescription("**" + x + "** Member, hören Musik").build()).complete();
            } else if (Color.equals("watch")) {
                channel.sendMessage(builder.setTitle(game).setDescription("**" + x + "** Member, schauen Filme").build()).complete();
            } else if (Color.equals("stream")) {
                channel.sendMessage(builder.setTitle("Stream").setDescription("**" + x + "** Member, streamen").build()).complete();
            }
        }
    }

    /*
    for each member, that is not playing, streaming ... etc > count
     */
    public void CountNotPlayingGames(Guild guild) {
        try {
            EmbedBuilder builder = new EmbedBuilder().setColor(new Color(255, 255, 255));
            int x = 0;
            TextChannel channel = guild.getTextChannelById(channel_id);
            for (Message message : channel.getIterableHistory()) {
                if (message.getEmbeds().get(0).getTitle().equalsIgnoreCase("Not Ingame")) {
                    for (Member member : guild.getMembers()) {
                        if (member.getOnlineStatus() != OnlineStatus.OFFLINE && member.getActivities().isEmpty()) {
                            x++;
                        }
                    }
                    if (x == 0) {
                        message.delete().complete();
                    } else if (x == 1) {
                        message.editMessage(builder.setTitle("Not Ingame").setDescription("**" + x + "** Member, macht gerade nichts.").build()).complete();
                    } else {
                        message.editMessage(builder.setTitle("Not Ingame").setDescription("**" + x + "** Member, machen gerade nichts.").build()).complete();
                    }
                    return;
                }
            }
            for (Member member : guild.getMembers()) {
                if (member.getOnlineStatus() != OnlineStatus.OFFLINE && member.getActivities().isEmpty()) {
                    x++;
                }
            }
            if (x == 0) {
            } else if (x == 1) {
                channel.sendMessage(builder.setTitle("Not Ingame").setDescription("**" + x + "** Member, macht gerade nichts.").build()).complete();
            } else {
                channel.sendMessage(builder.setTitle("Not Ingame").setDescription("**" + x + "** Member, machen gerade nichts.").build()).complete();
            }
        } catch (ErrorResponseException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }

    /*
    for each member get the GAME/STREAM/LISTEN/WATCH > count
     */
    private void CountPlayingGames(Guild guild) {
        for (Member member : guild.getMembers()) {
            if (!member.getActivities().isEmpty()) {
                if (member.getOnlineStatus() != OnlineStatus.OFFLINE && !member.getUser().isBot()) {
                    ForwardPlayingGame(guild, member.getActivities().get(0).getName(), member.getActivities().get(0).getType());
                }
            }
        }
    }

    /*
    delete all messages in the channel > then do CountPlayingCount (below)
     */
    private void DeleteMessages(Guild guild) {
        LB.log(Thread.currentThread().getName(), ConsoleColor.backByellow + "GamePlayingCount" + ConsoleColor.reset + " > Delete old Message´s...", "info");
        C_Category.checkingCategory(guild, "gamecategory");
        C_Channel.checkingChannel(guild, "playingcount");
        TextChannel channel = guild.getTextChannelById(PropertiesFile.readsPropertiesFile("playingcount"));
        for (Message message : channel.getIterableHistory()) {
            message.delete().complete();
        }
        LB.log(Thread.currentThread().getName(), ConsoleColor.backByellow + "GamePlayingCount" + ConsoleColor.reset + " > Done, Count Games...", "info");
        MessageBotGame(guild);
        CountNotPlayingGames(guild);
        CountPlayingGames(guild);
        LB.log(Thread.currentThread().getName(), ConsoleColor.backByellow + "GamePlayingCount" + ConsoleColor.reset + " > Finish!", "info");
    }

    /*
    Message for the Bot
     */
    private void MessageBotGame(Guild guild) {
        EmbedBuilder builder = new EmbedBuilder().setColor(new Color(0, 0, 0));
        TextChannel channel = guild.getTextChannelById(channel_id);
        channel.sendMessage(builder.setTitle(guild.getJDA().getPresence().getActivity().getName()).setDescription("**1** Member (__Bot__).").setFooter(guild.getJDA().getSelfUser().getName(),
                "https://cdn.discordapp.com/avatars/391244025015042049/5644aad11ff3d68620bfe939bee6323d.png").build()).complete();
    }
}