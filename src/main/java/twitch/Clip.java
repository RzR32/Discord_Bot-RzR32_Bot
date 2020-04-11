package twitch;

import config.PropertiesFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class Clip {

    public String[] getlatestclip(String username) {

        MakeRequest r = new MakeRequest();
        String[] list = r.doRequest("clips/top?channel=" + username + "&period=day");

        String display_name = null;
        String channel_url = null;
        String game = null;
        String title = null;
        String curator = null;
        String clip = null;
        String create = null;

        for (String string : list) {
            string = string.replace("\"", "");

            if (string.contains("slug")) {
                clip = "https://clips.twitch.tv/" + string.substring(14);

            } else if (string.contains("display_name")) {
                display_name = string.substring(13);

            } else if (string.contains("channel_url")) {
                channel_url = string.substring(12);

            } else if (string.contains("game")) {
                game = string.substring(5);

            } else if (string.contains("title")) {
                title = string.substring(6);

            } else if (string.contains("curator")) {
                curator = string.substring(12);
                User U = new User();
                curator = U.getUserbyID(curator);

            } else if (string.contains("created_at")) {
                create = string.substring(11).replace("T", " ").replace("Z", "");
            }
        }

        String[] out = new String[7];
        out[0] = display_name;
        out[1] = channel_url;
        out[2] = game;
        out[3] = title;
        out[4] = curator;
        out[5] = create;
        out[6] = clip;

        return out;
    }

    /*
    check if message is already in guild, if not create new
    */
    public void ClipMessage(Guild guild) {
        String[] list = getlatestclip(PropertiesFile.readsPropertiesFile("twitchname"));
        for (TextChannel channel : guild.getCategoryById(PropertiesFile.readsPropertiesFile("streamcategory")).getTextChannels()) {
            if (channel.getType() == ChannelType.TEXT) {
                for (Message message : channel.getIterableHistory()) {
                    if (message.getContentRaw().contains(list[6])) {
                        return;
                    }
                }
            }
            EmbedBuilder builder = new EmbedBuilder().setTitle("Twitch Clip").setColor(Color.MAGENTA).setDescription("**" + list[4] + "** hat am __" +
                    list[5] + "__ ein Clip bei [**" + list[0] + "**](" + list[1] + ") erstellt.\n" +
                    "Title: " + list[3] + "\nSpiel: " + list[2]);

            channel.sendMessage(builder.build()).queue();
            channel.sendMessage(list[6]).queue();
        }
    }
}