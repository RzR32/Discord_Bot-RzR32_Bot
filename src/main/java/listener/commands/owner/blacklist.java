package listener.commands.owner;

import config.PropertiesFile;
import config._Check.CheckIfEmptyFile;
import config._File.RemoveStringFromFile;
import count._main_Counter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import other._stuff.ConsoleColor;
import other._stuff.LogBack;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class blacklist {

    private static final LogBack LB = new LogBack();

    public static void command(Guild guild, TextChannel channel, Member member, Message message, String[] argArray) {
        if ((argArray[1].equalsIgnoreCase("add") || (argArray[1].equalsIgnoreCase("remove")) || (argArray[1].equalsIgnoreCase("list")))) {

            if (argArray[1].equalsIgnoreCase("list")) {
                GuildBlacklist_list(message, member.getUser());
                return;
            }

            /*
            compine all arguments
            */
            ArrayList<String> list = new ArrayList<>();
            for (String s : argArray) {
                list.add(s);
                if (list.size() == argArray.length) {
                    list.remove(argArray[0]);
                    list.remove(argArray[1]);
                    if (list.isEmpty()) {
                        return;
                    }
                }
            }
            String liststring = String.join(" ", list);
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("config/games.txt", StandardCharsets.UTF_8, true));
                BufferedReader reader = new BufferedReader(new FileReader("config/games.txt", StandardCharsets.UTF_8));

                List<String> lines = null;
                if (argArray[1].equalsIgnoreCase("add")) {
                    lines = Files.readAllLines(Paths.get("config/games.txt"), StandardCharsets.UTF_8);

                } else if (argArray[1].equalsIgnoreCase("remove")) {
                    lines = Files.readAllLines(Paths.get("config/blacklist/GuildBlackList.txt"), StandardCharsets.UTF_8);
                }

                if (!lines.isEmpty()) {
                    for (int z = 0; z < lines.size(); z++) {
                        if (lines.get(z).equalsIgnoreCase(liststring)) {
                            Guild_Blacklist(guild, message, lines.get(z), argArray[1]);
                            return;
                        }
                        if (lines.size() == z + 1) {
                            message.addReaction("\u274C").queue();
                            return;
                        }
                    }
                }
                writer.close();
                reader.close();
            } catch (IOException e) {
                LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
            }
        } else {
            channel.sendMessage(new EmbedBuilder().setTitle("Blacklist").setColor(Color.RED).setDescription(">blacklist add <game>\n>blacklist remove <game>\n>blacklist list").build()).queue();
        }
    }

    /**
     * List for a whole Server / Guild
     *
     * @param message message
     * @param user    user
     */
    private static void GuildBlacklist_list(Message message, User user) {
        try {
            File dir = new File("config/blacklist/");
            boolean a = dir.mkdir();
            String filename = "config/blacklist/GuildBlackList.txt";

            File file = new File("config/blacklist/GuildBlackList.txt");
            if (!file.exists()) {
                message.getChannel().sendMessage(user.getAsMention() + " die Blacklist des Server´s ist leer!").queue();
                return;
            }

            List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
            if (lines.toString().equals("[]")) {
                message.getChannel().sendMessage(user.getAsMention() + " die Blacklist des Server´s ist leer!").queue();
                return;
            }

            message.getChannel().sendMessage(new EmbedBuilder().setTitle("Server Blacklist").setColor(Color.WHITE).setDescription(lines.toString()).build()).queue();
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }

    /**
     * Add / Remove a Game from a whole Server/Guild list
     *
     * @param guild    guild
     * @param message  message
     * @param Game     Game
     * @param operator operator
     */
    public static void Guild_Blacklist(Guild guild, Message message, String Game, String operator) {

        File dir = new File("config/blacklist/");
        boolean a = dir.mkdir();

        if (operator.equalsIgnoreCase("add")) {
            /*
            remove game from file
            */
            RemoveStringFromFile RSFF = new RemoveStringFromFile();
            RSFF.remove(guild, "games", Game);
            /*
            delete the gamerole
            */
            for (Role role : guild.getRoles()) {
                try {
                    List<String> lines = Files.readAllLines(Paths.get("config/games.txt"), StandardCharsets.UTF_8);
                    if (lines.contains(Game)) {
                        if (role.getName().equalsIgnoreCase(Game)) {
                            role.delete().queue();
                            break;
                        }
                    }
                } catch (IOException e) {
                    LB.log(Thread.currentThread().getName(), e.getMessage(), "info");
                }
            }
            /*
            delete the ONE message in #games
            */
            for (Message message_s : guild.getTextChannelById(PropertiesFile.readsPropertiesFile("games", "config")).getIterableHistory()) {
                if (message_s.getEmbeds().get(0).getTitle().equalsIgnoreCase(Game)) {
                    message_s.delete().queue();
                    break;
                }
            }
            /*
            call gamecounter
            */
            _main_Counter c = new _main_Counter();
            c.getint(guild, "gamecount");

            LB.log(Thread.currentThread().getName(), ConsoleColor.backBblack + "LIST GAMES" + ConsoleColor.reset + ConsoleColor.cyan +
                    " > " + ConsoleColor.reset + ConsoleColor.Bred + "Das Spiel " + ConsoleColor.white + "*" + Game + "*" + ConsoleColor.reset + ConsoleColor.Bred + " wurde aus der Liste entfernt!" + ConsoleColor.reset, "info");
            message.addReaction("\uD83D\uDC4D").queue();

            try {
                /*
                add the game to the guild blacklist
                */
                String filename = "config/blacklist/GuildBlackList.txt";
                File inputFile = new File("config/blacklist/GuildBlackList.txt");

                BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile, StandardCharsets.UTF_8, true));
                BufferedReader reader = new BufferedReader(new FileReader(inputFile, StandardCharsets.UTF_8));
                List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);

                if (!inputFile.exists()) {
                    writer.close();
                    reader.close();
                    return;
                }
                if (!lines.contains(Game)) {
                    writer.write(Game + "\n");
                    message.addReaction("\uD83D\uDC4D").queue();
                    writer.close();
                    reader.close();
                }
                writer.close();
                reader.close();

                /*
                remove the game from the user blacklist´s
                */
                File folder = new File("config/blacklist/");
                File[] listOfFiles = folder.listFiles();

                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        if (!file.getName().equalsIgnoreCase("GuildBlackList.txt")) {
                            List<String> lines1 = Files.readAllLines(Paths.get(file + ""), StandardCharsets.UTF_8);
                            List<String> next = lines1;
                            /*
                            clear file
                            */
                            if (!lines1.isEmpty()) {
                                FileWriter writer1 = new FileWriter(file, false);
                                PrintWriter writer2 = new PrintWriter(writer1, false);
                                writer2.flush();
                                writer2.close();
                            } else {
                                return;
                            }
                            /*
                            write file new > WITH the old but remove ONE line
                            */
                            FileWriter writer3 = new FileWriter(file, false);
                            for (String string : next) {
                                if (!string.equalsIgnoreCase(Game)) {
                                    writer3.write(string + "\n");
                                }
                            }
                            writer3.close();
                        }
                    }
                }
            } catch (IOException e) {
                LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
            }
        } else if (operator.equalsIgnoreCase("remove")) {
            File inputFile = new File("config/blacklist/GuildBlackList.txt");
            try {
                String filename = "config/blacklist/GuildBlackList.txt";

                BufferedWriter WRITER = new BufferedWriter(new FileWriter(inputFile, StandardCharsets.UTF_8, true));
                BufferedReader READER = new BufferedReader(new FileReader(inputFile, StandardCharsets.UTF_8));
                List<String> LINES = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);

                if (!inputFile.exists()) {
                    WRITER.close();
                    READER.close();
                    return;
                }
                if (!LINES.contains(Game)) {
                    WRITER.close();
                    READER.close();
                    return;
                }

                WRITER.close();
                READER.close();

                List<String> LINES1 = Files.readAllLines(Paths.get(inputFile + ""), StandardCharsets.UTF_8);

                List<String> next = LINES1;

                /*
                clear file
                */
                if (!LINES1.isEmpty()) {
                    FileWriter writer1 = new FileWriter(inputFile, false);
                    PrintWriter writer2 = new PrintWriter(writer1, false);
                    writer2.flush();
                    writer2.close();
                }

                /*
                write file new > WITH the old but remove ONE line
                */
                FileWriter writer3 = new FileWriter(inputFile, false);
                for (String string : next) {
                    if (!string.equalsIgnoreCase(Game)) {
                        writer3.write(string + "\n");
                    } else {
                        message.addReaction("\uD83D\uDC4D").queue();
                    }
                }
                writer3.close();
                CheckIfEmptyFile.check_if_file_is_empty(inputFile);

            } catch (IOException e) {
                LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
            }
        }
    }
}