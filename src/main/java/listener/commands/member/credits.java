package listener.commands.member;

import net.dv8tion.jda.api.entities.TextChannel;

public class credits {

    public static void command(TextChannel channel) {
        channel.sendMessage("Dieser Bot wurde erstellt von RzR32\nmit der Hilfe von HannesGames").queue();
    }
}