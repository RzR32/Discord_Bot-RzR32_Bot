package listener.commands;

import check_create.CheckCategory;
import config.PropertiesFile;
import listener.member.Games_from_Member;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.ConsoleColor;
import other.LogBack;
import writeFile.WriteStringToFile;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;

public class Member_Commands extends ListenerAdapter {

    LogBack LB = new LogBack();

    /**
     * Listener for Commands
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            if (event.isFromType(ChannelType.TEXT)) {
                if (!event.getAuthor().isBot()) {
                    if (event.getMessage().getContentRaw().startsWith(">")) {
                        if (PropertiesFile.readsPropertiesFile("bot-channel").isEmpty()) {
                            CheckCategory C_Category = new CheckCategory();
                            C_Category.checkingCategory(event.getGuild(), "gamecategory");
                            event.getMessage().delete().queue();
                        } else {
                            try {
                                if (!event.getGuild().getTextChannelById(PropertiesFile.readsPropertiesFile("bot-channel")).toString().isEmpty()) {
                                }
                            } catch (NullPointerException e) {
                                try {
                                    if (!event.getGuild().getTextChannelsByName("bot-channel", false).get(0).getId().isEmpty()) {
                                        PropertiesFile.writePropertiesFile("bot-channel", event.getGuild().getTextChannelsByName("bot-channel", false).get(0).getId());
                                        event.getChannel().sendMessage("Es ist eine Fehler passiert, bitte führe den __Command__ erneut aus!").queue();
                                    }
                                    return;
                                } catch (IndexOutOfBoundsException e1) {
                                    LB.log(Thread.currentThread().getName(), e1.getMessage(), "error");
                                }
                            }
                            if (event.getChannel().getId().equals(PropertiesFile.readsPropertiesFile("bot-channel")) || (event.getGuild().getMember(event.getMember().getUser()).isOwner())) {

                                /*
                                HELP COMMAND
                                 */
                                if (event.getMessage().getContentRaw().equalsIgnoreCase(">help")) {
                                    event.getChannel().sendMessage(new EmbedBuilder().setTitle("HELP").setColor(Color.YELLOW).setDescription("" +
                                            "> >ping - Bot macht 'Pong!'\n" +
                                            "\n" +
                                            "> >amg - fügt dein aktuelles Spiel in der Liste hinzu (wenn es noch nicht dabei ist)\n" +
                                            "\n" +
                                            "> >gamelist - gibt aus, ob du in der Liste bist (für #games)\n" +
                                            "\n" +
                                            "> >gamerole - zeigt 'Menü' für GameRole\n" +
                                            "\n" +
                                            "> >blacklist - zeigt 'Menü' für die Spiele Blacklist\n" +
                                            "\n" +
                                            "> >userinfo\n" +
                                            "\n" +
                                            "> >league <Summoner Name>\n" +
                                            "\n" +
                                            "> >rolesinfo\n" +
                                            "\n" +
                                            "> >credits\n" +
                                            "\n" +
                                            "> >help+ - Commands für den Server Owner").build()).queue();

                                    event.getMessage().addReaction("\uD83D\uDC4D").queue();

                                /*
                                PING COMMAND
                                */
                                } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">ping")) {
                                    event.getChannel().sendMessage("Pong!").queue();

                                    event.getMessage().addReaction("\uD83D\uDC4D").queue();

                                /*
                                AMG COMMAND
                                (Add My Game)
                                */
                                } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">amg")) {
                                    if (!event.getMember().getActivities().isEmpty()) {
                                        Activity game = event.getMember().getActivities().get(0);
                                        if (game == null) {
                                            event.getChannel().sendMessage(event.getMember().getAsMention() + " du bist gerade nicht im Spiel!").queue();
                                        } else {
                                            if (PropertiesFile.readsPropertiesFile("games").isEmpty()) {
                                                LB.log(Thread.currentThread().getName(), "No *games* Channel set to send messages!", "error");
                                            } else {
                                                try {
                                                    if (!event.getGuild().getTextChannelById(PropertiesFile.readsPropertiesFile("games")).toString().isEmpty()) {
                                                    }
                                                } catch (NullPointerException e) {
                                                    LB.log(Thread.currentThread().getName(), "Channel *games* doesnt exist on this Server! (wrong id)", "error");
                                                    return;
                                                }
                                                if (event.getMember().getActivities().get(0).getType() == Activity.ActivityType.DEFAULT) {
                                                    WriteStringToFile WSTF = new WriteStringToFile();
                                                    WSTF.write(event.getGuild(), "games", event.getMember().getActivities().get(0).getName());
                                                    Games_from_Member userGameUpdateListener = new Games_from_Member();
                                                    userGameUpdateListener.GameRole(event.getGuild(), event.getMember().getUser().getId(), event.getMember(), event.getMember().getActivities().get(0).getName());
                                                }
                                            }
                                        }
                                    }

                                /*
                                CREDITS COMMAND
                                */
                                } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">credits")) {
                                    event.getChannel().sendMessage("Dieser Bot wurde erstellt von RzR32\nmit der Hilfe von HannesGames").queue();
                                    event.getMessage().addReaction("\uD83D\uDC4D").queue();

                                /*
                                PRINT OUT, IF THE MEMBER IS IN THE GAMELIST
                                */
                                } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">gamelist")) {
                                    try {
                                        List<String> lines = Files.readAllLines(Paths.get("config/list.txt"), StandardCharsets.UTF_8);
                                        if (!lines.contains(event.getMember().getUser().getId())) {
                                            event.getChannel().sendMessage(event.getMember().getAsMention() + "  du bist nicht in der Liste").queue();
                                        } else if (lines.contains(event.getMember().getUser().getId())) {
                                            event.getChannel().sendMessage(event.getMember().getAsMention() + " du bist in der Liste").queue();
                                        }
                                    } catch (IOException e) {
                                        LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
                                    }

                                /*
                                USER INFO
                                */
                                } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">userinfo") || event.getMessage().getContentRaw().equalsIgnoreCase(">ui")) {
                                    EmbedBuilder builder = new EmbedBuilder();

                                    builder.setTitle("USER INFO " + event.getMember().getEffectiveName());
                                    builder.setColor(event.getMember().getColorRaw());
                                    /*
                                    Create Date
                                    */
                                    OffsetDateTime create = event.getMember().getTimeCreated();
                                    builder.getDescriptionBuilder().append("User Acc Create Date: " + create.getDayOfMonth() + "." + create.getMonthValue() + "." + create.getYear() + "\n");
                                    /*
                                    JOIN DATE
                                    */
                                    OffsetDateTime join = event.getMember().getTimeJoined();
                                    builder.getDescriptionBuilder().append("User Join Date on this Server: " + join.getDayOfMonth() + "." + join.getMonthValue() + "." + join.getYear() + "\n");
                                    /*
                                    USERID
                                    */
                                    builder.getDescriptionBuilder().append("UserID: " + event.getMember().getId() + "\n");
                                    /*
                                    UserName
                                    */
                                    builder.getDescriptionBuilder().append("UserName: " + event.getMember().getUser().getName() + "\n");
                                    /*
                                    NickName - if set
                                    */
                                    if (event.getMember().getNickname() != null) {
                                        builder.getDescriptionBuilder().append("NickName: " + event.getMember().getNickname() + "\n");
                                    }
                                    /*
                                    Roles
                                    */
                                    builder.getDescriptionBuilder().append("Roles: ");
                                    for (Role role : event.getMember().getRoles()) {
                                        builder.getDescriptionBuilder().append("'" + role.getName() + "' ");
                                    }
                                    builder.getDescriptionBuilder().append("\n");
                                    /*
                                    Activities
                                    */
                                    if (!event.getMember().getActivities().isEmpty()) {
                                        builder.getDescriptionBuilder().append("Activities: " + event.getMember().getActivities() + "\n");
                                    } else {
                                        builder.getDescriptionBuilder().append("Activities: 'untätig'\n");
                                    }
                                    int message_count = 0;
                                    for (TextChannel textChannel : event.getGuild().getTextChannels()) {
                                        for (Message message : textChannel.getIterableHistory()) {
                                            if (message.getMember().equals(event.getMember())) {
                                                message_count++;
                                            }
                                        }
                                    }
                                    builder.getDescriptionBuilder().append("Message count: " + message_count + "\n");
                                    event.getChannel().sendMessage(builder.build()).queue();

                                /*
                                Roles INFO
                                */
                                } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">rolesinfo") || event.getMessage().getContentRaw().equalsIgnoreCase(">ri")) {
                                    EmbedBuilder builder = new EmbedBuilder();
                                    builder.setTitle("ROLES INFO");
                                    for (Role role : event.getGuild().getRoles()) {
                                        int role_count = 0;
                                        for (Member member : event.getGuild().getMembers()) {
                                            if (role.getName().equals("@everyone")) {
                                                break;
                                            }
                                            if (member.getRoles().toString().contains(role.getName()))
                                                role_count++;
                                        }
                                        if (role.getName().equals("@everyone")) {
                                            builder.getDescriptionBuilder().append("Name: " + role.getName() + " | ID: " + role.getId() + " | " + event.getGuild().getMembers().size() + " Member haben diese Rolle\n");
                                        } else {
                                            builder.getDescriptionBuilder().append("Name: " + role.getName() + " | ID: " + role.getId() + " | " + role_count + " Member haben diese Rolle\n");
                                        }
                                    }
                                    builder.getDescriptionBuilder().append("\n");
                                    event.getChannel().sendMessage(builder.build()).queue();
                                }

                            } else {
                                event.getMessage().delete().queue();
                            }
                        }
                    }
                }
            } else if (event.isFromType(ChannelType.PRIVATE) && (!event.getAuthor().isBot())) {
                LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "PRIVATECHAT" + ConsoleColor.reset + " > " + event.getAuthor().getName() + " schrieb *" +
                        event.getMessage().getContentRaw() + "*" + event.getMessage().getContentRaw() + "*", "info");
                event.getPrivateChannel().sendMessage("Sorry, aber hier höre ich nicht auf dich!").queue();
                event.getPrivateChannel().sendMessage("Wenn dann gehe bitte auf dem Server. :wink:").queue();
            }
        }
    }
}