package listener.commands.member;

import config.PropertiesFile;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import other._guild.check.CheckCategory;
import other._stuff.ConsoleColor;
import other._stuff.LogBack;

import java.util.Objects;

public class _main_member extends ListenerAdapter {

    LogBack LB = new LogBack();

    /**
     * Listener for Commands
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            if (event.isFromType(ChannelType.TEXT)) {
                if (!event.getAuthor().isBot()) {
                    if (event.getMessage().getContentRaw().startsWith(">")) {
                        if (PropertiesFile.readsPropertiesFile("bot-channel", "config").isEmpty()) {
                            CheckCategory C_Category = new CheckCategory();
                            C_Category.checkingCategory(event.getGuild(), "gamecategory");
                            event.getMessage().delete().queue();
                        } else {
                            try {
                                if (!Objects.requireNonNull(event.getGuild().getTextChannelById(PropertiesFile.readsPropertiesFile("bot-channel", "config"))).toString().isEmpty()) {
                                }
                            } catch (NullPointerException e) {
                                try {
                                    if (!event.getGuild().getTextChannelsByName("bot-channel", false).get(0).getId().isEmpty()) {
                                        PropertiesFile.writePropertiesFile("bot-channel", event.getGuild().getTextChannelsByName("bot-channel", false).get(0).getId(), "config");
                                        event.getChannel().sendMessage("Es ist eine Fehler passiert, bitte f\u00fchre den __Command__ erneut aus!").queue();
                                    }
                                    return;
                                } catch (IndexOutOfBoundsException e1) {
                                    LB.log(Thread.currentThread().getName(), e1.getMessage(), "error");
                                }
                            }
                            if (event.getChannel().getId().equals(PropertiesFile.readsPropertiesFile("bot-channel", "config")) || (event.getGuild().getMember(event.getMember().getUser()).isOwner())) {

                                /*
                                HELP COMMAND
                                 */
                                if (event.getMessage().getContentRaw().equalsIgnoreCase(">help")) {
                                    help.command(event.getTextChannel());
                                    event.getMessage().addReaction("\uD83D\uDC4D").queue();

                                /*
                                PING COMMAND
                                */
                                } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">ping")) {
                                    ping.command(event.getTextChannel());
                                    event.getMessage().addReaction("\uD83D\uDC4D").queue();

                                /*
                                AMG COMMAND
                                (Add My Game)
                                */
                                } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">amg")) {
                                    amg.command(event.getGuild(), event.getTextChannel(), Objects.requireNonNull(event.getMember()));
                                    event.getMessage().addReaction("\uD83D\uDC4D").queue();

                                /*
                                CREDITS COMMAND
                                */
                                } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">credits")) {
                                    credits.command(event.getTextChannel());
                                    event.getMessage().addReaction("\uD83D\uDC4D").queue();

                                /*
                                PRINT OUT, IF THE MEMBER IS IN THE GAMELIST
                                */
                                } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">gamelist")) {
                                    gamelist.command(event.getTextChannel(), Objects.requireNonNull(event.getMember()));
                                    event.getMessage().addReaction("\uD83D\uDC4D").queue();

                                /*
                                USER INFO
                                */
                                } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">userinfo") || event.getMessage().getContentRaw().equalsIgnoreCase(">ui")) {
                                    userinfo.command(event.getGuild(), event.getTextChannel(), Objects.requireNonNull(event.getMember()));
                                    event.getMessage().addReaction("\uD83D\uDC4D").queue();

                                /*
                                Roles INFO
                                */
                                } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">rolesinfo") || event.getMessage().getContentRaw().equalsIgnoreCase(">ri")) {
                                    rolesinfo.command(event.getGuild(), event.getTextChannel());
                                    event.getMessage().addReaction("\uD83D\uDC4D").queue();

                                /*
                                League
                                */
                                } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">league")) {
                                    league.command(event.getTextChannel(), event.getMember(), event.getMessage());
                                    event.getMessage().addReaction("\uD83D\uDC4D").queue();

                                /*
                                GameRole
                                */
                                } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">gamerole")) {
                                    gamerole.command(event.getGuild(), event.getTextChannel(), event.getMember(), event.getMessage());
                                    event.getMessage().addReaction("\uD83D\uDC4D").queue();

                                /*
                                Blacklist for User
                                */
                                } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">blacklist") || event.getMessage().getContentRaw().equalsIgnoreCase(">bl")) {
                                    blacklist.command(event.getGuild(), event.getTextChannel(), event.getMember(), event.getMessage());
                                    event.getMessage().addReaction("\uD83D\uDC4D").queue();
                                }

                            } else {
                                event.getMessage().delete().queue();
                                event.getMessage().addReaction("\u274C").queue();
                            }
                        }
                    }
                }
            } else if (event.isFromType(ChannelType.PRIVATE) && (!event.getAuthor().isBot())) {
                LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "PRIVATECHAT" + ConsoleColor.reset + " > " + event.getAuthor().getName() + " schrieb *" +
                        event.getMessage().getContentRaw() + "*" + event.getMessage().getContentRaw() + "*", "info");
                event.getPrivateChannel().sendMessage("Sorry, aber hier h\u00f6re ich nicht auf dich!").queue();
                event.getPrivateChannel().sendMessage("Wenn dann gehe bitte auf dem Server. :wink:").queue();
            }
        }
    }
}