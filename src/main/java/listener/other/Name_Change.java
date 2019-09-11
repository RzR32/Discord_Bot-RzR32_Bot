package listener.other;

import config.PropertiesFile;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.Members;


public class Name_Change extends ListenerAdapter {

    Members m = new Members();

    /*
    give´s out error, maybe remove...
     */
    @Override
    public void onUserUpdateName(UserUpdateNameEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            for (String id : Members.GetGuildsIDs()) {
                if (event.getJDA().getGuildById(id).getMemberById(event.getUser().getId()) != null) {
                /*
                member is on one guild > change name!
                 */
                    m.Member_CheckMemberOnFile(event.getJDA().getGuildById(id));
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