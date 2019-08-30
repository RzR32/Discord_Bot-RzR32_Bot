package listener.other;

import config.PropertiesFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.Agreement_Message;
import writeFile.RemoveStringFromFile;
import writeFile.WriteStringToFile;

import java.awt.*;

public class Message_Reaction extends ListenerAdapter {

    Agreement_Message A_M = new Agreement_Message();

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (PropertiesFile.readsPropertiesFile(">bot-zustimmung_on").equals("true") && PropertiesFile.readsPropertiesFile(">games_on").equals("true")) {
            if (event.getReaction().getMessageId().equals(PropertiesFile.readsPropertiesFile("agreement"))) {
                if (!event.getMember().getUser().isBot()) {
                    if (PropertiesFile.readsPropertiesFile(">bot-zustimmung_on").equals("true") && PropertiesFile.readsPropertiesFile(">games_on").equals("true")) {
                        WriteStringToFile WSTF = new WriteStringToFile();
                        WSTF.write(event.getGuild(), "list", event.getMember().getId());
                    }
                }
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
        if (PropertiesFile.readsPropertiesFile(">bot-zustimmung_on").equals("true") && PropertiesFile.readsPropertiesFile(">games_on").equals("true")) {
            if (event.getReaction().getMessageId().equals(PropertiesFile.readsPropertiesFile("agreement"))) {
                if (!event.getMember().getUser().isBot()) {
                    if (PropertiesFile.readsPropertiesFile(">bot-zustimmung_on").equals("true") && PropertiesFile.readsPropertiesFile(">games_on").equals("true")) {
                        RemoveStringFromFile WSTF = new RemoveStringFromFile();
                        WSTF.remove(event.getGuild(), "list", event.getMember().getId());
                    }
                }
            }
        }
    }

    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        if (PropertiesFile.readsPropertiesFile(">bot-zustimmung_on").equals("true") && PropertiesFile.readsPropertiesFile(">games_on").equals("true")) {
            String channelmention = event.getGuild().getTextChannelById(PropertiesFile.readsPropertiesFile("games")).getAsMention();
            if (event.getMessageId().equals(PropertiesFile.readsPropertiesFile("agreement"))) {
                PropertiesFile.writePropertiesFile("agreement", "");
                event.getChannel().sendMessage(new EmbedBuilder().setTitle("Warum?!").setColor(Color.RED).setDescription("Warum hast du es gelöscht? Das ist wichtig für " + channelmention).build()).queue();
            }
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                try {
                    if (event.getMessage().getContentRaw().equalsIgnoreCase(">agreement")) {
                        if (PropertiesFile.readsPropertiesFile(">bot-zustimmung_on").equals("true") && PropertiesFile.readsPropertiesFile(">games_on").equals("true")) {
                            for (Message message : event.getChannel().getIterableHistory()) {
                                message.delete().complete();
                            }
                            PropertiesFile.writePropertiesFile("agreement", "");
                            A_M.Message(event.getGuild(), event.getTextChannel());
                        }
                    }
                } catch (IllegalArgumentException | NullPointerException e) {
                    A_M.Message(event.getGuild(), event.getTextChannel());
                }
            }
        }
    }
}
