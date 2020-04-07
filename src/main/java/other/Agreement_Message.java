package other;

import config.PropertiesFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Agreement_Message {

    LogBack LB = new LogBack();

    public void Message(Guild guild, GuildChannel channel) {
        try {
            File dir = new File("config/");
            boolean exists = dir.exists();
            if (exists) {
                URL url = new URL("https://i.ibb.co/hYqS2SQ/bild.png");
                BufferedImage image = ImageIO.read(url);
                ImageIO.write(image, "png", new File("config/bild.png"));
                File file = new File("config/bild.png");

                String channelmention = guild.getTextChannelById(PropertiesFile.readsPropertiesFile("games")).getAsMention();
                /*
                first message
                */
                guild.getTextChannelById(channel.getId()).sendMessage(new EmbedBuilder().setTitle("Zustimmung").setColor(Color.GREEN).setDescription(
                        "Wenn du dieser Nachricht eine Reaktion hinzufügst, dann bist du damit einverstanden, " +
                                "das der Bot deine gespielten Spiele 'speichert' und dir als Rolle hinzufügt damit man diese 'Erwähnen' kann :wink: \n" +
                                "Trotz ohne Zustimmung, werden deine Spiele __ohne__ Namen in den Channel " + channelmention + " gepostet!").build()).queue(message -> {
                    PropertiesFile.writePropertiesFile("agreement", message.getId());
                    message.addReaction("\uD83D\uDC4D").queue();
                });
                /*
                second message
                */
                guild.getTextChannelById(channel.getId()).sendMessage("```\n" +
                        "- da ich den Bot noch nicht richtig testen konnte, könnten Fehler auftreten\n" +
                        "(was ich aber nicht hoffe)\n" +
                        "- wenn eine Rolle erstellt wird, die nichts mit einem Spiel zutun hat, erlaube ich mir diese zu entfernen! (z. B. *Google Chrome*)\n" +
                        "- niemand ist verpflichtet die *Zustimmung* anzunehmen!\n" +
                        "- fragt nicht warum ich das hier gemacht habe (Ich hab wohl zu viel Langeweile )\n" +
                        "```").queue();
                /*
                third message
                */
                guild.getTextChannelById(channel.getId()).sendFile(file).queue();
                if (guild.getRolesByName("GameRole", false).get(0) != null) {
                }
            } else {
                LB.log(Thread.currentThread().getName(), "Folder not found!", "error");
            }
        } catch (IndexOutOfBoundsException e) {
            guild.createRole().setName("GameRole").queue();
            LB.log(Thread.currentThread().getName(), "Rolle erstellt um *GameRole* zu verwalten!", "info");
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }
}