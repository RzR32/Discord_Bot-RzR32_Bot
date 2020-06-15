package listener.commands.member;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class help {

    public static void command(TextChannel channel) {
        channel.sendMessage(new EmbedBuilder().setTitle("HELP").setColor(Color.YELLOW).setDescription("" +
                "> >ping - Bot macht 'Pong!'\n" +
                "\n" +
                "> >amg - f\u00fcgt dein aktuelles Spiel in der Liste hinzu (wenn es noch nicht dabei ist)\n" +
                "\n" +
                "> >gamelist - gibt aus, ob du in der Liste bist (f\u00fcr #games)\n" +
                "\n" +
                "> >gamerole - zeigt 'Men\u00fc' f\u00fcr GameRole\n" +
                "\n" +
                "> >blacklist - zeigt 'Men\u00fc' f\u00fcr die Spiele Blacklist\n" +
                "\n" +
                "> >userinfo\n" +
                "\n" +
                "> >rolesinfo\n" +
                "\n" +
                "> >league <Summoner Name>\n" +
                "\n" +
                "> >credits\n" +
                "\n" +
                "> >help+ - Commands f\u00fcr den Server Owner").build()).queue();
    }
}