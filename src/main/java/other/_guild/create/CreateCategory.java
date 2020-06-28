package other._guild.create;

import config.PropertiesFile;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import other._guild.Agreement_Message;

public class CreateCategory {

    public void MainStream(Guild guild) {
        guild.createCategory("Stream").queue(category -> {
            /*
            save category id
            */
            PropertiesFile.writePropertiesFile("streamcategory", category.getId(), "config");
            /*
            create VOICECHANNEL
            */
            category.getGuild().createVoiceChannel("stream").setParent(guild.getCategoryById(PropertiesFile.readsPropertiesFile("streamcategory", "config"))).queue();
            /*
            create TEXTCHANNEL
            */
            category.getGuild().createTextChannel("stream").setParent(guild.getCategoryById(PropertiesFile.readsPropertiesFile("streamcategory", "config"))).queue();
        });
    }

    public void MainCount(Guild guild) {
        guild.createCategory("ServerStats").queue(category -> {
            /*
            save category id
            */
            PropertiesFile.writePropertiesFile("maincount", category.getId(), "config");
            /*
            set permission
            */
            category.createPermissionOverride(guild.getSelfMember()).setAllow(Permission.VIEW_CHANNEL, Permission.MANAGE_CHANNEL).queue();
            category.createPermissionOverride(guild.getPublicRole()).setAllow(Permission.VIEW_CHANNEL).setDeny(Permission.VOICE_CONNECT).queue();
            /*
            create *membercount* channel - voice
            */
            if (PropertiesFile.readsPropertiesFile(">membercount_on", "config").equals("true")) {
                category.getGuild().createVoiceChannel("MemberCount > 0").setParent(guild.getCategoryById(category.getId())).setPosition(1).queue(channel ->
                        PropertiesFile.writePropertiesFile("membercount", channel.getId(), "config"));
            }
            /*
            create *rolecount* channel - voice
            */
            if (PropertiesFile.readsPropertiesFile(">rolecount_on", "config").equals("true")) {
                category.getGuild().createVoiceChannel("RoleCount > 0").setParent(guild.getCategoryById(category.getId())).setPosition(2).queue(channel ->
                        PropertiesFile.writePropertiesFile("rolecount", channel.getId(), "config"));
            }
            /*
            create *gamerolecount* channel - voice
            */
            if (PropertiesFile.readsPropertiesFile(">gamerolecount_on", "config").equals("true")) {
                category.getGuild().createVoiceChannel("GameRoleCount > 0").setParent(guild.getCategoryById(category.getId())).setPosition(3).queue(channel ->
                        PropertiesFile.writePropertiesFile("gamerolecount", channel.getId(), "config"));
            }
            /*
            create *categorycount* channel - voice
            */
            if (PropertiesFile.readsPropertiesFile(">categorycount_on", "config").equals("true")) {
                category.getGuild().createVoiceChannel("CategoryCount > 0").setParent(guild.getCategoryById(category.getId())).setPosition(4).queue(channel ->
                        PropertiesFile.writePropertiesFile("categorycount", channel.getId(), "config"));
            }
            /*
            create *textchannelcount* channel - voice
            */
            if (PropertiesFile.readsPropertiesFile(">textchannelcount_on", "config").equals("true")) {
                category.getGuild().createVoiceChannel("TextChannelCount > 0").setParent(guild.getCategoryById(category.getId())).setPosition(5).queue(channel ->
                        PropertiesFile.writePropertiesFile("textchannelcount", channel.getId(), "config"));
            }
            /*
            create *voicechannelcount* channel - voice
            */
            if (PropertiesFile.readsPropertiesFile(">voicechannelcount_on", "config").equals("true")) {
                category.getGuild().createVoiceChannel("VoiceChannelCount > 0").setParent(guild.getCategoryById(category.getId())).setPosition(6).queue(channel ->
                        PropertiesFile.writePropertiesFile("voicechannelcount", channel.getId(), "config"));
            }
            /*
            create *emotecount* channel - voice
            */
            if (PropertiesFile.readsPropertiesFile(">emotecount_on", "config").equals("true")) {
                category.getGuild().createVoiceChannel("EmoteCount > 0").setParent(guild.getCategoryById(category.getId())).setPosition(7).queue(channel ->
                        PropertiesFile.writePropertiesFile("emotecount", channel.getId(), "config"));
            }
        });
    }

    public void MainGames(Guild guild) {
        guild.createCategory("Game-Category").queue(category -> {
            /*
            save category id
            */
            PropertiesFile.writePropertiesFile("gamecategory", category.getId(), "config");
            /*
            set permission
            */
            category.createPermissionOverride(guild.getSelfMember()).setAllow(Permission.VIEW_CHANNEL, Permission.MANAGE_CHANNEL, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION).queue();
            /*
            create *game* channel - text
            */
            if (PropertiesFile.readsPropertiesFile(">games_on", "config").equals("true")) {
                category.getGuild().createTextChannel("games").setParent(guild.getCategoryById(category.getId())).queue(channel -> {
                    PropertiesFile.writePropertiesFile("games", channel.getId(), "config");
                    channel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.MESSAGE_WRITE).queue();
                });
            }
            /*
            create *logs* channel - text
            */
            if (PropertiesFile.readsPropertiesFile(">logs_on", "config").equals("true")) {
                category.getGuild().createTextChannel("logs").setParent(guild.getCategoryById(category.getId())).queue(channel -> {
                    PropertiesFile.writePropertiesFile("logs", channel.getId(), "config");
                    channel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                });
            }
            /*
            create *playingcount* channel - text
            */
            if (PropertiesFile.readsPropertiesFile(">playingcount_on", "config").equals("true")) {
                category.getGuild().createTextChannel("playingcount").setParent(guild.getCategoryById(category.getId())).queue(channel -> {
                    PropertiesFile.writePropertiesFile("playingcount", channel.getId(), "config");
                    channel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.MESSAGE_WRITE).queue();
                });
            }
            /*
            create *bot-channel* channel - text
            */
            if (PropertiesFile.readsPropertiesFile(">bot-channel_on", "config").equals("true")) {
                category.getGuild().createTextChannel("bot-channel").setParent(guild.getCategoryById(category.getId())).queue(channel ->
                        PropertiesFile.writePropertiesFile("bot-channel", channel.getId(), "config"));
            }
            /*
            create *bot-zustimmung* channel - text
            */
            if (PropertiesFile.readsPropertiesFile(">bot-zustimmung_on", "config").equals("true") && PropertiesFile.readsPropertiesFile(">games_on", "config").equals("true")) {
                category.getGuild().createTextChannel("bot-zustimmung").setParent(guild.getCategoryById(category.getId())).queue(channel -> {
                    PropertiesFile.writePropertiesFile("bot-zustimmung", channel.getId(), "config");
                    channel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_WRITE).queue();
                    Agreement_Message guildMessageReaction = new Agreement_Message();
                    guildMessageReaction.Message(guild, channel);
                });
            }
            /*
            create *gamecount* channel - voice
            */
            if (PropertiesFile.readsPropertiesFile(">gamecount_on", "config").equals("true")) {
                category.getGuild().createVoiceChannel("GameCount > 0").setParent(guild.getCategoryById(category.getId())).queue(channel ->
                        PropertiesFile.writePropertiesFile("gamecount", channel.getId(), "config"));
            }
        });
    }
}