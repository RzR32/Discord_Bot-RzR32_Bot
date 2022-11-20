package listener.commands.owner;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Objects;

public class moveall {

    public static void command(Guild guild, TextChannel channel, Member member, String[] argArray) {
        if (argArray.length == 3) {
            VoiceChannel voiceChannel_from = guild.getVoiceChannelById(argArray[1]);
            VoiceChannel voiceChannel_to = guild.getVoiceChannelById(argArray[2]);

            assert voiceChannel_from != null;
            for (Member x_member : voiceChannel_from.getMembers()) {
                guild.moveVoiceMember(x_member, voiceChannel_to).queue();
            }

        } else if (argArray.length == 2) {
            VoiceChannel voiceChannel_from = Objects.requireNonNull(member.getVoiceState()).getChannel();
            VoiceChannel voiceChannel_to = guild.getVoiceChannelById(argArray[1]);

            assert voiceChannel_from != null;
            for (Member y_member : voiceChannel_from.getMembers()) {
                guild.moveVoiceMember(y_member, voiceChannel_to).queue();
            }

        } else {
            channel.sendMessage("Error: Wrong Input,\n" +
                    ">moveall <channelID from move> <channelID to move>\n" +
                    ">moveall <channelID to move> - **you** need to be in a Voicechannel").queue();
        }
    }
}