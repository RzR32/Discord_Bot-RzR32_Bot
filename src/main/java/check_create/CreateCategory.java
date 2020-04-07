package check_create;

import config.PropertiesFile;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import other.Agreement_Message;

public class CreateCategory {

    public void MainStream(Guild guild) {
        guild.createCategory("Stream").queue(category -> {
            /*
            save category id
             */
            PropertiesFile.writePropertiesFile("streamcategory", category.getId());
            /*
            create VOICECHANNEL
             */
            category.getGuild().createVoiceChannel("stream").setParent(guild.getCategoryById(PropertiesFile.readsPropertiesFile("streamcategory"))).queue();
            /*
            create TEXTCHANNEL
             */
            category.getGuild().createTextChannel("stream").setParent(guild.getCategoryById(PropertiesFile.readsPropertiesFile("streamcategory"))).queue();
        });
    }

    public void MainCount(Guild guild) {
        guild.createCategory("ServerStats").queue(category -> {
            /*
            save category id
             */
            PropertiesFile.writePropertiesFile("maincount", category.getId());
            /*
            set permission
             */
            category.createPermissionOverride(guild.getSelfMember()).setAllow(Permission.ALL_VOICE_PERMISSIONS).queue();
            category.createPermissionOverride(guild.getPublicRole()).setAllow(Permission.VIEW_CHANNEL).setDeny(Permission.VOICE_CONNECT).queue();
            /*
            create *membercount* channel
             */
            if (PropertiesFile.readsPropertiesFile(">membercount_on").equals("true")) {
                category.getGuild().createVoiceChannel("MemberCount > 0").setParent(guild.getCategoryById(PropertiesFile.readsPropertiesFile("maincount"))).setPosition(1).queue(channel ->
                        PropertiesFile.writePropertiesFile("membercount", channel.getId()));
            }
            /*
            create *rolecount* channel
             */
            if (PropertiesFile.readsPropertiesFile(">rolecount_on").equals("true")) {
                category.getGuild().createVoiceChannel("RoleCount > 0").setParent(guild.getCategoryById(PropertiesFile.readsPropertiesFile("maincount"))).setPosition(2).queue(channel ->
                        PropertiesFile.writePropertiesFile("rolecount", channel.getId()));
            }
            /*
            create *gamerolecount* channel
             */
            if (PropertiesFile.readsPropertiesFile(">gamerolecount_on").equals("true")) {
                category.getGuild().createVoiceChannel("GameRoleCount > 0").setParent(guild.getCategoryById(PropertiesFile.readsPropertiesFile("maincount"))).setPosition(3).queue(channel ->
                        PropertiesFile.writePropertiesFile("gamerolecount", channel.getId()));
            }
            /*
            create *categorycount* channel
             */
            if (PropertiesFile.readsPropertiesFile(">categorycount_on").equals("true")) {
                category.getGuild().createVoiceChannel("CategoryCount > 0").setParent(guild.getCategoryById(PropertiesFile.readsPropertiesFile("maincount"))).setPosition(4).queue(channel ->
                        PropertiesFile.writePropertiesFile("categorycount", channel.getId()));
            }
            /*
            create *textchannelcount* channel
             */
            if (PropertiesFile.readsPropertiesFile(">textchannelcount_on").equals("true")) {
                category.getGuild().createVoiceChannel("TextChannelCount > 0").setParent(guild.getCategoryById(PropertiesFile.readsPropertiesFile("maincount"))).setPosition(5).queue(channel ->
                        PropertiesFile.writePropertiesFile("textchannelcount", channel.getId()));
            }
            /*
            create *voicechannelcount* channel
             */
            if (PropertiesFile.readsPropertiesFile(">voicechannelcount_on").equals("true")) {
                category.getGuild().createVoiceChannel("VoiceChannelCount > 0").setParent(guild.getCategoryById(PropertiesFile.readsPropertiesFile("maincount"))).setPosition(6).queue(channel ->
                        PropertiesFile.writePropertiesFile("voicechannelcount", channel.getId()));
            }
        });
    }

    public void MainGames(Guild guild) {
        guild.createCategory("Game-Category").queue(category -> {
            /*
            save category id
             */
            PropertiesFile.writePropertiesFile("gamecategory", category.getId());
            /*
            set permission
             */
            category.createPermissionOverride(guild.getSelfMember()).setAllow(Permission.ALL_CHANNEL_PERMISSIONS).queue();
            /*
            create *game* channel
             */
            if (PropertiesFile.readsPropertiesFile(">games_on").equals("true")) {
                category.getGuild().createTextChannel("games").setParent(guild.getCategoryById(category.getId())).queue(channel -> {
                    PropertiesFile.writePropertiesFile("games", channel.getId());
                    channel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.MESSAGE_WRITE).queue();
                });
            }
            /*
            create *logs* channel
             */
            if (PropertiesFile.readsPropertiesFile(">logs_on").equals("true")) {
                category.getGuild().createTextChannel("logs").setParent(guild.getCategoryById(category.getId())).queue(channel -> {
                    PropertiesFile.writePropertiesFile("logs", channel.getId());
                    channel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                });
            }
            /*
            create *playingcount* channel
             */
            if (PropertiesFile.readsPropertiesFile(">playingcount_on").equals("true")) {
                category.getGuild().createTextChannel("playingcount").setParent(guild.getCategoryById(category.getId())).queue(channel -> {
                    PropertiesFile.writePropertiesFile("playingcount", channel.getId());
                    channel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.MESSAGE_WRITE).queue();
                });
            }
            /*
            create *bot-channel* channel
             */
            if (PropertiesFile.readsPropertiesFile(">bot-channel_on").equals("true")) {
                category.getGuild().createTextChannel("bot-channel").setParent(guild.getCategoryById(category.getId())).queue(channel ->
                        PropertiesFile.writePropertiesFile("bot-channel", channel.getId()));
            }
            /*
            create *bot-zustimmung* channel
             */
            if (PropertiesFile.readsPropertiesFile(">bot-zustimmung_on").equals("true") && PropertiesFile.readsPropertiesFile(">games_on").equals("true")) {
                category.getGuild().createTextChannel("bot-zustimmung").setParent(guild.getCategoryById(category.getId())).queue(channel -> {
                    PropertiesFile.writePropertiesFile("bot-zustimmung", channel.getId());
                    channel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_WRITE).queue();
                    Agreement_Message guildMessageReaction = new Agreement_Message();
                    guildMessageReaction.Message(guild, channel);
                });
            }
        });
    }
}