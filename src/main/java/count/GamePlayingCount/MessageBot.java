package count.GamePlayingCount;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class MessageBot {
    /*
    Message for the Bot
    */
    public static void _message(Guild guild) {
        EmbedBuilder builder = new EmbedBuilder().setColor(new Color(0, 0, 0));
        TextChannel channel = guild.getTextChannelById(_main_GamePlayingCount.channel_id);
        channel.sendMessage(builder.setTitle(guild.getJDA().getPresence().getActivity().getName()).setDescription("**1** Member (__Bot__)").setFooter(guild.getJDA().getSelfUser().getName(),
                "https://cdn.discordapp.com/avatars/391244025015042049/5644aad11ff3d68620bfe939bee6323d.png").build()).complete();
    }
}