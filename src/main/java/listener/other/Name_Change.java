package listener.other;

import config.PropertiesFile;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.Members;


public class Name_Change extends ListenerAdapter {

    Members m = new Members();

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            m.Member_CheckMemberOnFile(event.getGuild());
        }
    }
}