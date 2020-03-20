package writeFile;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import other.ConsoleColor;
import other.LogBack;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RemoveStringFromFile {

    LogBack LB = new LogBack();

    public void remove(Guild guild, String filename, String line) {
        try {
            String file = "config/" + filename + ".txt";
            String extra = filename;
            List<String> lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);
            /*
            clear file
             */
            if (!lines.isEmpty()) {
                FileWriter writer1 = new FileWriter(file, false);
                PrintWriter writer2 = new PrintWriter(writer1, false);
                writer1.flush();
                writer1.close();
                writer2.flush();
                writer2.close();
            }
            /*
            write file new > WITH the old but remove ONE line
             */
            FileWriter writer3 = new FileWriter(file, false);
            for (String string : lines) {
                if (!string.equalsIgnoreCase(line)) {
                    writer3.write(string + "\n");
                } else {
                    if (extra.equals("list")) {
                        Member member = guild.getMemberById(line);
                        User user = member.getUser();
                        LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "LIST for GAMEROLE" + ConsoleColor.reset + ConsoleColor.cyan +
                                " > " + member.getEffectiveName() + ConsoleColor.reset + ConsoleColor.red + " ist nun nicht mehr in der Liste" + ConsoleColor.reset, "info");
                        user.openPrivateChannel().queue(privateChannel ->
                                privateChannel.sendMessage("Hiermit wird bestätigt, dass du von der 'Zustimmung' entfernt wurdest! :grinning:").queue());
                        guild.removeRoleFromMember(member, guild.getRolesByName("GameRole", false).get(0)).queue();
                        //remove all gameroles
                        try {
                            List<String> lines1 = Files.readAllLines(Paths.get("config/games.txt"), StandardCharsets.UTF_8);
                            for (int x = 0; x < guild.getRoles().size(); x++) {
                                if (lines1.contains(guild.getRoles().get(x).getName())) {
                                    guild.removeRoleFromMember(member, guild.getRoles().get(x)).queue();
                                }
                            }
                        } catch (IndexOutOfBoundsException e) {
                            writer3.close();
                            System.out.print("");
                        }
                    }
                }
            }
            writer3.close();
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }
}