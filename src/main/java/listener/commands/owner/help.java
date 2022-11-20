package listener.commands.owner;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class help {

    public static void command(TextChannel channel) {
        channel.sendMessage(new EmbedBuilder().setTitle("HELP for Owner").setColor(Color.YELLOW).setDescription("" +
                "Diese Befehle sind nur f\u00fcr den Server Besitzer!\n" +
                "\n" +
                "> >twitch <name> (don´t update the counter instantly)\n" +
                "\n" +
                "> >playingcount\n" +
                "\n" +
                "> >agreement\n" +
                "\n" +
                "> >dm (**D**elete **M**essage´s)\n" +
                "\n" +
                "> >del <game>\n" +
                "\n" +
                "> >delete-bot (!Need to be confirm!)\n" +
                "\n" +
                "> >checkweb <game>\n" +
                "\n" +
                "> >edit <messageID to edit> <new game name>\n" +
                "\n" +
                "> >checkgame <game name>\n" +
                "\n" +
                "> >blacklist+ <list>\n" +
                "> __or__\n" +
                "> >blacklist+ <add/remove> <game>" +
                "\n" +
                "> >moveall <channelID from move> <channelID to move>\n" +
                "> __or__\n" +
                "> >moveall <channelID to move> (Sender Voicechannel is then first id)" +
                "\n").build()).queue();
    }
}