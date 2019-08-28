package listener.commands;

import check_create.CheckCategory;
import config.PropertiesFile;
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
                if (event.getChannel().getId().equals(PropertiesFile.readsPropertiesFile("bot-channel")) || (event.getGuild().getMember(event.getMember().getUser()).isOwner())) {
                    /*
                    help for owner
                     */
                    if (event.getMessage().getContentRaw().equalsIgnoreCase(">help+")) {
                        if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                            event.getChannel().sendMessage(new EmbedBuilder().setTitle("HELP for Owner").setColor(Color.YELLOW).setDescription("" +
                                    "Diese Befehle sind nur für den Server Besitzer!\n" +
                                    "\n" +
                                    "> >c cateory - checking the category, if the exists (also the ID)\n" +
                                    "\n" +
                                    "> >agreement - remake the *agreement* messages\n" +
                                    "\n").build()).queue();
                            event.getMessage().addReaction("\uD83D\uDC4D").queue();
                        } else {
                            event.getChannel().sendMessage("Dieser Befehl ist nur für den Server Besitzer!").queue();
                            event.getMessage().addReaction("\u274C").queue();
                        }
                        //example
                        /*
                    } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">del_cat")) {
                        if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                            //code
                        } else {
                            event.getChannel().sendMessage("Dieser Befehl ist nur für den Server Besitzer!").queue();
                            event.getMessage().addReaction("\u274C").queue();
                        }
                         */
                        /*
                        c category (c = check)
                         */
                    } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">c category")) {
                        if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                            CheckCategory C_Category = new CheckCategory();
                            C_Category.StartChecking(event.getGuild());
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