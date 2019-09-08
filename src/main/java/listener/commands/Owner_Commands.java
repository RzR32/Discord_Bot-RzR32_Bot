package listener.commands;

import config.PropertiesFile;
import count.Counter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import writeFile.RemoveStringFromFile;

import java.awt.*;
import java.util.ArrayList;

public class Owner_Commands extends ListenerAdapter {

    /**
     * Listener for Commands
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            if (!event.getAuthor().isBot()) {
                if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                    String[] argArray = event.getMessage().getContentRaw().split(" ");
                    /*
                    help for owner
                     */
                    if (argArray[0].equalsIgnoreCase(">help+")) {
                        if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                            event.getChannel().sendMessage(new EmbedBuilder().setTitle("HELP for Owner").setColor(Color.YELLOW).setDescription("" +
                                    "Diese Befehle sind nur für den Server Besitzer!\n" +
                                    "\n" +
                                    "> >twitch <name> (maybe don´t update the counter instantly)\n" +
                                    "\n" +
                                    "> >settings\n" +
                                    "\n" +
                                    "> >del <game>\n" +
                                    "\n").build()).queue();
                            event.getMessage().addReaction("\uD83D\uDC4D").queue();
                        } else {
                            event.getChannel().sendMessage("Dieser Befehl ist nur für den Server Besitzer!").queue();
                            event.getMessage().addReaction("\u274C").queue();
                        }

                        /*
                        set the twitchname
                         */
                    } else if (argArray[0].equalsIgnoreCase(">twitch")) {
                        if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                            PropertiesFile.writePropertiesFile("twitchname", argArray[1]);
                        } else {
                            event.getChannel().sendMessage("Dieser Befehl ist nur für den Server Besitzer!").queue();
                            event.getMessage().addReaction("\u274C").queue();
                        }

                        /*
                        set *first-startup* to false
                         */
                    } else if (argArray[0].equalsIgnoreCase(">ready")) {
                        if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                            PropertiesFile.writePropertiesFile("first-startup", "false");
                            for (TextChannel textChannel : event.getGuild().getTextChannels()) {
                                if (textChannel.getName().equals(event.getMember().getId())) {
                                    textChannel.delete().queue();
                                }
                            }
                            System.exit(0);
                        } else {
                            event.getChannel().sendMessage("Dieser Befehl ist nur für den Server Besitzer!").queue();
                            event.getMessage().addReaction("\u274C").queue();
                        }

                        /*
                        > remove game from file
                        > delete the gamerole (if created)
                        > delete the ONE message in #games
                         */
                    } else if (argArray[0].equalsIgnoreCase(">del")) {
                        if (argArray[1] == null) {
                            event.getChannel().sendMessage("Hey, you forgot the game name!").queue();
                        } else {
                            if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                                ArrayList<String> list = new ArrayList<>();
                                for (int x = 0; x < argArray.length; x++) {
                                    list.add(argArray[x]);
                                    if (list.size() == argArray.length) {
                                        list.remove(argArray[0]);
                                        if (list.isEmpty()) {
                                            return;
                                        }
                                        /*
                                        liststring = game
                                         */
                                        String liststring = String.join(" ", list);
                                        /*
                                        remove game from file
                                         */
                                        RemoveStringFromFile RSFF = new RemoveStringFromFile();
                                        RSFF.remove(event.getGuild(), "games", liststring);
                                        /*
                                        delete the gamerole
                                         */
                                        for (Role role : event.getGuild().getRoles()) {
                                            if (role.getName().equalsIgnoreCase(liststring)) {
                                                role.delete().queue();
                                                break;
                                            }
                                        }
                                        /*
                                        delete the ONE message in #games
                                         */
                                        for (Message message : event.getGuild().getTextChannelById(PropertiesFile.readsPropertiesFile("games")).getIterableHistory()) {
                                                if (message.getEmbeds().get(0).getTitle().equalsIgnoreCase(liststring)) {
                                                    message.delete().queue();
                                                    break;
                                                }
                                        }
                                        /*
                                        call gamecounter
                                         */
                                        Counter c = new Counter();
                                        c.getint(event.getGuild(), "gamecount");
                                        event.getMessage().addReaction("\uD83D\uDC4D").queue();
                                    }
                                }
                            } else {
                                event.getChannel().sendMessage("Dieser Befehl ist nur für den Server Besitzer!").queue();
                                event.getMessage().addReaction("\u274C").queue();
                            }
                        }
                    }
                }
            }
        }
    }
}