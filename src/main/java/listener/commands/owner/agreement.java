package listener.commands.owner;

import config.PropertiesFile;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import other._guild.Agreement_Message;

public class agreement {

    private static final Agreement_Message A_M = new Agreement_Message();

    public static void command(Guild guild, TextChannel channel, Message message) {
        try {
            if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
                if (message.getContentRaw().equalsIgnoreCase(">agreement")) {
                    if (PropertiesFile.readsPropertiesFile(">bot-zustimmung_on", "config").equals("true") && PropertiesFile.readsPropertiesFile(">games_on", "config").equals("true")) {
                        for (Message x_message : channel.getIterableHistory()) {
                            x_message.delete().complete();
                        }
                        PropertiesFile.writePropertiesFile("agreement", "", "config");
                        A_M.Message(guild, channel);
                    }
                }
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            A_M.Message(guild, channel);
        }
    }
}
