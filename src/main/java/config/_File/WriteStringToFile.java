package config._File;

import config.PropertiesFile;
import count._main_Counter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import other._stuff.ConsoleColor;
import other._stuff.LogBack;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class WriteStringToFile {

    LogBack LB = new LogBack();

    public void write(Guild guild, String filename, String line) {
        try {
            String file = "config/" + filename + ".txt";
            String extra = filename;
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, true));
            BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
            List<String> lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);

            if (!lines.contains(line)) {

                if (extra.equals("games")) {
                    /*
                    check, if "line" is in the blacklist
                    */
                    File file_bl = new File("config/blacklist/GuildBlackList.txt");
                    if (file_bl.exists()) {
                        List<String> lines_BL = Files.readAllLines(Paths.get(file_bl + ""), StandardCharsets.UTF_8);
                        if (lines_BL.contains(line)) {
                            return;
                        }
                    }
                    guild.getTextChannelById(PropertiesFile.readsPropertiesFile("games", "config")).sendMessage(new EmbedBuilder()
                            .setTitle(line)
                            .setColor(Color.GREEN)
                            .setDescription(new SimpleDateFormat("dd.MM.YY").format(Calendar.getInstance().getTime()) + "\n\n")
                            .build()).queue();
                    LB.log(Thread.currentThread().getName(), ConsoleColor.backBblack + "LIST GAMES" + ConsoleColor.reset + ConsoleColor.cyan +
                            " > " + ConsoleColor.reset + ConsoleColor.Bgreen + "Das Spiel " + ConsoleColor.white + "*" + line + "*" + ConsoleColor.reset + ConsoleColor.Bgreen + " ist nun in der Liste!" + ConsoleColor.reset, "info");
                    writer.write(line + "\n");
                    writer.close();
                    reader.close();
                    _main_Counter c = new _main_Counter();
                    c.getint(guild, "gamecount");

                } else if (extra.equals("list")) {
                    Member member = guild.getMemberById(line);
                    User user = member.getUser();
                    LB.log(Thread.currentThread().getName(), ConsoleColor.backBblack + "LIST GAMEROLE" + ConsoleColor.reset + ConsoleColor.cyan +
                            " > " + user.getName() + ConsoleColor.reset + ConsoleColor.Bgreen + " ist nun in der Liste" + ConsoleColor.reset, "info");
                    if (!member.equals(guild.getSelfMember())) {
                        guild.addRoleToMember(member, guild.getRolesByName("GameRole", false).get(0)).queue();
                        user.openPrivateChannel().queue(privateChannel ->
                                privateChannel.sendMessage("Hiermit wird best\u00e4tigt, dass du die 'Zustimmung' angenommen hast! :grinning:").queue());
                        writer.write(line + "\n");
                        writer.close();
                        reader.close();
                    }
                }
            }
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }
}