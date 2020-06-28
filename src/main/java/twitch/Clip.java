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

public class Clip {

    /*
    check if message is already in guild, if not create new
    */
    public void ClipMessage(Guild guild) {
        try {
            if (PropertiesFile.readsPropertiesFile(">clips_on", "config").equals("true") && PropertiesFile.readsPropertiesFile(">streamcategory_on", "config").equals("true")) {
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

                String period = PropertiesFile.readsPropertiesFile("clips_period", "config");
                if (!period.equals("day") && !period.equals("week") && !period.equals("month") && !period.equals("all")) {
                    System.err.println("Error: clips_period has a wrong String; day, week, month or all!");
                    return;
                }

                int clips_number = Integer.parseInt(PropertiesFile.readsPropertiesFile("clips_number", "config"));
                if (clips_number < 0 || clips_number > 100) {
                    System.err.println("Error: clips_number has a wrong int, between 0 - 100!");
                    return;
                }

                String[] list_source = mr.doRequest("clips/top?channel=" + PropertiesFile.readsPropertiesFile("twitchname", "config") + "&period=" + period + "&limit=" + clips_number);

                if (list_source == null) {
                    return;
                }

                /*split the one Array to ten*/
                ArrayList<String> list = new ArrayList<>();

                /*convert Array to list*/
                String[] list_temp = Arrays.toString(list_source).split("slug");
                int int_position_in_list = 0;
                for (String string : list_temp) {
                    if (!string.contains("[{\"clips\":[{\"")) {
                        list.add(int_position_in_list, "{\"slug" + string);
                        int_position_in_list++;
                        if (int_position_in_list == clips_number) {
                            break;
                        }
                    }
                }

                /*count backwards - for newest*/
                for (int int_position_reverse_in_list = int_position_in_list; int_position_reverse_in_list > 0; int_position_reverse_in_list--) {
                    String[] out = list.get(int_position_reverse_in_list - 1).split(",");

                    /*check if user has "new" clips*/
                    if (!checkClips(out)) {
                        String slug = getSlug(out);
                        String displayName = getDisplayName(out);
                        String channelurl = getChannelUrl(out);
                        String game = getGame(out);
                        String title = getTitle(out);
                        String curator = getCurator(out);
                        String createAt = getCreateAt(out);

                        if (out != null) {
                            for (TextChannel channel : guild.getCategoryById(PropertiesFile.readsPropertiesFile("streamcategory", "config")).getTextChannels()) {
                                if (channel.getType() == ChannelType.TEXT) {
                                    for (Message message : channel.getIterableHistory()) {
                                        if (message.getContentRaw().contains(slug)) {
                                            return;
                                        }
                                    }
                                }
                                EmbedBuilder builder = new EmbedBuilder().setTitle("Twitch Clip").setColor(Color.MAGENTA).setDescription("**" + curator + "** hat am __" +
                                        createAt + "__ ein Clip bei [**" + displayName + "**](" + channelurl + ") erstellt.\n" +
                                        "Title: " + title + "\nSpiel: " + game);

                                channel.sendMessage(builder.build()).queue();
                                channel.sendMessage(slug).queue();
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            ClipMessage(guild);
        }
    }

    private String getSlug(String[] strings) {
        String out = null;
        for (String string : strings) {
            if (string.contains("slug")) {
                out = "https://clips.twitch.tv/" + string.replace("\"", "").substring(6);
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

    private String getChannelUrl(String[] strings) {
        String out = null;
        for (String string : strings) {
            if (string.contains("channel_url")) {
                out = string.replace("\"", "").substring(13);
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

    private String getTitle(String[] strings) {
        String out = null;
        for (String string : strings) {
            if (string.contains("title")) {
                out = string.replace("\"", "").substring(7);
                break;
            }
        }
        return out;
    }

    private String getCurator(String[] strings) {
        String out = null;
        for (String string : strings) {
            if (string.contains("curator")) {
                out = string.replace("\"", "").substring(13);
                User U = new User();
                out = U.getUserbyID(out);
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

    private boolean checkClips(String[] strings) {
        boolean out = false;
        for (String string : strings) {
            if (string.contains("{\"clips\":[]")) {
                out = true;
                break;
            }
        }
        return out;
    }
}