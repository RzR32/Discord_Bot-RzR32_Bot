package other;

import check_create.CheckChannel;
import config.PropertiesFile;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Members {

    LogBack LB = new LogBack();

    CheckChannel C_Channel = new CheckChannel();

    public static String[] GetGuildsIDs() {
        File file = new File("config/blacklist/");
        String[] x = file.list();
        return x;
        }

    /*
    check if some member is new (if the bot was offline)
     */
    public void Member_CheckMembersOnGuild(Guild guild) {
        try {
            C_Channel.checkingChannel(guild, "logs");
            BufferedWriter writer = new BufferedWriter(new FileWriter("config/members.txt", StandardCharsets.UTF_8, true));
            BufferedReader reader = new BufferedReader(new FileReader("config/members.txt", StandardCharsets.UTF_8));
            List<String> lines = Files.readAllLines(Paths.get("config/members.txt"), StandardCharsets.UTF_8);

            for (Member member : guild.getMembers()) {
                if (!lines.toString().contains(member.getUser().getId())) {
                    if (member.getNickname() == null) {
                        LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "MEMBERS" + ConsoleColor.reset + ConsoleColor.cyan + " > " + member.getUser().getName() +
                                ConsoleColor.Bgreen + " wurde zur Liste hinzugefügt!" + ConsoleColor.reset, "info");
                        String m = member.getUser().getId() + " █ " + member.getUser().getName() + " █ <no nickname>";
                        writer.write(m + "\n");
                    } else {
                        LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "MEMBERS" + ConsoleColor.reset + ConsoleColor.cyan + " > " + member.getUser().getName() +
                                ConsoleColor.reset + " | " + ConsoleColor.cyan + member.getNickname() + ConsoleColor.Bgreen + " wurde zur Liste hinzugefügt!" + ConsoleColor.reset, "info");
                        String m1 = member.getUser().getId() + " █ " + member.getUser().getName() + " █ " + member.getNickname();
                        writer.write(m1 + "\n");
                    }
                    sendMessage(guild, member.getUser().getId() + " █ " + member.getUser().getName() + " █ <no nickname>", "betreten!");
                }
            }
            writer.close();
            reader.close();

            Member_CheckMemberOnFile(guild);
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }

    public void Member_CheckMemberOnFile(Guild guild) {
        try {
            C_Channel.checkingChannel(guild, "logs");
            List<String> lines = Files.readAllLines(Paths.get("config/members.txt"), StandardCharsets.UTF_8);
            List<Member> members = guild.getMembers();

            /*
            clear file
             */
            if (!lines.isEmpty()) {
                FileWriter writer1 = new FileWriter("config/members.txt", StandardCharsets.UTF_8, false);
                PrintWriter writer2 = new PrintWriter(writer1, false);
                writer2.flush();
                writer2.close();
            }

            /*
            write file new > WITH the old but remove ONE line
             */
            FileWriter writer3 = new FileWriter("config/members.txt", StandardCharsets.UTF_8, false);

            for (String string : lines) {
                String UserIDfromFile = string.substring(0, Math.min(string.length(), 18));
                Member memberwithID = guild.getMemberById(UserIDfromFile);

                if (members.toString().contains(UserIDfromFile)) {
                    String stringe = null;
                    if (memberwithID.getNickname() == null) {
                        stringe = " <no nickname>";
                    } else {
                        stringe = " " + memberwithID.getNickname();
                    }

                    if (!string.substring(string.lastIndexOf("█") + 1).equals(stringe)) {
                        LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "NICKNAME" + ConsoleColor.reset + ConsoleColor.cyan + " > " + memberwithID.getUser().getName() +
                                " >>> *" + string.substring(string.lastIndexOf("█") + 1) + "* heißt nun *" + stringe + "*" + ConsoleColor.reset, "info");
                        if (!PropertiesFile.readsPropertiesFile("logs").isEmpty()) {
                            String channel = PropertiesFile.readsPropertiesFile("logs");
                            guild.getTextChannelById(channel).sendMessage("NameChange: " + memberwithID.getUser().getName() + " >>> *" + string.substring(string.lastIndexOf("█") + 1) + "* heißt nun *" + stringe + "*").queue();
                        }
                    }

                    if (memberwithID.getNickname() == null) {
                        writer3.write(memberwithID.getUser().getId() + " █ " + memberwithID.getUser().getName() + " █ <no nickname>\n");
                    } else {
                        writer3.write(memberwithID.getUser().getId() + " █ " + memberwithID.getUser().getName() + " █ " + memberwithID.getNickname() + "\n");
                    }
                } else {
                    String s_between = string;
                    s_between = s_between.substring(s_between.indexOf("█") + 1);
                    s_between = s_between.substring(0, s_between.indexOf("█"));
                    LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "MEMBERS" + ConsoleColor.reset + ConsoleColor.cyan + " > " + s_between + ConsoleColor.Bred + " wurde aus der Liste entfernt!" + ConsoleColor.reset, "info");
                    sendMessage(guild, string, "verlassen!");
                }
            }
            writer3.close();
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }

    private void sendMessage(Guild guild, String s, String type) {
        C_Channel.checkingChannel(guild, "logs");
        int x = s.length() - 15; //no nickname int
        String out = null;

        if (x >= 1) {
            if (s.substring(x).equals("█ <no nickname>")) {
                out = s.substring(21, x);
            } else if (s.contains("█")) {
                s = s.substring(s.indexOf("█") + 1);
                s = s.substring(0, s.indexOf("█"));
                out = s;
            } else {
                out = s;
            }
        }

        if (PropertiesFile.readsPropertiesFile("logs").isEmpty()) {
            LB.log(Thread.currentThread().getName(), "No Channel set to send 'logs' messages!", "error");
        } else {
            try {
                if (!guild.getTextChannelById(PropertiesFile.readsPropertiesFile("logs")).toString().isEmpty()) {
                }
            } catch (NullPointerException e) {
                LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "GUILD" + ConsoleColor.reset + ConsoleColor.cyan + " > " + out + ConsoleColor.reset + " hat den Server " +
                    type + ConsoleColor.reset, "info");
                LB.log(Thread.currentThread().getName(), ConsoleColor.backgreen + ConsoleColor.red + "ERROR: Channel *logs* doesnt exist on this Server! (wrong id)" + ConsoleColor.reset, "error");
                return;
            }
        }

        if (type.equals("verlassen!")) {
            LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "GUILD" + ConsoleColor.reset + ConsoleColor.cyan + " > " + out + ConsoleColor.reset + ConsoleColor.red + " hat den Server verlassen!" + ConsoleColor.reset, "info");
        } else if (type.equals("betreten!")) {
            LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "GUILD" + ConsoleColor.reset + ConsoleColor.cyan + " > " + out + ConsoleColor.reset + ConsoleColor.green + " hat den Server betreten!" + ConsoleColor.reset, "info");
        }
        guild.getTextChannelById(PropertiesFile.readsPropertiesFile("logs")).sendMessage(out + " hat den Server " + type).queue();
    }
}