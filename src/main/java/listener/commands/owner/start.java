package listener.commands.owner;

import config.PropertiesFile;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import other._stuff.starting_all;

public class start {


    public static void command(Guild guild, Message message) {
        message.delete().queue();

        PropertiesFile.writePropertiesFile("first_startup", "false", "config");

        starting_all sa = new starting_all();
        sa.make_start(guild);
    }
}
