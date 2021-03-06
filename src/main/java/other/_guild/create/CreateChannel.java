package other._guild.create;

import config.PropertiesFile;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import other._guild.Agreement_Message;
import other._stuff.LogBack;

public class CreateChannel {

    LogBack LB = new LogBack();

    /*
    create new channel IF no found that starts with the same name!
    */
    public void createchannel(Guild guild, Category cat, String channel_name, String channel_id_name, String channel_type) {
        if (PropertiesFile.readsPropertiesFile(">" + channel_id_name + "_on", "config").equals("true")) {
            assert cat != null;
            if (channel_type.equals("voice")) {
                for (VoiceChannel voiceChannel : cat.getVoiceChannels()) {
                    if (voiceChannel.getName().startsWith(channel_name)) {
                        LB.log(Thread.currentThread().getName(), "Channel found with the name *" + channel_name + "*", "info");
                        PropertiesFile.writePropertiesFile(channel_id_name, voiceChannel.getId(), "config");
                        return;
                    }
                }
                LB.log(Thread.currentThread().getName(), "Create new Channel *" + channel_name + "*", "info");
                if (channel_name.equals("TwitchFollower") || channel_name.equals("GameCount")) {
                    cat.createVoiceChannel(channel_name + " > 0").queue(channel1 -> {
                        PropertiesFile.writePropertiesFile(channel_id_name, channel1.getId(), "config");
                        channel1.createPermissionOverride(guild.getPublicRole()).setAllow(Permission.VIEW_CHANNEL).setDeny(Permission.VOICE_CONNECT).queue();
                        channel1.createPermissionOverride(guild.getSelfMember()).setAllow(Permission.VIEW_CHANNEL, Permission.MANAGE_CHANNEL).queue();
                    });
                } else {
                    cat.createVoiceChannel(channel_name + " > 0").queue(channel1 -> PropertiesFile.writePropertiesFile(channel_id_name, channel1.getId(), "config"));
                }

            } else {
                for (TextChannel textChannel : cat.getTextChannels()) {
                    if (textChannel.getName().startsWith(channel_name)) {
                        LB.log(Thread.currentThread().getName(), "Channel found with the name *" + channel_name + "*", "info");
                        PropertiesFile.writePropertiesFile(channel_id_name, textChannel.getId(), "config");
                        return;
                    }
                }
                switch (channel_name) {
                    case "playingcount":
                    case "games":
                        cat.createTextChannel(channel_name).queue(channel1 -> {
                            PropertiesFile.writePropertiesFile(channel_id_name, channel1.getId(), "config");
                            channel1.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_WRITE).queue();
                        });
                        break;
                    case "logs":
                        cat.createTextChannel(channel_name).queue(channel1 -> {
                            PropertiesFile.writePropertiesFile(channel_id_name, channel1.getId(), "config");
                            channel1.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                        });
                        break;
                    case "bot-channel":
                        cat.createTextChannel(channel_name).queue(channel1 -> PropertiesFile.writePropertiesFile(channel_id_name, channel1.getId(), "config"));
                        break;
                    case "bot-zustimmung":
                        if (PropertiesFile.readsPropertiesFile(">games_on", "config").equals("true")) {
                            cat.createTextChannel(channel_name).queue(channel1 -> {
                                PropertiesFile.writePropertiesFile(channel_id_name, channel1.getId(), "config");
                                channel1.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_WRITE).queue();
                                Agreement_Message A_M = new Agreement_Message();
                                A_M.Message(guild, channel1);
                            });
                        } else {
                            LB.log(Thread.currentThread().getName(), "*>games_on* is not set on true!, so *bot-zustimmung* is disabled!", "warn");
                        }
                        break;
                }
                LB.log(Thread.currentThread().getName(), "Create new Channel *" + channel_name + "*", "info");
            }
        }
    }
}