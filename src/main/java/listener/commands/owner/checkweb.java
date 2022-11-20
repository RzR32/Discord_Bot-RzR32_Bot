package listener.commands.owner;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import other._stuff.CheckGameOnWebsite;

import java.util.ArrayList;

public class checkweb {

    public static void command(TextChannel channel, String[] argArray) {
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
            String game_name = String.join(" ", list);

            CheckGameOnWebsite GIS = new CheckGameOnWebsite();
            EmbedBuilder builder = new EmbedBuilder().setTitle(game_name);

            builder.appendDescription("\n");

            for (String string : GIS.startcheck(game_name)) {
                if (!string.contains("null")) {
                    builder.appendDescription(string + "\n");
                }
            }

            channel.sendMessage(builder.build()).queue();
        }
    }
}