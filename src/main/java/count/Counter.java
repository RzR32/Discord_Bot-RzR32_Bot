package count;

import check_create.CheckCategory;
import check_create.CheckChannel;
import other.ConsoleColor;
import other.LogBack;
import other.Members;
import config.PropertiesFile;
import net.dv8tion.jda.api.entities.Guild;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Counter {

    LogBack LB = new LogBack();

    private static ArrayList<String> list_ID = new ArrayList<>() {{
        add("membercount");
        add("rolecount");
        add("gamerolecount");
        add("textchannelcount");
        add("voicechannelcount");
        add("categorycount");
        add("gamecount");
        add("twitchcount");
    }};

    public void StartCounter(Guild guild) {
        for (String id : list_ID) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException error) {
                LB.log(Thread.currentThread().getName(), error.getMessage(), "error");
            }
            getint(guild, id);
        }
    }

    public void getint(Guild guild, String id) {
        /*
        check category
         */
        CheckCategory C_Category = new CheckCategory();
        C_Category.checkingCategory(guild, "maincount");
        C_Category.checkingCategory(guild, "streamcategory");
        C_Category.checkingCategory(guild, "gamecategory");
        /*
        check channel
         */
        CheckChannel C_CheckChannel = new CheckChannel();
        C_CheckChannel.checkingChannel(guild, id);
        /*
        size
         */
        String channel = null;
        int size = 0;
        switch (id) {
            case "membercount":
                channel = "MemberCount";
                size = guild.getMembers().size();
                /*
                extra
                 */
                Members member = new Members();
                member.Member_CheckMembersOnGuild(guild);
                break;
            case "rolecount":
                channel = "RoleCount";
                size = guild.getRoles().size();
                /*
                extra
                 */
                try {
                    BufferedReader br = new BufferedReader(new FileReader("config/games.txt"));
                    String roles = guild.getRoles().toString();
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        if (roles.contains(line)) {
                            size--;
                        }
                    }
                    br.close();
                } catch (IOException e) {
                    try {
                        File file = new File("config/games.txt");
                        boolean b = file.createNewFile();
                    } catch (IOException e1) {
                        return;
                    }
                }
                /*
                do gamerolecount
                 */
                GameRoleCount(guild, size);
                break;
            case "gamerolecount":
                return;
            case "textchannelcount":
                channel = "TextChannelCount";
                size = guild.getTextChannels().size();
                break;
            case "voicechannelcount":
                channel = "VoiceChannelCount";
                size = guild.getVoiceChannels().size();
                break;
            case "categorycount":
                channel = "CategoryCount";
                size = guild.getCategories().size();
                break;
            case "gamecount":
                channel = "GameCount";
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
                TwitchFollowerCount tfc = new TwitchFollowerCount();
                tfc.TwitchFollower(guild);
                return;
        }
        change(guild, channel, id, size);
    }

    private void GameRoleCount(Guild guild, int x) {
        /*
        size for gamerolecount
         */
        int size = guild.getRoles().size() - x;
        change(guild, "GameRoleCount", "gamerolecount", size);
    }

    public void change(Guild guild, String channel, String id, int size) {

        /*
        little pause to securely save the channel id by create channel
         */
        try {
            Thread.sleep(1000);
        } catch (InterruptedException error) {
            LB.log(Thread.currentThread().getName(), error.getMessage(), "error");
        }
        /*
        string for new channel name
         */
        String nameIsize = channel + " > " + size;
        /*
        string for old channel name
         */
        String string1 = guild.getVoiceChannelById(PropertiesFile.readsPropertiesFile(id)).getName();
        /*
        string without the name > only the int/counter
         */
        String string2 = string1.replaceAll("[\\D]", "");
        int string3 = Integer.parseInt(string2);
        if (size == string3) {
            LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + channel + ConsoleColor.reset + " > " + ConsoleColor.backcyan + ConsoleColor.Byellow + "Guild = " + size +
                    " │ Counter = " + string3 + " - " + ConsoleColor.bold + ConsoleColor.green + "unverändert!" + ConsoleColor.reset, "info");
        } else {
            LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + channel + ConsoleColor.reset + " > " + ConsoleColor.backcyan + ConsoleColor.Byellow + "Guild = " + size +
                    " │ Counter = " + string3 + " - " + ConsoleColor.bold + ConsoleColor.red + "verändert!" + ConsoleColor.reset, "info");
            guild.getVoiceChannelById(PropertiesFile.readsPropertiesFile(id)).getManager().setName(nameIsize).queue();
        }
    }
}
