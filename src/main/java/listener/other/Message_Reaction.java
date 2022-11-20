package listener.other;

import config.PropertiesFile;
import config._File.RemoveStringFromFile;
import config._File.WriteStringToFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import other._stuff.BotSettings;

import java.awt.*;
import java.util.Objects;

public class Message_Reaction extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        /*settings*/
        if (Objects.requireNonNull(event.getMember()).getId().equals(Objects.requireNonNull(event.getGuild().getOwner()).getId())) {
            if (event.getReactionEmote().toString().equals("RE:U+1f44e")) {
                for (Message message : event.getChannel().getIterableHistory()) {
                    if (message.getId().equals(event.getMessageId())) {
                        PropertiesFile.writePropertiesFile(">" + message.getContentRaw() + "_on", "false", "config");
                        BotSettings.check_list(event.getChannel());
                    }
                }
            }
        }
        /*settings*/

        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            /*agreement*/
            if (PropertiesFile.readsPropertiesFile(">bot-zustimmung_on", "config").equals("true") && PropertiesFile.readsPropertiesFile(">games_on", "config").equals("true")) {
                if (event.getReaction().getMessageId().equals(PropertiesFile.readsPropertiesFile("agreement", "config"))) {
                    if (!event.getMember().getUser().isBot()) {
                        if (PropertiesFile.readsPropertiesFile(">bot-zustimmung_on", "config").equals("true") && PropertiesFile.readsPropertiesFile(">games_on", "config").equals("true")) {
                            WriteStringToFile WSTF = new WriteStringToFile();
                            WSTF.write(event.getGuild(), "list", event.getMember().getId());
                        }
                    }
                }
            }
            /*agreement*/
        }
    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
        /*settings*/
        if (Objects.requireNonNull(event.getMember()).getId().equals(Objects.requireNonNull(event.getGuild().getOwner()).getId())) {
            if (event.getReactionEmote().toString().equals("RE:U+1f44e")) {
                for (Message message : event.getChannel().getIterableHistory()) {
                    if (message.getId().equals(event.getMessageId())) {
                        PropertiesFile.writePropertiesFile(">" + message.getContentRaw() + "_on", "true", "config");
                        BotSettings.check_list(event.getChannel());
                    }
                }
            }
        }
        /*settings*/

        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            /*agreement*/
            if (PropertiesFile.readsPropertiesFile(">bot-zustimmung_on", "config").equals("true") && PropertiesFile.readsPropertiesFile(">games_on", "config").equals("true")) {
                if (event.getReaction().getMessageId().equals(PropertiesFile.readsPropertiesFile("agreement", "config"))) {
                    if (!event.getMember().getUser().isBot()) {
                        if (PropertiesFile.readsPropertiesFile(">bot-zustimmung_on", "config").equals("true") && PropertiesFile.readsPropertiesFile(">games_on", "config").equals("true")) {
                            RemoveStringFromFile WSTF = new RemoveStringFromFile();
                            WSTF.remove(event.getGuild(), "list", event.getMember().getId());
                        }
                    }
                }
            }
            /*agreement*/
        }
    }

    @Override
    public void onGuildMessageDelete(@NotNull GuildMessageDeleteEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            /*agreement*/
            if (PropertiesFile.readsPropertiesFile(">bot-zustimmung_on", "config").equals("true") && PropertiesFile.readsPropertiesFile(">games_on", "config").equals("true")) {
                String channelmention = event.getGuild().getTextChannelById(PropertiesFile.readsPropertiesFile("games", "config")).getAsMention();
                if (event.getMessageId().equals(PropertiesFile.readsPropertiesFile("agreement", "config"))) {
                    PropertiesFile.writePropertiesFile("agreement", "", "config");
                    event.getChannel().sendMessage(new EmbedBuilder().setTitle("Warum?!").setColor(Color.RED).setDescription("Warum hast du es gel\u00f6scht? Das ist wichtig f\u00f6r " + channelmention).build()).queue();
                }
            }
            /*agreement*/
        }
    }
}