package count.GamePlayingCount;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import other._stuff.CheckGameOnWebsite;

import java.awt.*;
import java.util.HashMap;
import java.util.Objects;

public class _Message {

    public static void _message(Guild guild, Activity A_game, Color color, String Color, Message message) {
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
        TextChannel channel = guild.getTextChannelById(_main_GamePlayingCount.channel_id);
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
            switch (Color) {
                case "default":
                    builder.setDescription("**" + x_count + "** Member, spielt gerade dieses Spiel");
                    break;
                case "stream":
                    builder.setDescription("**" + x_count + "** Member, streamt");
                    break;
                case "listen":
                    builder.setDescription("**" + x_count + "** Member, h\u00f6rt Musik");
                    break;
                case "watch":
                    builder.setDescription("**" + x_count + "** Member, schaut ein Film");
                    break;
                case "custom":
                    builder.setDescription("**" + x_count + "** Member, hat dies als Status");
                    break;
            }
        } else if (x_count > 1) {
            switch (Color) {
                case "default":
                    builder.setDescription("**" + x_count + "** Member, spielen gerade dieses Spiel");
                    break;
                case "stream":
                    builder.setDescription("**" + x_count + "** Member, streamen");
                    break;
                case "listen":
                    builder.setDescription("**" + x_count + "** Member, h\u00f6ren Musik");
                    break;
                case "watch":
                    builder.setDescription("**" + x_count + "** Member, schauen Filme");
                    break;
                case "custom":
                    builder.setDescription("**" + x_count + "** Member, haben dies als Status");
                    break;
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
                    builder.getDescriptionBuilder().append("**").append(list_integer_detail.get(_int)).append("** - ").append(list_string_detail.get(_int)).append("\n");
                }
            }
            /*
            list state
            */
            if (list_string_state.size() != 0 && list_integer_state.get(0) != null && !list_string_state.toString().contains("null")) {
                builder.getDescriptionBuilder().append("\n__State:__\n");
                for (int _int = 0; _int < list_string_state.size(); _int++) {
                    builder.getDescriptionBuilder().append("**").append(list_integer_state.get(_int)).append("** - ").append(list_string_state.get(_int)).append("\n");
                }
            }
            /*
            list largeimg
            */
            if (list_string_largeimg.size() != 0 && list_integer_largeimg.get(0) != null && !list_string_largeimg.toString().contains("null")) {
                builder.getDescriptionBuilder().append("\n__Largeimg:__\n");
                for (int _int = 0; _int < list_string_largeimg.size(); _int++) {
                    builder.getDescriptionBuilder().append("**").append(list_integer_largeimg.get(_int)).append("** - ").append(list_string_largeimg.get(_int)).append("\n");
                }
            }
            /*
            list CheckGameOnWebsite
            */
            CheckGameOnWebsite GIS = new CheckGameOnWebsite();

            builder.appendDescription("\n");

            for (String string : GIS.startcheck(A_game.getName())) {
                if (!string.contains("null")) {
                    builder.appendDescription(string + "\n");
                }
            }
        }
        /*
        send/edit message
        */
        if (message == null) {
            if (x_count != 0) {
                try {
                    if (!A_game.asRichPresence().getLargeImage().equals(message.getEmbeds().get(0).getThumbnail()) && A_game.asRichPresence().getLargeImage().getText() == null) {
                        try {
                            channel.sendMessage(builder.setThumbnail(A_game.asRichPresence().getLargeImage().getUrl()).build()).complete();
                        } catch (ErrorResponseException e_response) {
                            return;
                        }
                    } else {
                        try {
                            channel.sendMessage(builder.setThumbnail(message.getEmbeds().get(0).getThumbnail().getUrl()).build()).complete();
                        } catch (ErrorResponseException e_response) {
                            return;
                        }
                    }
                } catch (NullPointerException e) {
                    try {
                        if (!A_game.asRichPresence().getSmallImage().equals(message.getEmbeds().get(0).getThumbnail()) && A_game.asRichPresence().getSmallImage().getText() == null) {
                            try {
                                channel.sendMessage(builder.setThumbnail(A_game.asRichPresence().getSmallImage().getUrl()).build()).complete();
                            } catch (ErrorResponseException e_response) {
                                return;
                            }
                        } else {
                            try {
                                channel.sendMessage(builder.setThumbnail(message.getEmbeds().get(0).getThumbnail().getUrl()).build()).complete();
                            } catch (ErrorResponseException e_response) {
                                return;
                            }
                        }
                    } catch (NullPointerException e1) {
                        try {
                            channel.sendMessage(builder.build()).queue();
                        } catch (ErrorResponseException e_response) {
                            return;
                        }
                    }
                }
            }
        } else {
            if (x_count == 0) {
                try {
                    if (checkMessage(guild, message)) {
                        message.delete().queue();
                    }
                } catch (ErrorResponseException e_response) {
                    return;
                }
            } else {
                /*LARGE IMG*/
                try {
                    if (!A_game.asRichPresence().getLargeImage().equals(message.getEmbeds().get(0).getThumbnail()) && A_game.asRichPresence().getLargeImage().getText() == null) {
                        try {
                            if (checkMessage(guild, message)) {
                                builder.setThumbnail(A_game.asRichPresence().getLargeImage().getUrl());
                                message.editMessage(builder.build()).complete();
                            }
                        } catch (ErrorResponseException e_response) {
                            return;
                        }
                    } else {
                        try {
                            if (checkMessage(guild, message)) {
                                builder.setThumbnail(message.getEmbeds().get(0).getThumbnail().getUrl());
                                message.editMessage(builder.build()).complete();
                            }
                        } catch (ErrorResponseException e_response) {
                            return;
                        }
                    }
                } catch (NullPointerException e) {
                    /*SMALL IMG*/
                    try {
                        if (!A_game.asRichPresence().getSmallImage().equals(message.getEmbeds().get(0).getThumbnail()) && A_game.asRichPresence().getSmallImage().getText() == null) {
                            try {
                                if (checkMessage(guild, message)) {
                                    builder.setThumbnail(A_game.asRichPresence().getSmallImage().getUrl());
                                    message.editMessage(builder.build()).complete();
                                }
                            } catch (ErrorResponseException e_response) {
                                return;
                            }
                        } else {
                            try {
                                if (checkMessage(guild, message)) {
                                    builder.setThumbnail(message.getEmbeds().get(0).getThumbnail().getUrl());
                                    message.editMessage(builder.build()).complete();
                                }
                            } catch (ErrorResponseException e_response) {
                                return;
                            }
                        }
                    } catch (NullPointerException e1) {
                        /*NO IMG*/
                        try {
                            if (checkMessage(guild, message)) {
                                message.editMessage(builder.build()).queue();
                            }
                        } catch (ErrorResponseException e_response) {
                            return;
                        }
                    }
                }
            }
        }
        CountNotPlayingGames._message(guild);

        list_string_detail.clear();
        list_integer_detail.clear();

        list_string_state.clear();
        list_integer_state.clear();

        list_string_largeimg.clear();
        list_integer_largeimg.clear();
    }

    private static boolean checkMessage(Guild guild, Message message) {
        boolean found = false;

        for (Message x_message : Objects.requireNonNull(guild.getTextChannelById(_main_GamePlayingCount.channel_id)).getIterableHistory()) {
            if (Objects.equals(x_message.getEmbeds().get(0).getTitle(), message.getEmbeds().get(0).getTitle())) {
                found = true;
                break;
            }
        }
        return found;
    }
}
