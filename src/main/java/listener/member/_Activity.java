package listener.member;

import config.PropertiesFile;
import config._File.WriteStringToFile;
import count.GamePlayingCount.ForwardPlayingGame;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import other._guild.check.CheckChannel;
import other._stuff.CheckGameOnWebsite;
import other._stuff.ConsoleColor;
import other._stuff.LogBack;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _Activity extends ListenerAdapter {

    private final HashMap<Member, List<Activity>> list = new HashMap<>();
    LogBack LB = new LogBack();

    public void onUserActivityStart(@NotNull UserActivityStartEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            if (!event.getMember().getUser().isBot()) {
                CheckChannel C_Channel = new CheckChannel();
                C_Channel.checkingChannel(event.getGuild(), "games");
                C_Channel.checkingChannel(event.getGuild(), "playingcount");

                Forwarded(event.getGuild(), "start", event.getMember(), event.getNewActivity());
            }
        }
    }

    public void onUserActivityEnd(@NotNull UserActivityEndEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            if (!event.getMember().getUser().isBot()) {
                CheckChannel C_Channel = new CheckChannel();
                C_Channel.checkingChannel(event.getGuild(), "games");
                C_Channel.checkingChannel(event.getGuild(), "playingcount");

                for (Activity activity : event.getMember().getActivities()) {
                    if (activity.getName().equals(event.getOldActivity().getName())) {
                        //the game is the same as old - return
                        return;
                    }
                }
                Forwarded(event.getGuild(), "end", event.getMember(), event.getOldActivity());
            }
        }
    }

    public void Forwarded(Guild guild, String start_end, Member member, Activity game) {
        boolean new_act = false;

        String username = member.getEffectiveName();
        String game_name = game.getName();
        String name;
        String type;

        if (game.getType() == Activity.ActivityType.DEFAULT) {
            name = "GAME";
            type = "spielt";
        } else if (game.getType() == Activity.ActivityType.LISTENING) {
            name = "LISTEN";
            type = "h\u00f6rt";
        } else if (game.getType() == Activity.ActivityType.STREAMING) {
            name = "STREAM";
            type = "streamt";
        } else if (game.getType() == Activity.ActivityType.WATCHING) {
            name = "WATCH";
            type = "schaut";
        } else if (game.getType() == Activity.ActivityType.CUSTOM_STATUS) {
            name = "STATUS";
            type = "Status ist";
        } else {
            LB.log(Thread.currentThread().getName(), "Error: Games_from_Member, ActivityType " + game_name, "error");
            return;
        }

        /*
        Console OutPut like Logback
        */
        SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss.SSS");
        Date D = new Date();
        String date = SDF.format(D);

        String s_prefix = ConsoleColor.red + date + " " + ConsoleColor.green + "[" + Thread.currentThread().getName() + "] " + ConsoleColor.yellow + "INFO - ";
        String s_mid = " > " + ConsoleColor.cyan + username + ConsoleColor.reset + ConsoleColor.Bblue + " ";
        String s_suffix_game = ConsoleColor.reset + ConsoleColor.white + game_name + ConsoleColor.reset;

        if (start_end.equals("end")) {
            System.out.println(s_prefix + ConsoleColor.backblue + name + ConsoleColor.reset + s_mid + type + " nicht mehr " + s_suffix_game);
            list.replace(member, member.getActivities());

        /*
        member still playing - check, if game is in the list
        */
        } else if (start_end.equals("start")) {
            if (!list.containsKey(member)) {
                new_act = true;
            } else {
                for (Map.Entry<Member, List<Activity>> entry : list.entrySet()) {
                    Member key = entry.getKey();
                    if (key.equals(member)) {
                        if (entry.getValue() != null) {
                            if (key.equals(member)) {
                                if (!entry.getValue().toString().contains(game_name)) {
                                    new_act = true;
                                }
                            }
                        } else {
                            new_act = true;
                            break;
                        }
                    }
                }
            }

            if (new_act) {
                if (game.getType() == Activity.ActivityType.DEFAULT) {
                    WriteStringToFile WSTF = new WriteStringToFile();
                    WSTF.write(guild, "games", game_name);
                    GameRole(guild, member.getId(), member, game_name);
                    EditMessagesFromGames(guild, game);
                }
                System.out.println(s_prefix + ConsoleColor.backblue + name + ConsoleColor.reset + s_mid + type + " nun " + s_suffix_game);
                list.put(member, member.getActivities());
            }
        }
        ForwardPlayingGame._message(guild, game);
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
            if (PropertiesFile.readsPropertiesFile(">bot-zustimmung_on", "config").equals("true") && PropertiesFile.readsPropertiesFile(">games_on", "config").equals("true")) {
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
                                    LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "GAMEROLE" + ConsoleColor.reset + " > " + ConsoleColor.cyan + ConsoleColor.white + " Ein User wurde eine Gamerole hinzugef\u00fcgt", "info");
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        } catch (IndexOutOfBoundsException e1) {
            guild.createRole().setName(game).setMentionable(true).queue(role -> guild.addRoleToMember(member, role).queue());
            LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "GAMEROLE" + ConsoleColor.reset + " > " + ConsoleColor.cyan + ConsoleColor.reset + "F\u00fcr das Spiel *" + game + "* wurde eine Rolle erstellt", "info");
        }
    }

    /*
    edit existing message, to add new thinks > like thumbnail, Shop URL
    */
    public void EditMessagesFromGames(Guild guild, Activity activity) {
        try {
            CheckGameOnWebsite GIS = new CheckGameOnWebsite();

            for (Message message : guild.getTextChannelById(PropertiesFile.readsPropertiesFile("games", "config")).getIterableHistory()) {
                MessageEmbed mes = message.getEmbeds().get(0);
                if (mes.getTitle().equals(activity.getName())) {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle(mes.getTitle()).setColor(mes.getColor());

                    for (String string : GIS.startcheck(activity.getName())) {
                        if (!string.contains("null")) {
                            builder.appendDescription(string + "\n");
                        }
                    }

                    /*
                    largeIMG
                    */
                    try {
                        if (!activity.asRichPresence().getLargeImage().equals(message.getEmbeds().get(0).getThumbnail()) && activity.asRichPresence().getLargeImage().getText() == null) {
                            builder.setThumbnail(activity.asRichPresence().getLargeImage().getUrl());
                            message.editMessage(builder.build()).queue();
                        }
                    } catch (NullPointerException e) {
                        /*
                        smallIMG
                         */
                        try {
                            if (!activity.asRichPresence().getSmallImage().equals(message.getEmbeds().get(0).getThumbnail()) && activity.asRichPresence().getSmallImage().getText() == null) {
                                builder.setThumbnail(activity.asRichPresence().getSmallImage().getUrl());
                                message.editMessage(builder.build()).queue();
                            }
                        /*
                        else - no img
                        */
                        } catch (NullPointerException ex) {
                            message.editMessage(builder.build()).queue();
                        }
                    }
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }
}