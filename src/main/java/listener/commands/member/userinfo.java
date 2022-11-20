package listener.commands.member;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.time.OffsetDateTime;
import java.util.Objects;

public class userinfo {

    public static void command(Guild guild, TextChannel channel, Member member) {

        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("USER INFO " + member.getEffectiveName());
        builder.setColor(member.getColorRaw());
        /*
        Create Date
        */
        OffsetDateTime create = member.getTimeCreated();
        builder.getDescriptionBuilder().append("User Acc Create Date: ").append(create.getDayOfMonth()).append(".").append(create.getMonthValue()).append(".").append(create.getYear()).append("\n");
        /*
        JOIN DATE
        */
        OffsetDateTime join = member.getTimeJoined();
        builder.getDescriptionBuilder().append("User Join Date on this Server: ").append(join.getDayOfMonth()).append(".").append(join.getMonthValue()).append(".").append(join.getYear()).append("\n");
        /*
        USERID
        */
        builder.getDescriptionBuilder().append("UserID: ").append(member.getId()).append("\n");
        /*
        UserName
        */
        builder.getDescriptionBuilder().append("UserName: ").append(member.getUser().getName()).append("\n");
        /*
        NickName - if set
        */
        if (member.getNickname() != null) {
            builder.getDescriptionBuilder().append("NickName: ").append(member.getNickname()).append("\n");
        }
        /*
        Roles
        */
        builder.getDescriptionBuilder().append("Roles: ");
        for (Role role : member.getRoles()) {
            builder.getDescriptionBuilder().append("'").append(role.getName()).append("' ");
        }
        builder.getDescriptionBuilder().append("\n");
        /*
        Activities
        */
        if (!member.getActivities().isEmpty()) {
            builder.getDescriptionBuilder().append("Activities: ").append(member.getActivities()).append("\n");
        } else {
            builder.getDescriptionBuilder().append("Activities: 'unt\u00e4tig'\n");
        }
        int message_count = 0;
        for (TextChannel textChannel : guild.getTextChannels()) {
            for (Message message : textChannel.getIterableHistory()) {
                if (Objects.equals(message.getMember(), member)) {
                    message_count++;
                }
            }
        }
        builder.getDescriptionBuilder().append("Message count: ").append(message_count).append("\n");
        channel.sendMessage(builder.build()).queue();
    }
}