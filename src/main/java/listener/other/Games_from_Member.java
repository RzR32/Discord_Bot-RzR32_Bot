package listener.other;

import check_create.CheckChannel;
import count.GamePlayingCount;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.ConsoleColor;
import other.LogBack;
import writeFile.WriteStringToFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Games_from_Member extends ListenerAdapter {

    LogBack LB = new LogBack();

    public void onUserActivityStart(UserActivityStartEvent event) {
        if (!event.getMember().getUser().isBot()) {
            CheckChannel C_Channel = new CheckChannel();
            C_Channel.checkingChannel(event.getGuild(), "games");
            C_Channel.checkingChannel(event.getGuild(), "playingcount");
            Forwarded(event.getGuild(), "start", event.getNewActivity().getType(), event.getMember(), null, event.getNewActivity().getName());
        }
    }

    public void onUserActivityEnd(UserActivityEndEvent event) {
        if (!event.getMember().getUser().isBot()) {
            CheckChannel C_Channel = new CheckChannel();
            C_Channel.checkingChannel(event.getGuild(), "games");
            C_Channel.checkingChannel(event.getGuild(), "playingcount");
            Forwarded(event.getGuild(), "end", event.getOldActivity().getType(), event.getMember(), event.getOldActivity().getName(), null);
        }
    }

    private void Forwarded(Guild guild, String start_end, Activity.ActivityType short_type, Member member, String oldgame, String newgame) {
            String username = member.getEffectiveName();
        if (short_type == Activity.ActivityType.DEFAULT) {
            if (start_end.equals("start")) {
                WriteStringToFile WSTF = new WriteStringToFile();
                WSTF.write(guild, "games", newgame);
                GameRole(guild, member.getId(), member, newgame);
                LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "GAME" + ConsoleColor.reset + ConsoleColor.cyan + " > " + username + ConsoleColor.reset +
                        ConsoleColor.Bblue + " spielt nun " + ConsoleColor.reset + ConsoleColor.white + newgame + ConsoleColor.reset, "info");
            } else {
                LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "GAME" + ConsoleColor.reset + ConsoleColor.cyan + " > " + username + ConsoleColor.reset +
                        ConsoleColor.Bblue + " spielt nichtmehr " + ConsoleColor.reset + ConsoleColor.white + oldgame + ConsoleColor.reset, "info");
            }
        } else if (short_type == Activity.ActivityType.LISTENING) {
            if (start_end.equals("start")) {
                LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "LISTEN" + ConsoleColor.reset + ConsoleColor.cyan + " > " + username + ConsoleColor.reset +
                        ConsoleColor.Bblue + " hört nun " + ConsoleColor.reset + ConsoleColor.white + newgame + ConsoleColor.reset, "info");
            } else {
                LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "LISTEN" + ConsoleColor.reset + ConsoleColor.cyan + " > " + username + ConsoleColor.reset +
                        ConsoleColor.Bblue + " hört nichtmehr " + ConsoleColor.reset + ConsoleColor.white + oldgame + ConsoleColor.reset, "info");
            }
        } else if (short_type == Activity.ActivityType.STREAMING) {
            if (start_end.equals("start")) {
                LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "STREAM" + ConsoleColor.reset + ConsoleColor.cyan + " > " + username + ConsoleColor.reset +
                        ConsoleColor.Bmagenta + " streamt nun " + ConsoleColor.reset + ConsoleColor.white + newgame + ConsoleColor.reset, "info");
            } else {
                LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "STREAM" + ConsoleColor.reset + ConsoleColor.cyan + " > " + username + ConsoleColor.reset +
                        ConsoleColor.Bmagenta + " streamt nichtmehr " + ConsoleColor.reset + ConsoleColor.white + oldgame + ConsoleColor.reset, "info");
            }
        } else if (short_type == Activity.ActivityType.WATCHING) {
            if (start_end.equals("start")) {
                LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "WATCH" + ConsoleColor.reset + ConsoleColor.cyan + " > " + username + ConsoleColor.reset +
                        ConsoleColor.Bblue + " schaut nun " + ConsoleColor.reset + ConsoleColor.white + newgame + ConsoleColor.reset, "info");
            } else {
                LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "WATCH" + ConsoleColor.reset + ConsoleColor.cyan + " > " + username + ConsoleColor.reset +
                        ConsoleColor.Bblue + " schaut nichtmehr " + ConsoleColor.reset + ConsoleColor.white + oldgame + ConsoleColor.reset, "info");
            }
        }
        GamePlayingCount gamePlayingCount = new GamePlayingCount();
        if (start_end.equals("start")) {
            gamePlayingCount.ForwardPlayingGame(guild, newgame, short_type);
        } else {
            gamePlayingCount.ForwardPlayingGame(guild, oldgame, short_type);
        }
    }

    /**
     * create a Role from the Game, if the user agreed
     * @param guild Name from guild
     * @param userid ID from user
     * @param member get the member
     * @param game Name of the game
     */
    public void GameRole(Guild guild, String userid, Member member, String game) {
        try {
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
                                        LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "GAMEROLE" + ConsoleColor.reset + ConsoleColor.cyan + " > " + ConsoleColor.white +
                                                member.getUser().getName() + ConsoleColor.reset + " hat nun die Rolle = " + ConsoleColor.white + game + ConsoleColor.reset, "info");
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
            LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "GAMEROLE" + ConsoleColor.reset + ConsoleColor.cyan + " > " + ConsoleColor.white + member.getUser().getName() +
                    ConsoleColor.reset + " hat die Rolle = " + ConsoleColor.white + game + ConsoleColor.reset + " erstellt", "info");
        }
    }
}