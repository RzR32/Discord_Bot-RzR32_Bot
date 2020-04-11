package listener.member;

import check_create.CheckChannel;
import config.PropertiesFile;
import count.GamePlayingCount;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.CheckGameOnWebsite;
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

                Forwarded(event.getGuild(), "start", event.getNewActivity().getType(), event.getMember(), event.getNewActivity());
            }
        }
    }

    public void onUserActivityEnd(UserActivityEndEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            if (!event.getMember().getUser().isBot()) {
                CheckChannel C_Channel = new CheckChannel();
                C_Channel.checkingChannel(event.getGuild(), "games");
                C_Channel.checkingChannel(event.getGuild(), "playingcount");

                Forwarded(event.getGuild(), "end", event.getOldActivity().getType(), event.getMember(), event.getOldActivity());
            }
        }
    }

    public void checkAllMembersActivity(Guild guild) {
        for (Member member : guild.getMembers()) {
            if (guild.getSelfMember() != member) {
                if (member.getActivities().size() > 0) {
                    for (Activity activity : member.getActivities()) {
                        Forwarded(guild, "start", activity.getType(), member, activity);
                    }
                }
            }
        }
    }

    private void Forwarded(Guild guild, String start_end, Activity.ActivityType short_type, Member member, Activity game) {
        GamePlayingCount gamePlayingCount = new GamePlayingCount();

        String username = member.getEffectiveName();
        String game_name = game.getName();
        String name;
        String type;

        if (short_type == Activity.ActivityType.DEFAULT) {
            name = "GAME";
            type = "spielt";
        } else if (short_type == Activity.ActivityType.LISTENING) {
            name = "LISTEN";
            type = "hört";
        } else if (short_type == Activity.ActivityType.STREAMING) {
            name = "STREAM";
            type = "streamt";
        } else if (short_type == Activity.ActivityType.WATCHING) {
            name = "WATCH";
            type = "schaut";
        } else if (short_type == Activity.ActivityType.CUSTOM_STATUS) {
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

        /*
        if the string "start_end == end BUT member has activity > member still playing
        */
        if (member.getActivities().size() != 0 && start_end.equals("end")) {
        /*
        if the string "start_end == end BUT member has NO activity > member dont playing
        */
        } else if (member.getActivities().size() == 0 && start_end.equals("end")) {
            System.out.println(s_prefix + ConsoleColor.backblue + name + ConsoleColor.reset + s_mid + type + " nicht mehr " + s_suffix_game);

        } else if (start_end.equals("start")) {
            if (short_type == Activity.ActivityType.DEFAULT) {
                WriteStringToFile WSTF = new WriteStringToFile();
                WSTF.write(guild, "games", game_name);
                GameRole(guild, member.getId(), member, game_name);
            }
            System.out.println(s_prefix + ConsoleColor.backblue + name + ConsoleColor.reset + s_mid + type + " nun " + s_suffix_game);
        }
        gamePlayingCount.ForwardPlayingGame(guild, game);
        EditMessagesFromGames(guild, game);
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
                                    LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "GAMEROLE" + ConsoleColor.reset + " > " + ConsoleColor.cyan + ConsoleColor.white + " Ein User wurde eine Gamerole hinzugefügt", "info");
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
            LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "GAMEROLE" + ConsoleColor.reset + " > " + ConsoleColor.cyan + ConsoleColor.reset + "Für das Spiel *" + game + " wurde eine Rolle erstellt", "info");
        }
    }

    /*
    edit existing message, to add new thinks > like thumbnail, Shop URL
    */
    private void EditMessagesFromGames(Guild guild, Activity activity) {
        try {
            CheckGameOnWebsite GIS = new CheckGameOnWebsite();

            for (Message message : guild.getTextChannelById(PropertiesFile.readsPropertiesFile("games")).getIterableHistory()) {
                MessageEmbed mes = message.getEmbeds().get(0);
                if (mes.getTitle().equals(activity.getName())) {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle(mes.getTitle()).setColor(mes.getColor());

                    String[] lines = mes.getDescription().split("\\r?\\n");
                    builder.setDescription(lines[0] + "\n\n");
                    String[] new_lines = {"null", "null", "null", "null", "null"};

                    String _steam = GIS.Steam(activity.getName());
                    String _epic = GIS.EpicGames(activity.getName());
                    String _blizzard = GIS.Blizzard(activity.getName());
                    String _origin = GIS.Origin(activity.getName());
                    String _uplay = GIS.Uplay(activity.getName());

                    if (!_steam.contains("null")) {
                        new_lines[0] = _steam;
                    }
                    if (!_epic.contains("null")) {
                        new_lines[1] = _epic;
                    }
                    if (!_blizzard.contains("null")) {
                        new_lines[2] = _blizzard;
                    }
                    if (!_origin.contains("null")) {
                        new_lines[3] = _origin;
                    }
                    if (!_origin.contains("null")) {
                        new_lines[4] = _uplay;
                    }

                    for (String string : new_lines) {
                        if (!string.equals("null")) {
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