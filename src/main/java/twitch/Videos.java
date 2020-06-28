package twitch;

import config.PropertiesFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import other._guild.check.CheckCategory;
import other._guild.check.CheckChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Videos {

    /*
    check if message is already in guild, if not create new
    */
    public void VideoMessage(Guild guild) {
        try {
            if (PropertiesFile.readsPropertiesFile(">videos_on", "config").equals("true") && PropertiesFile.readsPropertiesFile(">streamcategory_on", "config").equals("true")) {
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

                MakeRequest mr = new MakeRequest();

                StringBuilder extra = new StringBuilder();

                if (PropertiesFile.readsPropertiesFile("videos_archice_true?", "config").equals("true")) {
                    extra.append("?broadcast_type=archive");
                }
                if (PropertiesFile.readsPropertiesFile("videos_uplaod_true?", "config").equals("true")) {
                    extra.append("?broadcast_type=uplaod");
                }
                if (PropertiesFile.readsPropertiesFile("videos_highlight_true?", "config").equals("true")) {
                    extra.append("?broadcast_type=highlight");
                }

                int videos_number = Integer.parseInt(PropertiesFile.readsPropertiesFile("videos_number", "config"));
                if (videos_number < 0 || videos_number > 100) {
                    System.err.println("Error: videos_number has a wrong int, between 0 - 100!");
                    return;
                }

                User U = new User();
                String[] list_source;

                if (extra.length() == 0) {
                    list_source = mr.doRequest("channels/" + U.getUserbyName(PropertiesFile.readsPropertiesFile("twitchname", "config")) + "/videos?limit=" + videos_number);
                } else {
                    list_source = mr.doRequest("channels/" + U.getUserbyName(PropertiesFile.readsPropertiesFile("twitchname", "config")) + "/videos" + extra + "&limit=" + videos_number);
                }

                if (list_source == null) {
                    return;
                }

                /*split the one Array to ten*/
                ArrayList<String> list = new ArrayList<>();

                /*convert Array to list*/
                String[] list_temp = Arrays.toString(list_source).split("title");
                int int_position_in_list = 0;
                for (String string : list_temp) {
                    if (!string.contains("[{\"_total")) {
                        list.add(int_position_in_list, "title" + string);
                        int_position_in_list++;
                        if (int_position_in_list == videos_number) {
                            break;
                        }
                    }
                }

                /*count backwards - for newest*/
                for (int int_position_reverse_in_list = int_position_in_list; int_position_reverse_in_list > 0; int_position_reverse_in_list--) {
                    String[] out = list.get(int_position_reverse_in_list - 1).split(",");

                    /*check if user has "new" clips*/
                    if (!checkVideos(out)) {
                        String url = getUrl(out);
                        String displayName = getDisplayName(out);
                        String channelurl = "https://www.twitch.tv/" + displayName;
                        String game = getGame(out);
                        String type = getType(out);
                        String title = getTitle(out);
                        String createAt = getCreateAt(out);

                        if (out != null) {
                            for (TextChannel channel : guild.getCategoryById(PropertiesFile.readsPropertiesFile("streamcategory", "config")).getTextChannels()) {
                                if (channel.getType() == ChannelType.TEXT) {
                                    for (Message message : channel.getIterableHistory()) {
                                        if (message.getContentRaw().contains(url)) {
                                            return;
                                        }
                                    }
                                }
                                EmbedBuilder builder = new EmbedBuilder().setTitle("Twitch Video").setColor(Color.MAGENTA).setDescription("Bei [**" + displayName + "**](" + channelurl + ")" +
                                        " wurde am __" + createAt + "__ ein " + type + " erstellt.\n" +
                                        "Title: " + title + "\nSpiel: " + game);

                                channel.sendMessage(builder.build()).queue();
                                channel.sendMessage(url).queue();
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            VideoMessage(guild);
        }
    }

    private String getUrl(String[] strings) {
        String out = null;
        for (String string : strings) {
            if (string.replace("\"", "").contains("url")) {
                out = string.replace("\"", "").substring(5);
                break;
            }
        }
        return out;
    }

    private String getDisplayName(String[] strings) {
        String out = null;
        for (String string : strings) {
            if (string.contains("display_name")) {
                out = string.replace("\"", "").substring(14);
                break;
            }
        }
        return out;
    }

    private String getGame(String[] strings) {
        String out = null;
        for (String string : strings) {
            if (string.replace("\"", "").contains("game")) {
                out = string.replace("\"", "").substring(6);
                break;
            }
        }
        return out;
    }

    private String getType(String[] strings) {
        String out = null;
        for (String string : strings) {
            if (string.replace("\"", "").contains("broadcast_type")) {
                out = string.replace("\"", "").substring(16);
                break;
            }
        }
        return out;
    }

    private String getTitle(String[] strings) {
        String out = null;
        for (String string : strings) {
            if (string.contains("title")) {
                out = string.replace("\"", "").substring(6);
                break;
            }
        }
        return out;
    }

    private String getCreateAt(String[] strings) {
        String out = null;
        for (String string : strings) {
            if (string.contains("created_at")) {
                out = string.replace("\"", "").substring(12).replace("T", " ").replace("Z", "");
                break;
            }
        }
        return out;
    }

    private boolean checkVideos(String[] strings) {
        boolean out = false;
        for (String string : strings) {
            if (string.contains("videos\":[]")) {
                out = true;
                break;
            }
        }
        return out;
    }
}