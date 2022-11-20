package listener.commands.member;

import config._Check.CheckIfEmptyFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
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

    public static void command(Guild guild, TextChannel channel, Member member, Message message) {
        String[] argArray = message.getContentRaw().split(" ");

        if (argArray[1].equalsIgnoreCase("add") || argArray[1].equalsIgnoreCase("remove") || argArray[1].equalsIgnoreCase("list")) {

            if (argArray[1].equalsIgnoreCase("list")) {
                UserBlackList_list(message, member.getUser());
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
                List<String> lines = Files.readAllLines(Paths.get("config/games.txt"), StandardCharsets.UTF_8);

                if (lines.toString().isEmpty()) {
                    writer.close();
                    reader.close();
                    return;
                }

                for (int z = 0; z < lines.size(); z++) {

                    if (lines.get(z).equalsIgnoreCase(liststring)) {
                        User_BlackList(guild, message, member.getUser(), lines.get(z), argArray[1]);
                        return;
                    }
                    if (lines.size() == z + 1) {
                        message.addReaction("\u274C").queue();
                        return;
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
     * List for a specific User
     *
     * @param message message for reaction
     * @param user    user for mention
     */
    private static void UserBlackList_list(Message message, User user) {
        try {
            File dir = new File("config/blacklist/");
            boolean a = dir.mkdir();
            String filename = "config/blacklist/" + user.getId() + ".txt";

            File file = new File("config/blacklist/" + user.getId() + ".txt");
            if (!file.exists()) {
                message.getChannel().sendMessage(user.getAsMention() + " deine Liste ist leer").queue();
                return;
            }

            List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
            if (lines.toString().equals("[]")) {
                message.getChannel().sendMessage(user.getAsMention() + " deine Liste ist leer").queue();
                return;
            }

            message.getChannel().sendMessage(new EmbedBuilder().setTitle(user.getName() + "´s Blacklist").setColor(Color.WHITE).setDescription(lines.toString()).build()).queue();
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }

    /**
     * Add / Remove a Game from a specific User list
     *
     * @param guild    guild
     * @param message  message
     * @param user     user
     * @param Game     Game
     * @param operator operator
     */
    private static void User_BlackList(Guild guild, Message message, User user, String Game, String operator) {
        try {
            File dir = new File("config/blacklist/");
            boolean a = dir.mkdir();

            File GuildBlackList_file = new File("config/blacklist/GuildBlackList.txt");
            if (!GuildBlackList_file.exists()) {
                GuildBlackList_file.createNewFile();
            }
            File user_file = new File("config/blacklist/" + user.getId() + ".txt");
            if (!user_file.exists()) {
                user_file.createNewFile();
            }

            List<String> lineS = Files.readAllLines(Paths.get("config/blacklist/GuildBlackList.txt"), StandardCharsets.UTF_8);

            if (lineS.contains(Game)) {
                message.addReaction("\u274C").queue();
                CheckIfEmptyFile.check_if_file_is_empty(user_file);
                return;
            }

            if (operator.equalsIgnoreCase("add")) {

                BufferedWriter writer = new BufferedWriter(new FileWriter(user_file, StandardCharsets.UTF_8, true));
                BufferedReader reader = new BufferedReader(new FileReader(user_file, StandardCharsets.UTF_8));
                List<String> lines = Files.readAllLines(Paths.get("config/blacklist/" + user.getId() + ".txt"), StandardCharsets.UTF_8);
                if (lines.toString().isEmpty()) {
                    writer.close();
                    reader.close();
                    return;
                }
                /*
                game already in the blacklist
                */
                if (lines.toString().contains(Game)) {
                    CheckIfEmptyFile.check_if_file_is_empty(user_file);
                    message.addReaction("\u274C").queue();
                /*
                game now add to the blacklist
                */
                } else {
                    writer.write(Game + "\n");
                    message.addReaction("\uD83D\uDC4D").queue();
                    guild.removeRoleFromMember(guild.getMembersByName(user.getName(), false).get(0), guild.getRolesByName(Game, false).get(0)).queue();
                }
                writer.close();
                reader.close();

            } else if (operator.equalsIgnoreCase("remove")) {

                List<String> lines = Files.readAllLines(Paths.get("config/blacklist/" + user.getId() + ".txt"), StandardCharsets.UTF_8);
                List<String> next = lines;

                /*
                clear file
                */
                if (!lines.isEmpty()) {
                    FileWriter writer1 = new FileWriter(user_file, false);
                    PrintWriter writer2 = new PrintWriter(writer1, false);
                    writer2.flush();
                    writer2.close();
                }

                /*
                write file new > WITH the old but remove ONE line
                */
                FileWriter writer3 = new FileWriter(user_file, false);
                for (String string : next) {
                    if (!string.equalsIgnoreCase(Game)) {
                        writer3.write(string + "\n");
                    } else {
                        message.addReaction("\uD83D\uDC4D").queue();
                    }
                }
                writer3.close();
                CheckIfEmptyFile.check_if_file_is_empty(user_file);
            }
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        } catch (IndexOutOfBoundsException ignored) {
        }
    }
}