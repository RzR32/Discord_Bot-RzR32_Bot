package listener.commands;

import config.PropertiesFile;
import count.TwitchFollowerCount;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

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
                                    "> >twitch <name>\n" +
                                    "\n" +
                                    "> >settings\n" +
                                    "\n").build()).queue();
                            event.getMessage().addReaction("\uD83D\uDC4D").queue();
                        } else {
                            event.getChannel().sendMessage("Dieser Befehl ist nur für den Server Besitzer!").queue();
                            event.getMessage().addReaction("\u274C").queue();
                        }
                        //example
                        /*
                    } else if (argArray[0].equalsIgnoreCase(">help+")) {
                        if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                            //code
                        } else {
                            event.getChannel().sendMessage("Dieser Befehl ist nur für den Server Besitzer!").queue();
                            event.getMessage().addReaction("\u274C").queue();
                        }
                         */
                    } else if (argArray[0].equalsIgnoreCase(">twitch")) {
                        if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                            PropertiesFile.writePropertiesFile("twitchname", argArray[1]);
                            TwitchFollowerCount TFC = new TwitchFollowerCount();
                            TFC._stop(event.getGuild());
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