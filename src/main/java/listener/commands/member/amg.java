package listener.commands.member;

import config.PropertiesFile;
import config._File.WriteStringToFile;
import listener.member._Activity;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import other._stuff.LogBack;

import java.util.Objects;

public class amg {

    private static final LogBack LB = new LogBack();

    public static void command(Guild guild, TextChannel channel, Member member) {
        if (!member.getActivities().isEmpty()) {
            Activity game = member.getActivities().get(0);
            if (game == null) {
                channel.sendMessage(member.getAsMention() + " du bist gerade nicht im Spiel!").queue();
            } else {
                if (PropertiesFile.readsPropertiesFile("games", "config").isEmpty()) {
                    LB.log(Thread.currentThread().getName(), "No *games* Channel set to send messages!", "error");
                } else {
                    try {
                        if (!Objects.requireNonNull(guild.getTextChannelById(PropertiesFile.readsPropertiesFile("games", "config"))).toString().isEmpty()) {
                        }
                    } catch (NullPointerException e) {
                        LB.log(Thread.currentThread().getName(), "Channel *games* doesnt exist on this Server! (wrong id)", "error");
                        return;
                    }
                    if (member.getActivities().get(0).getType() == Activity.ActivityType.DEFAULT) {
                        WriteStringToFile WSTF = new WriteStringToFile();
                        WSTF.write(guild, "games", member.getActivities().get(0).getName());
                        _Activity userGameUpdateListener = new _Activity();
                        userGameUpdateListener.GameRole(guild, member.getUser().getId(), member, member.getActivities().get(0).getName());
                    }
                }
            }
        }
    }
}