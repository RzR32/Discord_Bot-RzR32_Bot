package listener.commands.owner;

import config.PropertiesFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Objects;

public class edit {

    public static void command(Guild guild, String[] argArray) {
        String MessageID = argArray[1];
        if (argArray[1] != null) {
                /*
                compine all arguments
                */
            ArrayList<String> list = new ArrayList<>();
            for (String s : argArray) {
                list.add(s);
                if (list.size() == argArray.length) {
                    list.remove(argArray[0]);
                    list.remove(argArray[1]);
                    if (list.isEmpty()) {
                        return;
                    }
                }
            }
            String game_name = String.join(" ", list);

            EmbedBuilder builder = new EmbedBuilder();

            for (Message x_message : Objects.requireNonNull(guild.getTextChannelById(PropertiesFile.readsPropertiesFile("games", "config"))).getIterableHistory()) {
                if (x_message.getId().equals(MessageID)) {
                    x_message.editMessage(builder.setDescription(x_message.getEmbeds().get(0).getDescription()).setColor(x_message.getEmbeds().get(0).getColor()).setTitle(game_name).build()).queue();
                    break;
                }
            }
        }
    }
}