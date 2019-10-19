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
            } else {
                return;
            }

            /*
            write file new > WITH the old but remove ONE line
             */
            FileWriter writer3 = new FileWriter("config/members.txt", StandardCharsets.UTF_8, false);

            for (String string : lines) {
                String UserID_fromFile = string.substring(0, Math.min(string.length(), 18));
                Member MemberID = guild.getMemberById(UserID_fromFile);
                String user_nickname;
                String user_name = string;
                user_name = user_name.substring(user_name.indexOf("█") + 1);
                user_name = user_name.substring(0, user_name.indexOf("█")).trim();

                /*check, if member ID is in the list*/
                if (members.toString().contains(UserID_fromFile)) {
                    /*check if member has nickname*/
                    if (MemberID.getNickname() == null) {
                        user_nickname = " <no nickname>";
                    } else {
                        user_nickname = " " + MemberID.getNickname();
                    }

                    /*compare nickname with file*/
                    if (user_nickname.equals(" <no nickname>")) {
                        if (!string.substring(string.lastIndexOf("█") + 1).equals(user_nickname)) {
                            String trim = string.substring(string.lastIndexOf("█") + 1).trim();
                            LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "NICKNAME" + ConsoleColor.reset + ConsoleColor.cyan + " > " + user_name + ConsoleColor.reset + " hat sein Nickname entfernt (" + ConsoleColor.cyan + trim + ConsoleColor.reset + ")" + ConsoleColor.reset, "info");
                            if (!PropertiesFile.readsPropertiesFile("logs").isEmpty()) {
                                String channel = PropertiesFile.readsPropertiesFile("logs");
                                guild.getTextChannelById(channel).sendMessage("NickNameChange: **" + user_name + "** hat sein Nickname entfernt (**" + trim + "**)").queue();
                            }
                        }
                    } else {
                        if (!lines.toString().contains(MemberID.getEffectiveName())) {
                            if (!MemberID.getEffectiveName().equals(user_name)) {
                                LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "NICKNAME" + ConsoleColor.reset + ConsoleColor.cyan + " > " + user_name + ConsoleColor.reset + " heißt nun " + ConsoleColor.cyan + MemberID.getEffectiveName() + ConsoleColor.reset, "info");
                                if (!PropertiesFile.readsPropertiesFile("logs").isEmpty()) {
                                    String channel = PropertiesFile.readsPropertiesFile("logs");
                                    guild.getTextChannelById(channel).sendMessage("NickNameChange: **" + user_name + "** heißt nun **" + MemberID.getEffectiveName() + "**").queue();
                                }
                            }
                        }
                    }

                    /*write file*/
                    if (MemberID.getNickname() == null) {
                        writer3.write(MemberID.getUser().getId() + " █ " + user_name + " █ <no nickname>\n");
                    } else {
                        writer3.write(MemberID.getUser().getId() + " █ " + user_name + " █ " + MemberID.getEffectiveName() + "\n");
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

    /*sendmessage for join/leave*/
    private void sendMessage(Guild guild, String s, String type) {
        C_Channel.checkingChannel(guild, "logs");
        int x = s.length() - 15; //no nickname int
        String out = null;

        if (x >= 1) {
            if (s.substring(x).equals("█ <no nickname>")) {
                out = s.substring(21, x).trim();
            } else if (s.contains("█")) {
                s = s.substring(s.indexOf("█") + 1);
                s = s.substring(0, s.indexOf("█"));
                out = s.trim();
            } else {
                out = s.trim();
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