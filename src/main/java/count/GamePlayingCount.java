package count;

import check_create.CheckCategory;
import check_create.CheckChannel;
import config.PropertiesFile;
import listener.member.Games_from_Member;
import listener.member.Status_from_Member;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import other.CheckGameOnWebsite;
import other.ConsoleColor;
import other.LogBack;
import other.Pause;

import java.awt.*;
import java.util.HashMap;
import java.util.Objects;

public class GamePlayingCount {

    private final String channel_id = PropertiesFile.readsPropertiesFile("playingcount");
    LogBack LB = new LogBack();
    Pause P = new Pause();
    CheckCategory C_Category = new CheckCategory();
    CheckChannel C_Channel = new CheckChannel();
    private boolean b_deleting = false;

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
        try {
            /*
            remove listener
            */
            if (guild.getJDA().getEventManager().getRegisteredListeners().contains("Games_from_Member")) {
                guild.getJDA().removeEventListener(new Games_from_Member());
            }
            if (guild.getJDA().getEventManager().getRegisteredListeners().contains("Status_from_Member")) {
                guild.getJDA().removeEventListener(new Status_from_Member());
            }
            b_deleting = true;

            C_Category.checkingCategory(guild, "gamecategory");
            C_Channel.checkingChannel(guild, "playingcount");

            LB.log(Thread.currentThread().getName(), ConsoleColor.backByellow + ConsoleColor.black + "GamePlayingCount" + ConsoleColor.reset + " > Delete old Message´s...", "info");

            TextChannel channel = guild.getTextChannelById(PropertiesFile.readsPropertiesFile("playingcount"));
            for (Message message : channel.getIterableHistory()) {
                message.delete().complete();
            }

            P.pause(1000);

            b_deleting = false;

            LB.log(Thread.currentThread().getName(), ConsoleColor.backByellow + ConsoleColor.black + "GamePlayingCount" + ConsoleColor.reset + " > Done, Count Games...", "info");

            MessageBotGame(guild);
            CountOfflineMember(guild);
            CountNotPlayingGames(guild);
            CountPlayingGames(guild);

            /*
            add listener
            */
            if (!guild.getJDA().getEventManager().getRegisteredListeners().contains("Games_from_Member")) {
                guild.getJDA().addEventListener(new Games_from_Member());
            }
            if (!guild.getJDA().getEventManager().getRegisteredListeners().contains("Status_from_Member")) {
                guild.getJDA().addEventListener(new Status_from_Member());
            }
            LB.log(Thread.currentThread().getName(), ConsoleColor.backByellow + ConsoleColor.black + "GamePlayingCount" + ConsoleColor.reset + " > Finish!", "info");
        } catch (ErrorResponseException ignored) {
        }
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
        if (!b_deleting) {
            try {
                EmbedBuilder builder = new EmbedBuilder().setColor(new Color(0, 0, 255)).setTitle("Online").setDescription("");
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
                            /*
                            should not be triggered, cause the bot also count as 1
                            */
                            builder.getDescriptionBuilder().append("kein Member ist Online\n");
                        } else if (y == 1) {
                            builder.getDescriptionBuilder().append("**" + y + "** Member, ist Online\n");
                        } else {
                            builder.getDescriptionBuilder().append("**" + y + "** Member, sind Online\n");
                        }
                        if (x == 0) {
                            builder.getDescriptionBuilder().append("Niemand der Online ist, macht etwas\n");
                        } else if (x == 1) {
                            builder.getDescriptionBuilder().append("**" + x + "** Member, macht gerade nichts\n");
                        } else {
                            builder.getDescriptionBuilder().append("**" + x + "** Member, machen gerade nichts\n");
                        }
                        int z = y - x;
                        if (z == 0) {
                            builder.getDescriptionBuilder().append("Alle die Online sind, machen etwas\n");
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
                int z = y - x;
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
        } else {
            P.pause(10000);
            CountNotPlayingGames(guild);
        }
    }

    /*
    for each member that is offline > count
    */
    public void CountOfflineMember(Guild guild) {
        if (!b_deleting) {
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
        } else {
            P.pause(10000);
            CountOfflineMember(guild);
        }
    }

    /*
    for each member get the GAME/STREAM/LISTEN/WATCH > count
    */
    private void CountPlayingGames(Guild guild) {
        /*
        if the three default message was "found", do the code
        */
        if (!b_deleting) {
            for (Member member : guild.getMembers()) {
                if (!member.getActivities().isEmpty()) {
                    if (member.getOnlineStatus() != OnlineStatus.OFFLINE && !member.getUser().isBot()) {
                        for (Activity activity : member.getActivities()) {
                            ForwardPlayingGame(guild, activity);
                        }
                    }
                }
            }
            checkforDoubleMessage(guild.getTextChannelById(channel_id));

            /*
            check for all active user activity´s
             */
            Games_from_Member GfM = new Games_from_Member();
            GfM.checkAllMembersActivity(guild);
        } else {
            P.pause(10000);
            CountPlayingGames(guild);
        }
    }

    private void checkforDoubleMessage(TextChannel channel) {
        try {
            for (Message message : channel.getIterableHistory()) {
                String message_title = message.getEmbeds().get(0).getTitle();
                int int_message_found_count = 0;
                for (Message message_check_first : channel.getIterableHistory()) {
                    if (message_check_first.getEmbeds().get(0).getTitle().equalsIgnoreCase(message_title)) {
                        int_message_found_count++;
                    }
                }
                /*
                if the message was found more as one time, delete the other!
                */
                if (int_message_found_count > 1) {
                    for (Message message_check_second : channel.getIterableHistory()) {
                        if (int_message_found_count <= 1) {
                            return;
                        }
                        if (message_check_second.getEmbeds().get(0).getTitle().equalsIgnoreCase(message_title)) {
                            try {
                                message_check_second.delete().queue();
                            } catch (ErrorResponseException ignored) {
                            }
                            int_message_found_count--;
                        }
                    }
                }
            }
        } catch (ErrorResponseException ignored) {
        }
    }

    public void ForwardPlayingGame(Guild guild, Activity activity) {
        if (!b_deleting) {
            try {
                if (PropertiesFile.readsPropertiesFile(">playingcount_on").equals("true") && PropertiesFile.readsPropertiesFile(">gamecategory_on").equals("true")) {
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

                    for (Message message : Objects.requireNonNull(guild.getTextChannelById(channel_id)).getIterableHistory()) {
                        if (Color.equals("stream")) {
                            bool_title = message.getEmbeds().get(0).getTitle().equals("Stream");
                        } else {
                            bool_title = message.getEmbeds().get(0).getTitle().equalsIgnoreCase(activity.getName());
                        }
                        if (bool_title) {
                            _Message(guild, activity, color, Color, message);
                            return;
                        }
                    }
                    _Message(guild, activity, color, Color, null);
                }
            } catch (Exception ignored) {
            }
        } else {
            P.pause(10000);
            ForwardPlayingGame(guild, activity);
        }
    }

    private void _Message(Guild guild, Activity A_game, Color color, String Color, Message message) {
        int x_count = 0;
        /*
        list for detail - string and int
        */
        HashMap<Integer, String> list_string_detail = new HashMap<>();
        list_string_detail.put(0, null);
        HashMap<Integer, Integer> list_integer_detail = new HashMap<>();
        list_integer_detail.put(0, 0);
        /*
        list for state - string and int
        */
        HashMap<Integer, String> list_string_state = new HashMap<>();
        list_string_state.put(0, null);
        HashMap<Integer, Integer> list_integer_state = new HashMap<>();
        list_integer_state.put(0, 0);
        /*
        list for largeimg - string and int
        */
        HashMap<Integer, String> list_string_largeimg = new HashMap<>();
        list_string_largeimg.put(0, null);
        HashMap<Integer, Integer> list_integer_largeimg = new HashMap<>();
        list_integer_largeimg.put(0, 0);
        /*
        setup builder to send/edit message
        */
        TextChannel channel = guild.getTextChannelById(channel_id);
        EmbedBuilder builder = new EmbedBuilder().setColor(color);
        if (Color.equals("stream")) {
            builder.setTitle("Stream");
        } else {
            builder.setTitle(A_game.getName());
        }
        /*
        get int / count
        */
        for (Member member : guild.getMembers()) {
            /*
            if member is not offline and not a bot
            */
            if (member.getOnlineStatus() != OnlineStatus.OFFLINE && !member.getUser().isBot()) {
                /*
                if member has activity´s
                */
                if (!member.getActivities().isEmpty()) {
                    for (Activity activity : member.getActivities()) {
                        /*
                        if game == activity | count
                        */
                        boolean bool;
                        String s = "<keine Daten>";
                        if (Color.equals("stream")) {
                            bool = activity.getType() == Activity.ActivityType.STREAMING;
                        } else {
                            bool = activity.getName().equals(A_game.getName());
                        }
                        if (bool) {
                            x_count++;
                            if (activity.asRichPresence() != null) {
                                /*
                                count | detail
                                */
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
                                    } else {
                                        if (!list_string_detail.toString().contains(s)) {
                                            list_string_detail.put(list_string_detail.size(), s);
                                            list_integer_detail.put(list_integer_detail.size(), 1);
                                        } else {
                                            for (int a = 0; a <= list_string_detail.size(); a++) {
                                                if (list_string_detail.get(a).equalsIgnoreCase(s)) {
                                                    list_integer_detail.put(a, list_integer_detail.get(a) + 1);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                } catch (NullPointerException ignored) {
                                }
                                /*
                                detail | state
                                */
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
                                    } else {
                                        if (!list_string_state.toString().contains(s)) {
                                            list_string_state.put(list_string_state.size(), s);
                                            list_integer_state.put(list_integer_state.size(), 1);
                                        } else {
                                            for (int a = 0; a <= list_string_state.size(); a++) {
                                                if (list_string_state.get(a).equalsIgnoreCase(s)) {
                                                    list_integer_state.put(a, list_integer_state.get(a) + 1);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                } catch (NullPointerException ignored) {
                                }
                                /*
                                state | largeimg
                                */
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
                                    } else {
                                        if (!list_string_largeimg.toString().contains(s)) {
                                            list_string_largeimg.put(list_string_largeimg.size(), s);
                                            list_integer_largeimg.put(list_integer_largeimg.size(), 1);
                                        } else {
                                            for (int a = 0; a <= list_string_largeimg.size(); a++) {
                                                if (list_string_largeimg.get(a).equalsIgnoreCase(s)) {
                                                    list_integer_largeimg.put(a, list_integer_largeimg.get(a) + 1);
                                                    break;
                                                }
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
        /*
        setup message | for one or more - member
        */
        if (x_count == 1) {
            if (Color.equals("default")) {
                builder.setDescription("**" + x_count + "** Member, spielt gerade dieses Spiel");
            } else if (Color.equals("stream")) {
                builder.setDescription("**" + x_count + "** Member, streamt");
            } else if (Color.equals("listen")) {
                builder.setDescription("**" + x_count + "** Member, hört Musik");
            } else if (Color.equals("watch")) {
                builder.setDescription("**" + x_count + "** Member, schaut ein Film");
            } else if (Color.equals("custom")) {
                builder.setDescription("**" + x_count + "** Member, hat dies als Status");
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
            } else if (Color.equals("custom")) {
                builder.setDescription("**" + x_count + "** Member, haben dies als Status");
            }
        }
        /*
        add everything from the list to the builder
        */
        if (Color.equals("default") || Color.equals("stream")) {
            builder.getDescriptionBuilder().append("\n");
            /*
            list detail
            */
            if (list_string_detail.get(0) != null && list_integer_detail.get(0) != null && !list_string_detail.toString().contains("null")) {
                builder.getDescriptionBuilder().append("\n__Detail:__\n");
                for (int _int = 0; _int < list_string_detail.size(); _int++) {
                    builder.getDescriptionBuilder().append("**" + list_integer_detail.get(_int) + "** - " + list_string_detail.get(_int) + "\n");
                }
            }
            /*
            list state
            */
            if (list_string_state.size() != 0 && list_integer_state.get(0) != null && !list_string_state.toString().contains("null")) {
                builder.getDescriptionBuilder().append("\n__State:__\n");
                for (int _int = 0; _int < list_string_state.size(); _int++) {
                    builder.getDescriptionBuilder().append("**" + list_integer_state.get(_int) + "** - " + list_string_state.get(_int) + "\n");
                }
            }
            /*
            list largeimg
            */
            if (list_string_largeimg.size() != 0 && list_integer_largeimg.get(0) != null && !list_string_largeimg.toString().contains("null")) {
                builder.getDescriptionBuilder().append("\n__Largeimg:__\n");
                for (int _int = 0; _int < list_string_largeimg.size(); _int++) {
                    builder.getDescriptionBuilder().append("**" + list_integer_largeimg.get(_int) + "** - " + list_string_largeimg.get(_int) + "\n");
                }
            }
            /*
            list CheckGameOnWebsite
            */
            CheckGameOnWebsite GIS = new CheckGameOnWebsite();

            String _steam = GIS.Steam(A_game.getName());
            String _epic = GIS.EpicGames(A_game.getName());
            String _blizzard = GIS.Blizzard(A_game.getName());
            String _origin = GIS.Origin(A_game.getName());
            String _uplay = GIS.Uplay(A_game.getName());

            builder.appendDescription("\n");

            if (!_steam.contains("null")) {
                builder.appendDescription(_steam + "\n");
            }
            if (!_epic.contains("null")) {
                builder.appendDescription(_epic + "\n");
            }
            if (!_blizzard.contains("null")) {
                builder.appendDescription(_blizzard + "\n");
            }
            if (!_origin.contains("null")) {
                builder.appendDescription(_origin + "\n");
            }
            if (!_uplay.contains("null")) {
                builder.appendDescription(_uplay + "\n");
            }
        }
        /*
        send/edit message
        */
        try {
            if (message == null) {
                if (x_count != 0) {
                    try {
                        if (!A_game.asRichPresence().getLargeImage().equals(message.getEmbeds().get(0).getThumbnail()) && A_game.asRichPresence().getLargeImage().getText() == null) {
                            channel.sendMessage(builder.setThumbnail(A_game.asRichPresence().getLargeImage().getUrl()).build()).complete();
                        } else {
                            channel.sendMessage(builder.setThumbnail(message.getEmbeds().get(0).getThumbnail().getUrl()).build()).complete();
                        }
                    } catch (NullPointerException e) {
                        try {
                            if (!A_game.asRichPresence().getSmallImage().equals(message.getEmbeds().get(0).getThumbnail()) && A_game.asRichPresence().getSmallImage().getText() == null) {
                                channel.sendMessage(builder.setThumbnail(A_game.asRichPresence().getSmallImage().getUrl()).build()).complete();
                            } else {
                                channel.sendMessage(builder.setThumbnail(message.getEmbeds().get(0).getThumbnail().getUrl()).build()).complete();
                            }
                        } catch (NullPointerException e1) {
                            try {
                                channel.sendMessage(builder.build()).queue();
                            } catch (ErrorResponseException ignored) {
                            }
                        }
                    }
                }
            } else {
                if (x_count == 0) {
                    try {
                        message.delete().queue();
                    } catch (ErrorResponseException ignored) {
                    }
                } else {
                    try {
                        if (!A_game.asRichPresence().getLargeImage().equals(message.getEmbeds().get(0).getThumbnail()) && A_game.asRichPresence().getLargeImage().getText() == null) {
                            message.editMessage(builder.setThumbnail(A_game.asRichPresence().getLargeImage().getUrl()).build()).complete();
                        } else {
                            message.editMessage(builder.setThumbnail(message.getEmbeds().get(0).getThumbnail().getUrl()).build()).complete();
                        }
                    } catch (NullPointerException e) {
                        try {
                            if (!A_game.asRichPresence().getSmallImage().equals(message.getEmbeds().get(0).getThumbnail()) && A_game.asRichPresence().getSmallImage().getText() == null) {
                                message.editMessage(builder.setThumbnail(A_game.asRichPresence().getSmallImage().getUrl()).build()).complete();
                            } else {
                                message.editMessage(builder.setThumbnail(message.getEmbeds().get(0).getThumbnail().getUrl()).build()).complete();
                            }
                        } catch (NullPointerException e1) {
                            try {
                                message.editMessage(builder.build()).queue();
                            } catch (ErrorResponseException ignored) {
                            }
                        }
                    }
                }
            }
        } catch (ErrorResponseException ignored) {
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