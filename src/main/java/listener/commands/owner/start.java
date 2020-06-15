package listener.commands.owner;

import config.PropertiesFile;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import other.starting_all;

public class start {


    public static void command(Guild guild, Message message) {
        message.delete().queue();

        PropertiesFile.writePropertiesFile("first-startup", "false", "config");

        starting_all.make_start(guild);
    }
}
