package listener.commands.member;

import net.dv8tion.jda.api.entities.TextChannel;

public class ping {

    public static void command(TextChannel channel) {
        channel.sendMessage("Pong! :P").queue();
    }
}