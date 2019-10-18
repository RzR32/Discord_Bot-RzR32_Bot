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
import java.util.HashMap;

public class GamePlayingCount {

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
        CountOfflineMember(guild);
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
        channel.sendMessage(builder.setTitle(guild.getJDA().getPresence().getActivity().getName()).setDescription("**1** Member (__Bot__)").setFooter(guild.getJDA().getSelfUser().getName(),
                "https://cdn.discordapp.com/avatars/391244025015042049/5644aad11ff3d68620bfe939bee6323d.png").build()).complete();
    }

    /*
    for each member, that is no activity´s > count
     */
    public void CountNotPlayingGames(Guild guild) {
        try {
            EmbedBuilder builder = new EmbedBuilder().setColor(new Color(255, 255, 255)).setTitle("Online").setDescription("");
            int x = 0;  /*if member is not offline AND has no activity´s*/
            int y = 0;  /*if member is not offline IGNORE activity´s*/
            TextChannel channel = guild.getTextChannelById(channel_id);
            for (Message message : channel.getIterableHistory()) {
                if (message.getEmbeds().get(0).getTitle().equalsIgnoreCase("Online")) {
                    for (Member member : guild.getMembers()) {
                        if (member.getOnlineStatus() != OnlineStatus.OFFLINE) {
                            y++;
                        }
                        if (member.getOnlineStatus() != OnlineStatus.OFFLINE && member.getActivities().isEmpty()) {
                            x++;
                        }
                    }
                    if (y == 0) {
                        message.delete().complete();
                    } else if (y == 1) {
                        builder.getDescriptionBuilder().append("**" + y + "** Member, ist Online\n");
                    } else {
                        builder.getDescriptionBuilder().append("**" + y + "** Member, sind Online\n");
                    }
                    if (x == 0) {
                        message.delete().complete();
                    } else if (x == 1) {
                        builder.getDescriptionBuilder().append("**" + x + "** Member, macht gerade nichts\n");
                    } else {
                        builder.getDescriptionBuilder().append("**" + x + "** Member, machen gerade nichts\n");
                    }
                    int z =  y - x;
                    if (z == 0) {
                    } else if (z == 1) {
                        builder.getDescriptionBuilder().append("**" + z + "** Member, macht gerade etwas");
                    } else if (z > 1) {
                        builder.getDescriptionBuilder().append("**" + z + "** Member, machen gerade etwas");
                    }
                    message.editMessage(builder.build()).complete();
                    return;
                }
            }
            for (Member member : guild.getMembers()) {
                if (member.getOnlineStatus() != OnlineStatus.OFFLINE) {
                    y++;
                }
                if (member.getOnlineStatus() != OnlineStatus.OFFLINE && member.getActivities().isEmpty()) {
                    x++;
                }
            }
            if (y == 0) {
            } else if (y == 1) {
                builder.getDescriptionBuilder().append("**" + y + "** Member, ist Online\n");
            } else {
                builder.getDescriptionBuilder().append("**" + y + "** Member, sind Online\n");
            }
            if (x == 0) {
            } else if (x == 1) {
                builder.getDescriptionBuilder().append("**" + x + "** Member, macht gerade nichts\n");
            } else {
                builder.getDescriptionBuilder().append("**" + x + "** Member, machen gerade nichts\n");
            }
            int z =  y - x;
            if (z == 0) {
            } else if (z == 1) {
                builder.getDescriptionBuilder().append("**" + z + "** Member, macht gerade etwas");
            } else if (z > 1) {
                builder.getDescriptionBuilder().append("**" + z + "** Member, machen gerade etwas");
            }
            channel.sendMessage(builder.build()).complete();
        } catch (ErrorResponseException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }

    /*
    for each member that is offline > count
     */
    public void CountOfflineMember(Guild guild) {
        try {
            EmbedBuilder builder = new EmbedBuilder().setColor(new Color(255, 0, 0)).setTitle("Offline");
            int x = 0;
            TextChannel channel = guild.getTextChannelById(channel_id);
            for (Message message : channel.getIterableHistory()) {
                if (message.getEmbeds().get(0).getTitle().equalsIgnoreCase("Offline")) {
                    for (Member member : guild.getMembers()) {
                        if (member.getOnlineStatus() == OnlineStatus.OFFLINE) {
                            x++;
                        }
                    }
                    if (x == 0) {
                        message.delete().complete();
                    } else if (x == 1) {
                        message.editMessage(builder.setDescription("**" + x + "** Member, ist Offline").build()).complete();
                    } else {
                        message.editMessage(builder.setDescription("**" + x + "** Member, sind Offline").build()).complete();
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
                channel.sendMessage(builder.setDescription("**" + x + "** Member, ist Offline").build()).complete();
            } else {
                channel.sendMessage(builder.setDescription("**" + x + "** Member, sind Offline").build()).complete();
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
                    for (Activity activity : member.getActivities()) {
                        ForwardPlayingGame(guild, activity);
                    }
                }
            }
        }
    }

    public void ForwardPlayingGame(Guild guild, Activity activity) {
        if (PropertiesFile.readsPropertiesFile(">playingcount_on").equals("true") && PropertiesFile.readsPropertiesFile(">gamecategory_on").equals("true")) {
            Color color = null;
            String Color = null;
            boolean bool_check_message = false;
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
            }
            /*get messages | if found*/
            for (Message message : guild.getTextChannelById(channel_id).getIterableHistory()) {
                if (message.getEmbeds().get(0).getTitle().equalsIgnoreCase("Offline")) {
                    bool_check_message = true;
                }
            }
            if (bool_check_message) {
                for (Message message : guild.getTextChannelById(channel_id).getIterableHistory()) {
                    if (Color.equals("stream")) {
                        bool_title = message.getEmbeds().get(0).getTitle().equalsIgnoreCase("Stream");
                    } else {
                        bool_title = message.getEmbeds().get(0).getTitle().equalsIgnoreCase(activity.getName());
                    }
                    if (bool_title) {
                        _Message(guild, activity, color, Color, message);
                        return;
                    }
                }
                _Message(guild, activity, color, Color, null);
            } else {
                ForwardPlayingGame(guild, activity);
            }
        }
    }

    private void _Message(Guild guild, Activity A_game, Color color, String Color, Message message) {
        int x_count = 0;
        /*list for detail - string and int*/
        HashMap<Integer, String> list_string_detail = new HashMap<>();
        list_string_detail.put(0, null);
        HashMap<Integer, Integer> list_integer_detail = new HashMap<>();
        list_integer_detail.put(0, 0);
        /*list for state - string and int*/
        HashMap<Integer, String> list_string_state = new HashMap<>();
        list_string_state.put(0, null);
        HashMap<Integer, Integer> list_integer_state = new HashMap<>();
        list_integer_state.put(0, 0);
        /*list for largeimg - string and int*/
        HashMap<Integer, String> list_string_largeimg = new HashMap<>();
        list_string_largeimg.put(0, null);
        HashMap<Integer, Integer> list_integer_largeimg = new HashMap<>();
        list_integer_largeimg.put(0, 0);
        /*setup builder to send/edit message*/
        TextChannel channel = guild.getTextChannelById(channel_id);
        EmbedBuilder builder = new EmbedBuilder().setColor(color);
        if (Color.equals("stream")) {
            builder.setTitle("Stream");
        } else {
            builder.setTitle(A_game.getName());
        }
        /*get int / count */
        for (Member member : guild.getMembers()) {
            /*if member is not offline and not a bot*/
            if (member.getOnlineStatus() != OnlineStatus.OFFLINE && !member.getUser().isBot()) {
                /*if member has activity´s*/
                if (!member.getActivities().isEmpty()) {
                    for (Activity activity : member.getActivities()) {
                        /*if game == activity | count*/
                        boolean bool;
                        if (Color.equals("stream")) {
                            bool = activity.getType() == Activity.ActivityType.STREAMING;
                        } else {
                            bool = activity.getName().equals(A_game.getName());
                        }
                        if (bool) {
                            x_count++;
                            if (activity.asRichPresence() != null) {
                                /*count | detail*/
                                try {
                                    if (activity.asRichPresence().getDetails() != null) {
                                        for (int a = 0; a <= list_string_detail.size(); a++) {
                                            if (activity.asRichPresence().getDetails().equals(list_string_detail.get(a))) {
                                                int b = list_integer_detail.get(a) + 1;
                                                list_integer_detail.put(a, b);
                                                break;
                                            }
                                            if (a == list_string_detail.size()) {
                                                if (list_string_detail.get(0) == null) {
                                                    a = 0;
                                                }
                                                list_string_detail.put(a, activity.asRichPresence().getDetails());
                                                list_integer_detail.put(a, 1);
                                                break;
                                            }
                                        }
                                    }
                                } catch (NullPointerException ignored) {
                                }
                                /*detail | state*/
                                try {
                                    if (activity.asRichPresence().getState() != null) {
                                        for (int a = 0; a <= list_string_state.size(); a++) {
                                            if (activity.asRichPresence().getState().equals(list_string_state.get(a))) {
                                                int b = list_integer_state.get(a) + 1;
                                                list_integer_state.put(a, b);
                                                break;
                                            }
                                            if (a == list_string_state.size()) {
                                                if (list_string_state.get(0) == null) {
                                                    a = 0;
                                                }
                                                list_string_state.put(a, activity.asRichPresence().getState());
                                                list_integer_state.put(a, 1);
                                                break;
                                            }
                                        }
                                    }
                                } catch (NullPointerException ignored) {
                                }
                                /*state | largeimg*/
                                try {
                                    if (activity.asRichPresence().getLargeImage().getText() != null) {
                                        for (int a = 0; a <= list_string_largeimg.size(); a++) {
                                            if (activity.asRichPresence().getLargeImage().getText().equals(list_string_largeimg.get(a))) {
                                                int b = list_integer_largeimg.get(a) + 1;
                                                list_integer_largeimg.put(a, b);
                                                break;
                                            }
                                            if (a == list_string_largeimg.size()) {
                                                if (list_string_largeimg.get(0) == null) {
                                                    a = 0;
                                                }
                                                list_string_largeimg.put(a, activity.asRichPresence().getLargeImage().getText());
                                                list_integer_largeimg.put(a, 1);
                                                break;
                                            }
                                        }
                                    }
                                } catch (NullPointerException ignored) {
                                }
                            }
                        }
                    }
                }
            }
        }
        /*setup message | for one or more - member*/
        if (x_count == 1) {
            if (Color.equals("default")) {
                builder.setDescription("**" + x_count + "** Member, spielt gerade dieses Spiel");
            } else if (Color.equals("stream")) {
                builder.setDescription("**" + x_count + "** Member, streamt");
            } else if (Color.equals("listen")) {
                builder.setDescription("**" + x_count + "** Member, hört Musik");
            } else if (Color.equals("watch")) {
                builder.setDescription("**" + x_count + "** Member, schaut ein Film");
            }
        } else if (x_count > 1) {
            if (Color.equals("default")) {
                builder.setDescription("**" + x_count + "** Member, spielen gerade dieses Spiel");
            } else if (Color.equals("stream")) {
                builder.setDescription("**" + x_count + "** Member, streamen");
            } else if (Color.equals("listen")) {
                builder.setDescription("**" + x_count + "** Member, hören Musik");
            } else if (Color.equals("watch")) {
                builder.setDescription("**" + x_count + "** Member, schauen Filme");
            }
        }
        /*add everything from the list to the builder*/
        if (Color.equals("default") || Color.equals("stream")) {
            builder.getDescriptionBuilder().append("\n");
            if (list_string_detail.get(0) != null && list_integer_detail.get(0) != null && !list_string_detail.toString().contains("null")) {
                builder.getDescriptionBuilder().append("\n");
                for (int _int = 0; _int < list_string_detail.size(); _int++) {
                    builder.getDescriptionBuilder().append("**" + list_integer_detail.get(_int) + "** - " + list_string_detail.get(_int) + "\n");
                }
            }
            if (list_string_state.size() != 0 && list_integer_state.get(0) != null && !list_string_state.toString().contains("null")) {
                builder.getDescriptionBuilder().append("\n");
                for (int _int = 0; _int < list_string_state.size(); _int++) {
                    builder.getDescriptionBuilder().append("**" + list_integer_state.get(_int) + "** - " + list_string_state.get(_int) + "\n");
                }
            }
            if (list_string_largeimg.size() != 0 && list_integer_largeimg.get(0) != null && !list_string_largeimg.toString().contains("null")) {
                builder.getDescriptionBuilder().append("\n");
                for (int _int = 0; _int < list_string_largeimg.size(); _int++) {
                    builder.getDescriptionBuilder().append("**" + list_integer_largeimg.get(_int) + "** - " + list_string_largeimg.get(_int) + "\n");
                }
            }
        }
        /*send/edit message*/
        if (message == null) {
            if (x_count != 0) {
                channel.sendMessage(builder.build()).complete();
            }
        } else {
            if (x_count == 0) {
                message.delete().queue();
            } else {
                message.editMessage(builder.build()).complete();
            }
        }
        CountNotPlayingGames(guild);
        list_string_detail.clear();
        list_integer_detail.clear();
        list_string_state.clear();
        list_integer_state.clear();
        list_string_largeimg.clear();
        list_integer_largeimg.clear();
    }
}