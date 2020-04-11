package listener.commands;

import config.PropertiesFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.LogBack;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DEactivate_Commands extends ListenerAdapter {

    private final File file = new File("config/config.prop");
    LogBack LB = new LogBack();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            if (event.isFromType(ChannelType.TEXT)) {
                if (!event.getAuthor().isBot()) {
                    String[] argArray = event.getMessage().getContentRaw().split(" ");
                    try {
                        EmbedBuilder builder = new EmbedBuilder().setTitle("Settings");
                        if (argArray[0].equalsIgnoreCase(">settings")) {
                            /*
                            LIST
                             */
                            if (argArray[1].equalsIgnoreCase("list")) {
                                List<String> lines = Files.readAllLines(Paths.get(String.valueOf(file)), StandardCharsets.UTF_8);
                                for (String string : lines) {
                                    if (string.startsWith(">")) {
                                        if (string.contains("false")) {
                                            builder.getDescriptionBuilder().append(" > **" + string + "**\n");
                                        } else {
                                            builder.getDescriptionBuilder().append(" > " + string + "\n");
                                        }
                                    }
                                }
                                event.getChannel().sendMessage(builder.build()).queue();
                            } else if (argArray[1].equalsIgnoreCase("set")) {
                                if (argArray.length == 4) {
                                    if (argArray[3].equals("true") || argArray[3].equals("false")) {
                                        if (PropertiesFile.readsPropertiesFile(argArray[2]) != null) {
                                            PropertiesFile.writePropertiesFile(argArray[2], argArray[3]);
                                        } else {
                                            event.getChannel().sendMessage("Error: Falscher Key!\n>settings set **>**rolecount**_on** true").queue();
                                        }
                                    } else {
                                        event.getChannel().sendMessage("Error: Entweder **true** oder **false**, nichts anderes!").queue();
                                    }
                                } else {
                                    event.getChannel().sendMessage("Error: **>settings set <key> <true/false>**").queue();
                                }
                            } else {
                                event.getChannel().sendMessage(new EmbedBuilder().setTitle("Settings").setColor(Color.RED).setDescription(">settings list\n>settings set <key> <true/false>" +
                                        "\nThis only disable the function, it don´t delete the channel!").build()).queue();
                            }
                        }
                    } catch (IOException e) {
                        LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
                    } catch (ArrayIndexOutOfBoundsException e) {
                        event.getChannel().sendMessage(new EmbedBuilder().setTitle("Settings").setColor(Color.RED).setDescription(">settings list\n>settings set <key> <true/false>" +
                                "\nThis only disable the function, it don´t delete the channel!").build()).queue();
                    }
                }
            }
        }
    }
}