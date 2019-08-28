package listener.commands;

import check_create.CheckCategory;
import config.PropertiesFile;
import listener.other.Games_from_Member;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.ConsoleColor;
import other.LogBack;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Member_Commands extends ListenerAdapter {

    LogBack LB = new LogBack();

    private int checkFileWrite = 0;

    /**
     * Listener for Commands
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
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
                            //help
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
                                        "> >credits\n" +
                                        "\n" +
                                        "> >help+ - Commands für den Server Owner").build()).queue();

                                event.getMessage().addReaction("\uD83D\uDC4D").queue();

                                //ping
                            } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">ping")) {
                                event.getChannel().sendMessage("Pong!").queue();

                                event.getMessage().addReaction("\uD83D\uDC4D").queue();

                                //amg (Add My Game) DEBUG
                            } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">amg")) {
                                if (!event.getMember().getActivities().isEmpty()) {
                                    Activity game = event.getMember().getActivities().get(0);
                                    if (game == null) {
                                        event.getChannel().sendMessage(event.getMember().getAsMention() + " du bist gerade nicht im Spiel!").queue();
                                    } else {
                                        if (PropertiesFile.readsPropertiesFile("games").isEmpty()) {
                                            LB.log(Thread.currentThread().getName(), "No *games* Channel set to send messages!", "error");
                                            event.getChannel().sendMessage("ERROR: No Channel set to send messages!").queue();
                                            event.getChannel().sendMessage("Setup Channel with '>set channel game'").queue();
                                        } else {
                                            try {
                                                if (!event.getGuild().getTextChannelById(PropertiesFile.readsPropertiesFile("games")).toString().isEmpty()) {
                                                }
                                            } catch (NullPointerException e) {
                                                LB.log(Thread.currentThread().getName(), "Channel *games* doesnt exist on this Server! (wrong id)", "error");
                                                return;
                                            }
                                            if (event.getMember().getActivities().get(0).getType() != Activity.ActivityType.STREAMING) {
                                                writeFile(game.getName());

                                                if (checkFileWrite == 1) {
                                                    String channelid = PropertiesFile.readsPropertiesFile("games");
                                                    event.getChannel().sendMessage(event.getMember().getAsMention() + "Dein Spiel " + game.getName() + " wurde hinzugefügt!").queue();
                                                    event.getJDA().getTextChannelById(channelid).sendMessage(new EmbedBuilder().setTitle(event.getMember().getActivities().get(0).getName()).setColor(Color.GREEN).setDescription(new SimpleDateFormat("dd.MM.YY").format(Calendar.getInstance().getTime())).build()).queue();
                                                    Games_from_Member userGameUpdateListener = new Games_from_Member();
                                                    userGameUpdateListener.GameRole(event.getGuild(), event.getMember().getUser().getId(), event.getMember(), event.getMember().getActivities().get(0).getName());
                                                    --checkFileWrite;
                                                } else {
                                                    event.getChannel().sendMessage(event.getMember().getAsMention() + " Sorry, das Spiel ist bereits vorhanden!").queue();
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    event.getChannel().sendMessage(event.getMember().getAsMention() + " Sorry, aber du bist gerade nicht im Spiel").queue();
                                }
                                event.getMessage().addReaction("\uD83D\uDC4D").queue();

                                //credits
                            } else if (event.getMessage().getContentRaw().equalsIgnoreCase(">credits")) {
                                event.getChannel().sendMessage("Dieser Bot wurde erstellt von RzR32\nmit der Hilfe von HannesGames").queue();
                                event.getMessage().addReaction("\uD83D\uDC4D").queue();

                                //check for *gamerole* list
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

    /**
     * Writes game into games.txt if game is not in file
     *
     * @param game Name from Game
     */
    private void writeFile(String game) {
        try {
            //file writer
            BufferedWriter writer = new BufferedWriter(new FileWriter("config/games.txt", true));
            //file reader
            BufferedReader reader = new BufferedReader(new FileReader("config/games.txt", StandardCharsets.UTF_8));

            List<String> lines = Files.readAllLines(Paths.get("config/games.txt"), StandardCharsets.UTF_8);
            if (!lines.contains(game)) {
                writer.write(game + "\n");
                writer.close();
                ++checkFileWrite;
            } else {
                if (checkFileWrite == 1) {
                    --checkFileWrite;
                }
            }
            reader.close();
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }
}