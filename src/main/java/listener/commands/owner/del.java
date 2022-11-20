package listener.commands.owner;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;

public class del {

    public static void command(Guild guild, TextChannel channel, Message message, String[] argArray) {
        try {
            if (argArray[1] != null) {
                ArrayList<String> list = new ArrayList<>();
                for (String s : argArray) {
                    list.add(s);
                    if (list.size() == argArray.length) {
                        list.remove(argArray[0]);
                        if (list.isEmpty()) {
                            return;
                        }
                        String game = String.join(" ", list);

                        /*remove the one game from all - simplified over guild blacklist */
                        blacklist.Guild_Blacklist(guild, message, game, "add");

                        blacklist.Guild_Blacklist(guild, message, game, "remove");
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            channel.sendMessage("Hey, you forgot the game name!").queue();
        }
    }
}