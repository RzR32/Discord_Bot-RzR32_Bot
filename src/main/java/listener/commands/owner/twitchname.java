package listener.commands.owner;

import config.PropertiesFile;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;

public class twitchname {

    public static void command(TextChannel channel, String[] argArray) {
        try {
            if (argArray[1] != null) {
                /*
                compine all arguments
                */
                ArrayList<String> list = new ArrayList<>();
                for (String s : argArray) {
                    list.add(s);
                    if (list.size() == argArray.length) {
                        list.remove(argArray[0]);
                        if (list.isEmpty()) {
                            return;
                        }
                    }
                }
                String twitchname = String.join(" ", list);
                PropertiesFile.writePropertiesFile("twitchname", twitchname, "config");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            channel.sendMessage("Error: It´s look that you forgot the name of the Twitch User").queue();
        }
    }
}