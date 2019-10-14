package count;

import check_create.CheckCategory;
import check_create.CheckChannel;
import config.PropertiesFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities .*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import other.ConsoleColor;
import other.LogBack;

import java.awt .*;
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
            EmbedBuilder builder = new EmbedBuilder().setColor(new Color(255, 255, 255)).setTitle("Online");
            int x = 0;
            TextChannel channel = guild.getTextChannelById(channel_id);
            for (Message message : channel.getIterableHistory()) {
                if (message.getEmbeds().get(0).getTitle().equalsIgnoreCase("Online")) {
                    for (Member member : guild.getMembers()) {
                        if (member.getOnlineStatus() != OnlineStatus.OFFLINE && member.getActivities().isEmpty()) {
                            x++;
                        }
                    }
                    if (x == 0) {
                        message.delete().complete();
                    } else if (x == 1) {
                        message.editMessage(builder.setDescription("**" + x + "** Member, macht gerade nichts").build()).complete();
                    } else {
                        message.editMessage(builder.setDescription("**" + x + "** Member, machen gerade nichts").build()).complete();
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
                channel.sendMessage(builder.setDescription("**" + x + "** Member, macht gerade nichts").build()).complete();
            } else {
                channel.sendMessage(builder.setDescription("**" + x + "** Member, machen gerade nichts").build()).complete();
            }
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
            if (activity.getType().equals(Activity.ActivityType.STREAMING)) {
                GetMessage(guild, activity, "stream");
            } else if (activity.getType().equals(Activity.ActivityType.LISTENING)) {
                GetMessage(guild, activity, "listen");
            } else if (activity.getType().equals(Activity.ActivityType.WATCHING)) {
                GetMessage(guild, activity, "watch");
            } else if (activity.getType().equals(Activity.ActivityType.DEFAULT)) {
                GetMessage(guild, activity, "default");
            }
            CountNotPlayingGames(guild);
        }
    }

    private void GetMessage(Guild guild, Activity A_game, String Color) {
        /*set color´s*/
        Color color = null;
        if (Color.equals("stream")) {
            color = new Color(255, 0, 255);
        } else if (Color.equals("listen")) {
            color = new Color(0, 255, 0);
        } else if (Color.equals("watch")) {
            color = new Color(255, 255, 0);
        } else if (Color.equals("default")) {
            color = new Color(0, 240, 255);
        }
        /*get messages | if found*/
        for (Message message : guild.getTextChannelById(channel_id).getIterableHistory()) {
            if (message.getEmbeds().get(0).getTitle().equalsIgnoreCase(A_game.getName())) {
                _Message(guild, A_game, color, Color, message);
                return;
            }
        }
        _Message(guild, A_game, color, Color, null);
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
        EmbedBuilder builder;
        if (Color.equals("stream")) {
            builder = new EmbedBuilder().setTitle("Stream").setColor(color);
        } else {
            builder = new EmbedBuilder().setTitle(A_game.getName()).setColor(color);
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
        list_string_detail.clear();
        list_integer_detail.clear();
        list_string_state.clear();
        list_integer_state.clear();
        list_string_largeimg.clear();
        list_integer_largeimg.clear();
    }
}