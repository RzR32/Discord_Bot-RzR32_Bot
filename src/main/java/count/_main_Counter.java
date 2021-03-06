package count;

import config.PropertiesFile;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import other._guild.Members;
import other._guild.check.CheckCategory;
import other._guild.check.CheckChannel;
import other._stuff.ConsoleColor;
import other._stuff.LogBack;
import other._stuff.Pause;
import twitch.FollowerCount;
import twitch.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class _main_Counter {

    private static final ArrayList<String> list_ID = new ArrayList<>() {{
        add("membercount");
        add("rolecount");
        add("gamerolecount");
        add("textchannelcount");
        add("voicechannelcount");
        add("categorycount");
        add("emotecount");
        add("gamecount");
        add("twitchcount");
    }};
    LogBack LB = new LogBack();

    public void StartCounter(Guild guild) {
        for (String id : list_ID) {
            Pause P = new Pause();
            P.pause(250);
            getint(guild, id);
        }
    }

    public void getint(Guild guild, String id) {
        if (PropertiesFile.readsPropertiesFile(">" + id + "_on", "config").equals("true")) {
            /*
            check category
            */
            CheckCategory C_Category = new CheckCategory();
            C_Category.StartChecking(guild);
            /*
            check channel
            */
            CheckChannel C_CheckChannel = new CheckChannel();
            C_CheckChannel.checkingChannel(guild, id);
            /*
            size
            */
            String channel = null;
            String category = null;
            int size = 0;
            switch (id) {
                case "membercount":
                    channel = "MemberCount";
                    size = guild.getMemberCount();
                    category = "maincount";
                    /*
                    extra
                    */
                    Members member = new Members();
                    member.Member_CheckMembersOnGuild(guild);
                    break;
                case "rolecount":
                    channel = "RoleCount";
                    size = guild.getRoles().size();
                    category = "maincount";
                    /*
                    extra
                    */
                    try {
                        List<String> lines = Files.readAllLines(Paths.get("config/games.txt"), StandardCharsets.UTF_8);

                        if (lines.toString() == null || lines.isEmpty()) {
                            size = 0;
                        }

                        for (String string : lines) {
                            for (Role role : guild.getRoles()) {
                                if (string.equalsIgnoreCase(role.getName())) {
                                    size--;
                                }
                            }
                        }
                    } catch (IOException e) {
                        try {
                            File file = new File("config/games.txt");
                            boolean b = file.createNewFile();
                        } catch (IOException e1) {
                            return;
                        }
                    }
                    getint(guild, "gamerolecount");
                    GameRoleCount(guild, size);
                    break;
                case "gamerolecount":
                    return;
                case "textchannelcount":
                    channel = "TextChannelCount";
                    size = guild.getTextChannels().size();
                    category = "maincount";
                    break;
                case "voicechannelcount":
                    channel = "VoiceChannelCount";
                    size = guild.getVoiceChannels().size();
                    category = "maincount";
                    break;
                case "categorycount":
                    channel = "CategoryCount";
                    size = guild.getCategories().size();
                    category = "maincount";
                    break;
                case "emotecount":
                    channel = "EmoteCount";
                    size = guild.getEmotes().size();
                    category = "maincount";
                    break;
                case "gamecount":
                    channel = "GameCount";
                    category = "gamecategory";
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter("config/games.txt", StandardCharsets.UTF_8, true));
                        BufferedReader reader = new BufferedReader(new FileReader("config/games.txt", StandardCharsets.UTF_8));
                        List<String> lines = Files.readAllLines(Paths.get("config/games.txt"), StandardCharsets.UTF_8);
                        if (lines.toString().isEmpty()) {
                            writer.close();
                            reader.close();
                            return;
                        }
                        writer.close();
                        reader.close();
                        size = lines.size();
                        break;
                    } catch (IOException e) {
                        LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
                    }
                case "twitchcount":
                    /*re-call the counter*/
                    FollowerCount FC = new FollowerCount();
                    User U = new User();
                    try {
                        size = FC.getFollowerCount(U.getUserbyName(PropertiesFile.readsPropertiesFile("twitchname", "config")));
                    } catch (NullPointerException e) {
                        LB.log(Thread.currentThread().getName(), "No Twitchname set, can be ignored (if not needed)", "warn");
                    }
                    if (size == 0) {
                        return;
                    }
                    channel = "TwitchFollower";
                    category = "streamcategory";
                    break;
            }
            if (PropertiesFile.readsPropertiesFile(">" + category + "_on", "config").equals("true")) {
                change(guild, channel, id, size);
            }
        }
    }

    private void GameRoleCount(Guild guild, int x) {
        /*
        size for gamerolecount
        */
        if (PropertiesFile.readsPropertiesFile(">gamerolecount_on", "config").equals("true") && PropertiesFile.readsPropertiesFile(">maincount_on", "config").equals("true")) {
            int size = guild.getRoles().size() - x;
            change(guild, "GameRoleCount", "gamerolecount", size);
        }
    }

    public void change(Guild guild, String channel, String id, int size) {
        /*
        little pause to securely save the channel id by create channel
        */
        Pause P = new Pause();
        P.pause(1000);
        /*
        string for new channel name
        */
        String nameIsize = channel + " > " + size;
        /*
        string for old channel name
        */
        String string1 = guild.getVoiceChannelById(PropertiesFile.readsPropertiesFile(id, "config")).getName();
        /*
        string without the name > only the int/counter
        */
        String string2 = string1.replaceAll("[\\D]", "");
        int string3 = Integer.parseInt(string2);

        String wert;
        String farbe;
        String farbe_prefix;
        if (size == string3) {
            wert = "unver\u00e4ndert";
            farbe = ConsoleColor.backBgreen;
        } else {
            wert = "ver\u00e4ndert";
            farbe = ConsoleColor.backBred;
            Objects.requireNonNull(guild.getVoiceChannelById(PropertiesFile.readsPropertiesFile(id, "config"))).getManager().setName(nameIsize).queue();
        }

        if (channel.equals("TwitchFollower")) {
            farbe_prefix = ConsoleColor.backmagenta;
        } else {
            farbe_prefix = ConsoleColor.backcyan;
        }

        LB.log(Thread.currentThread().getName(), farbe_prefix + ConsoleColor.black + channel + ConsoleColor.reset + " > " + ConsoleColor.Byellow + "RealCounter = " + size +
                " │ ChannelName = " + string3 + " - " + ConsoleColor.reset + ConsoleColor.black + ConsoleColor.bold + farbe + wert + ConsoleColor.reset, "info");
    }
}