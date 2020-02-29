package listener.member;

import check_create.CheckChannel;
import config.PropertiesFile;
import count.GamePlayingCount;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.ConsoleColor;
import other.LogBack;
import writeFile.WriteStringToFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Games_from_Member extends ListenerAdapter {

    LogBack LB = new LogBack();

    public void onUserActivityStart(UserActivityStartEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            if (!event.getMember().getUser().isBot()) {
                CheckChannel C_Channel = new CheckChannel();
                C_Channel.checkingChannel(event.getGuild(), "games");
                C_Channel.checkingChannel(event.getGuild(), "playingcount");
                Forwarded(event.getGuild(), "start", event.getNewActivity().getType(), event.getMember(), null, event.getNewActivity());
                EditMessagesFromGames(event.getGuild(), event.getNewActivity());
            }
        }
    }

    public void onUserActivityEnd(UserActivityEndEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            if (!event.getMember().getUser().isBot()) {
                CheckChannel C_Channel = new CheckChannel();
                C_Channel.checkingChannel(event.getGuild(), "games");
                C_Channel.checkingChannel(event.getGuild(), "playingcount");
                Forwarded(event.getGuild(), "end", event.getOldActivity().getType(), event.getMember(), event.getOldActivity(), null);
                EditMessagesFromGames(event.getGuild(), event.getOldActivity());
            }
        }
    }

    private void Forwarded(Guild guild, String start_end, Activity.ActivityType short_type, Member member, Activity old_game, Activity new_game) {
        String username = member.getEffectiveName();
        String newgame;
        String oldgame;
        if (new_game == null) {
            newgame = null;
        } else {
            newgame = new_game.getName();
        }
        if (old_game == null) {
            oldgame = null;
        } else {
            oldgame = old_game.getName();
        }

        SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss.SSS");
        Date D = new Date();
        String date = SDF.format(D);

        String s_prefix = ConsoleColor.red + date + " " + ConsoleColor.green + "[" + Thread.currentThread().getName() + "] " + ConsoleColor.yellow + "INFO - ";
        String s_mid = ConsoleColor.cyan + " > " + username + ConsoleColor.reset + ConsoleColor.Bblue;
        String s_suffix_new = ConsoleColor.reset + ConsoleColor.white + newgame + ConsoleColor.reset;
        String s_suffix_old = ConsoleColor.reset + ConsoleColor.white + oldgame + ConsoleColor.reset;

        if (short_type == Activity.ActivityType.DEFAULT) {
            if (start_end.equals("start")) {
                WriteStringToFile WSTF = new WriteStringToFile();
                WSTF.write(guild, "games", newgame);
                GameRole(guild, member.getId(), member, newgame);
                System.out.println(s_prefix + ConsoleColor.backblue + "GAME" + ConsoleColor.reset + s_mid + " spielt nun " + s_suffix_new);
            } else {
                System.out.println(s_prefix + ConsoleColor.backblue + "GAME" + ConsoleColor.reset + s_mid + " spielt nichtmehr " + s_suffix_old);
            }
        } else if (short_type == Activity.ActivityType.LISTENING) {
            if (start_end.equals("start")) {
                System.out.println(s_prefix + ConsoleColor.backblue + "LISTEN" + ConsoleColor.reset + s_mid + " hört nun " + s_suffix_new);
            } else {
                System.out.println(s_prefix + ConsoleColor.backblue + "LISTEN" + ConsoleColor.reset + s_mid + " hört nichtmehr " + s_suffix_old);
            }
        } else if (short_type == Activity.ActivityType.STREAMING) {
            if (start_end.equals("start")) {
                System.out.println(s_prefix + ConsoleColor.backblue + "STREAM" + ConsoleColor.reset + s_mid + " streamt nun " + s_suffix_new);
            } else {
                System.out.println(s_prefix + ConsoleColor.backblue + "STREAM" + ConsoleColor.reset + s_mid + " streamt nichtmehr " + s_suffix_old);
            }
        } else if (short_type == Activity.ActivityType.WATCHING) {
            if (start_end.equals("start")) {
                System.out.println(s_prefix + ConsoleColor.backblue + "WATCH" + ConsoleColor.reset + s_mid + " schaut nun " + s_suffix_new);
            } else {
                System.out.println(s_prefix + ConsoleColor.backblue + "WATCH" + ConsoleColor.reset + s_mid + " schaut nichtmehr " + s_suffix_old);
            }
        }
        GamePlayingCount gamePlayingCount = new GamePlayingCount();
        if (start_end.equals("start")) {
            gamePlayingCount.ForwardPlayingGame(guild, new_game);
        } else {
            gamePlayingCount.ForwardPlayingGame(guild, old_game);
        }
    }

    /**
     * create a Role from the Game, if the user agreed
     *
     * @param guild  Name from guild
     * @param userid ID from user
     * @param member get the member
     * @param game   Name of the game
     */
    public void GameRole(Guild guild, String userid, Member member, String game) {
        try {
            if (PropertiesFile.readsPropertiesFile(">bot-zustimmung_on").equals("true") && PropertiesFile.readsPropertiesFile(">games_on").equals("true")) {
                File dir = new File("config/blacklist/");
                /*
                Check if user is in the "agree" list
                */
                BufferedWriter writer = new BufferedWriter(new FileWriter("config/list.txt", StandardCharsets.UTF_8, true));
                BufferedReader reader = new BufferedReader(new FileReader("config/list.txt", StandardCharsets.UTF_8));
                reader.close();
                writer.close();
                List<String> lines = Files.readAllLines(Paths.get("config/list.txt"), StandardCharsets.UTF_8);
                if (lines.contains(userid)) {
                    /*
                    Check if the game is blacklisted or not
                    */
                    BufferedWriter writer1 = new BufferedWriter(new FileWriter("config/blacklist/GuildBlackList.txt", StandardCharsets.UTF_8, true));
                    BufferedReader reader1 = new BufferedReader(new FileReader("config/blacklist/GuildBlackList.txt", StandardCharsets.UTF_8));
                    reader1.close();
                    writer1.close();
                    List<String> lines1 = Files.readAllLines(Paths.get("config/blacklist/GuildBlackList.txt"), StandardCharsets.UTF_8);
                    if (!lines1.contains(game)) {
                        /*
                        Check if the game is not in the userblacklist
                        */
                        BufferedWriter writer2 = new BufferedWriter(new FileWriter("config/blacklist/" + userid + ".txt", StandardCharsets.UTF_8, true));
                        BufferedReader reader2 = new BufferedReader(new FileReader("config/blacklist/" + userid + ".txt", StandardCharsets.UTF_8));
                        reader2.close();
                        writer2.close();
                        List<String> lines2 = Files.readAllLines(Paths.get("config/blacklist/" + userid + ".txt"), StandardCharsets.UTF_8);
                        if (!lines2.contains(game)) {
                            if (guild.getRolesByName(game, true).get(0) != null) {
                                if (!member.getRoles().toString().contains(game)) {
                                    guild.addRoleToMember(member, guild.getRolesByName(game, true).get(0)).queue();
                                    LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "GAMEROLE" + ConsoleColor.reset + ConsoleColor.cyan + " > " + ConsoleColor.white + " Ein User wurde eine Gamerole hinzugefügt", "info");
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        } catch (IndexOutOfBoundsException e1) {
            guild.createRole().setName(game).setMentionable(true).queue(role ->
                    guild.addRoleToMember(member, role).queue());
            LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "GAMEROLE" + ConsoleColor.reset + ConsoleColor.cyan + " > " + ConsoleColor.reset + "Für das Spiel *" + game + " wurde eine Rolle erstellt", "info");
        }
    }

    /*edit existing message, to add new thinks > like thumbnail*/
    private void EditMessagesFromGames(Guild guild, Activity activity) {
        for (Message message : guild.getTextChannelById(PropertiesFile.readsPropertiesFile("games")).getIterableHistory()) {
            MessageEmbed mes = message.getEmbeds().get(0);
            if (mes.getTitle().equals(activity.getName())) {
                EmbedBuilder builder = new EmbedBuilder();
                try {
                    if (!activity.asRichPresence().getLargeImage().equals(message.getEmbeds().get(0).getThumbnail()) && activity.asRichPresence().getLargeImage().getText() == null) {
                        message.editMessage(builder
                                .setTitle(mes.getTitle())
                                .setDescription(mes.getDescription())
                                .setColor(mes.getColor())
                                .setThumbnail(activity.asRichPresence().getLargeImage().getUrl())
                                .build()).queue();
                    }
                } catch (NullPointerException e) {
                    try {
                        if (!activity.asRichPresence().getSmallImage().equals(message.getEmbeds().get(0).getThumbnail()) && activity.asRichPresence().getSmallImage().getText() == null) {
                            message.editMessage(builder
                                    .setTitle(mes.getTitle())
                                    .setDescription(mes.getDescription())
                                    .setColor(mes.getColor())
                                    .setThumbnail(activity.asRichPresence().getSmallImage().getUrl())
                                    .build()).queue();
                        }
                    } catch (NullPointerException ignored) {
                    }
                }
            }
        }
    }
}