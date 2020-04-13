package twitch;

import check_create.CheckCategory;
import check_create.CheckChannel;
import config.PropertiesFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
public class Clip {

    /*
    check if message is already in guild, if not create new
    */
    public void ClipMessage(Guild guild) {
        if (PropertiesFile.readsPropertiesFile(">streamcategory_on").equals("true")) {
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

            MakeRequest r = new MakeRequest();

            String period = PropertiesFile.readsPropertiesFile("clips_period");
            if (!period.equals("day") && !period.equals("week") && !period.equals("month") && !period.equals("all")) {
                System.err.println("Error: clips_period has a wrong String, day, week, month or all!");
                return;
            }

            int clips_number = Integer.parseInt(PropertiesFile.readsPropertiesFile("clips_number"));
            if (clips_number < 0 || clips_number > 100) {
                System.err.println("Error: clips_number has a wrong int, between 0 - 100!");
                return;
            }

            String[] list_source = r.doRequest("clips/top?channel=" + PropertiesFile.readsPropertiesFile("twitchname") + "&period=" + period + "&limit=" + clips_number);

            /*split the one Array to ten*/
            ArrayList<String> list = new ArrayList<>();

            /*convert Array to list*/
            String[] list_temp = Arrays.toString(list_source).split("slug");
            int x = 0;
            for (String string : list_temp) {
                if (!string.equals("[{\"clips\":[{\"")) {
                    list.add(x, "{\"slug" + string);
                    x++;
                    if (x == clips_number + 1) {
                        break;
                    }
                }
            }

            /*count backwards - for newest*/
            for (int y = x - 2; y >= 0; y--) {
                String[] out = list.get(y).split(",");

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
                        for (TextChannel channel : guild.getCategoryById(PropertiesFile.readsPropertiesFile("streamcategory")).getTextChannels()) {
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
    }

    private String getSlug(String[] strings) {
        String out = null;
        for (String string : strings) {
            if (string.contains("{\"slug")) {
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