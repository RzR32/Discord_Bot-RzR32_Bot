package check_create;

import config.PropertiesFile;
import net.dv8tion.jda.api.entities.*;
import other.LogBack;

import java.util.ArrayList;

public class CheckChannel {

    LogBack LB = new LogBack();

    private static ArrayList<String> list_ID = new ArrayList<>() {{
        add("games");
        add("logs");
        add("playingcount");
        add("bot-channel");
        add("bot-zustimmung");
    }};

    public void StartChecking(Guild guild) {
        for (String channel : list_ID) {
            checkingChannel(guild, channel);
        }
    }

    public void checkingChannel(Guild guild, String channel) {
        String id = PropertiesFile.readsPropertiesFile(channel);
        String channel_type = null;
        if (list_ID.contains(channel)) {
            channel_type = "text";
        } else {
            channel_type = "voice";
        }
        if (id.isBlank() || id.isEmpty() || PropertiesFile.readsPropertiesFile(channel) == null) {
            LB.log(Thread.currentThread().getName(), "ID in config file for channel *" + channel + "* is null, search for another or create new", "warn");
            String s = null;
            Category cat = null;
            Category maincount_ID = guild.getCategoryById(PropertiesFile.readsPropertiesFile("maincount"));
            Category stream_ID = guild.getCategoryById(PropertiesFile.readsPropertiesFile("streamcategory"));
            Category gamecategory = guild.getCategoryById(PropertiesFile.readsPropertiesFile("gamecategory"));
            switch (channel) {
                /*
                VOICE channel´s
                 */
                case "categorycount":
                    s = "CategoryCount";
                    cat = maincount_ID;
                    break;
                case "gamerolecount":
                    s = "GameRoleCount";
                    cat = maincount_ID;
                    break;
                case "membercount":
                    s = "MemberCount";
                    cat = maincount_ID;
                    break;
                case "rolecount":
                    s = "RoleCount";
                    cat = maincount_ID;
                    break;
                case "textchannelcount":
                    s = "TextChannelCount";
                    cat = maincount_ID;
                    break;
                case "voicechannelcount":
                    s = "VoiceChannelCount";
                    cat = maincount_ID;
                    break;
                case "gamecount":
                    s = "GameCount";
                    cat = gamecategory;
                    break;
                case "twitchcount":
                    s = "TwitchFollower";
                    cat = stream_ID;
                    break;
                    /*
                    TEXT channel´s
                     */
                case "games":
                case "logs":
                case "playingcount":
                case "bot-channel":
                case "bot-zustimmung":
                    s = channel;
                    cat = gamecategory;
            }
            CreateChannel C_Channel = new CreateChannel();
            C_Channel.createchannel(guild, cat, s, channel, channel_type);
        } else {
            GuildChannel counter_channel = null;
            if (channel_type.equals("voice")) {
                counter_channel = guild.getVoiceChannelById(id);
            } else {
                counter_channel = guild.getTextChannelById(id);
            }
            if (counter_channel == null) {
                LB.log(Thread.currentThread().getName(), "ID for *" + channel + "* is not a channel | or dont exists!", "error");
                PropertiesFile.writePropertiesFile(channel, "");
                checkingChannel(guild, channel);
            }
        }
    }
}