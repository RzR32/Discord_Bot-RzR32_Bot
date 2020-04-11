package check_create;

import config.PropertiesFile;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import other.LogBack;

import java.util.ArrayList;

public class CheckChannel {

    private static final ArrayList<String> list_ID = new ArrayList<>() {{
        add("games");
        add("logs");
        add("playingcount");
        add("bot-channel");
        add("bot-zustimmung");
    }};
    LogBack LB = new LogBack();

    public void StartChecking(Guild guild) {
        for (String channel : list_ID) {
            checkingChannel(guild, channel);
        }
    }

    public void checkingChannel(Guild guild, String channel) {
        if (PropertiesFile.readsPropertiesFile(">" + channel + "_on").equals("true")) {
            String id = PropertiesFile.readsPropertiesFile(channel);
            String channel_type;
            String category;

            if (list_ID.contains(channel)) {
                channel_type = "text";
                category = "gamecategory";
            } else {
                channel_type = "voice";
                if (channel.equals("gamecount")) {
                    category = "gamecategory";
                } else if (channel.equals("twitchcount")) {
                    category = "streamcategory";
                } else {
                    category = "maincount";
                }
            }
            if (PropertiesFile.readsPropertiesFile(">" + category + "_on").equals("true")) {
                if (id.isBlank() || id.isEmpty() || PropertiesFile.readsPropertiesFile(channel) == null) {
                    LB.log(Thread.currentThread().getName(), "ID in config file for channel *" + channel + "* is null, search for another or create new", "warn");
                    String s = null;
                    Category cat = null;
                    Category maincount_ID = null;
                    Category stream_ID = null;
                    Category gamecategory = null;

                    if (PropertiesFile.readsPropertiesFile(">maincount_on").equals("true")) {
                        maincount_ID = guild.getCategoryById(PropertiesFile.readsPropertiesFile("maincount"));
                    }
                    if (PropertiesFile.readsPropertiesFile(">streamcategory_on").equals("true")) {
                        stream_ID = guild.getCategoryById(PropertiesFile.readsPropertiesFile("streamcategory"));
                    }
                    if (PropertiesFile.readsPropertiesFile(">gamecategory_on").equals("true")) {
                        gamecategory = guild.getCategoryById(PropertiesFile.readsPropertiesFile("gamecategory"));
                    }
                    switch (channel) {
                        /*Voice (channel)*/
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
                        /*Text (channel)*/
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
                    GuildChannel counter_channel;
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
    }
}