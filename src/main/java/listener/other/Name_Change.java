package listener.other;

import config.PropertiesFile;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.Members;


public class Name_Change extends ListenerAdapter {

    Members m = new Members();

    @Override
    public void onUserUpdateName(UserUpdateNameEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            for (Guild guild : event.getJDA().getGuilds()) {
                for (Member member : guild.getMembers()) {
                    if (member.getId().equals(event.getUser().getId())) {
                        m.Member_CheckMemberOnFile(guild);
                    }
                }
            }
        }
    }

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            m.Member_CheckMemberOnFile(event.getGuild());
        }
    }
}