package listener.commands.member;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class rolesinfo {

    public static void command(Guild guild, TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("ROLES INFO");
        for (Role role : guild.getRoles()) {
            int role_count = 0;
            for (Member member : guild.getMembers()) {
                if (role.getName().equals("@everyone")) {
                    break;
                }
                if (member.getRoles().toString().contains(role.getName()))
                    role_count++;
            }
            if (role.getName().equals("@everyone")) {
                builder.getDescriptionBuilder().append("Name: ").append(role.getName()).append(" | ID: ").append(role.getId()).append(" | ").append(guild.getMembers().size()).append(" Member haben diese Rolle\n");
            } else {
                builder.getDescriptionBuilder().append("Name: ").append(role.getName()).append(" | ID: ").append(role.getId()).append(" | ").append(role_count).append(" Member haben diese Rolle\n");
            }
        }
        builder.getDescriptionBuilder().append("\n");
        channel.sendMessage(builder.build()).queue();
    }
}