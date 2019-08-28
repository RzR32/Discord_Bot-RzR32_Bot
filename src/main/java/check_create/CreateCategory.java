package check_create;

import other.Agreement_Message;
import config.PropertiesFile;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;

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
            category.getGuild().createVoiceChannel("MemberCount > 0").setParent(guild.getCategoryById(PropertiesFile.readsPropertiesFile("maincount"))).setPosition(1).queue(channel ->
                    PropertiesFile.writePropertiesFile("membercount", channel.getId()));
            /*
            create *rolecount* channel
             */
            category.getGuild().createVoiceChannel("RoleCount > 0").setParent(guild.getCategoryById(PropertiesFile.readsPropertiesFile("maincount"))).setPosition(2).queue(channel ->
                    PropertiesFile.writePropertiesFile("rolecount", channel.getId()));
            /*
            create *gamerolecount* channel
             */
            category.getGuild().createVoiceChannel("GameRoleCount > 0").setParent(guild.getCategoryById(PropertiesFile.readsPropertiesFile("maincount"))).setPosition(3).queue(channel ->
                    PropertiesFile.writePropertiesFile("gamerolecount", channel.getId()));
            /*
            create *categorycount* channel
             */
            category.getGuild().createVoiceChannel("CategoryCount > 0").setParent(guild.getCategoryById(PropertiesFile.readsPropertiesFile("maincount"))).setPosition(4).queue(channel ->
                    PropertiesFile.writePropertiesFile("categorycount", channel.getId()));
            /*
            create *textchannelcount* channel
             */
            category.getGuild().createVoiceChannel("TextChannelCount > 0").setParent(guild.getCategoryById(PropertiesFile.readsPropertiesFile("maincount"))).setPosition(5).queue(channel ->
                    PropertiesFile.writePropertiesFile("textchannelcount", channel.getId()));
            /*
            create *voicechannelcount* channel
             */
            category.getGuild().createVoiceChannel("VoiceChannelCount > 0").setParent(guild.getCategoryById(PropertiesFile.readsPropertiesFile("maincount"))).setPosition(6).queue(channel ->
                    PropertiesFile.writePropertiesFile("voicechannelcount", channel.getId()));
            /*
            sync category perm with channel´s
            for (GuildChannel channel : guild.getCategoryById(PropertiesFile.readsPropertiesFile("maincount")).getChannels()) {
                channel.getManager().sync(guild.getCategoryById(PropertiesFile.readsPropertiesFile("maincount"))).queue();
            }
            */
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
            category.getGuild().createTextChannel("games").setParent(guild.getCategoryById(category.getId())).queue(channel -> {
                PropertiesFile.writePropertiesFile("games", channel.getId());
                channel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.MESSAGE_WRITE).queue();
            });
            /*
            create *logs* channel
             */
            category.getGuild().createTextChannel("logs").setParent(guild.getCategoryById(category.getId())).queue(channel -> {
                PropertiesFile.writePropertiesFile("logs", channel.getId());
                channel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
            });
            /*
            create *playingcount* channel
             */
            category.getGuild().createTextChannel("playingcount").setParent(guild.getCategoryById(category.getId())).queue(channel -> {
                PropertiesFile.writePropertiesFile("playingcount", channel.getId());
                channel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.MESSAGE_WRITE).queue();
            });
            /*
            create *bot-channel* channel
             */
            category.getGuild().createTextChannel("bot-channel").setParent(guild.getCategoryById(category.getId())).queue(channel ->
                    PropertiesFile.writePropertiesFile("bot-channel", channel.getId()));
            /*
            create *bot-zustimmung* channel
             */
            category.getGuild().createTextChannel("bot-zustimmung").setParent(guild.getCategoryById(category.getId())).queue(channel -> {
                PropertiesFile.writePropertiesFile("bot-zustimmung", channel.getId());
                channel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_WRITE).queue();
                Agreement_Message guildMessageReaction = new Agreement_Message();
                guildMessageReaction.Message(guild, channel);
            });
        });
    }
}