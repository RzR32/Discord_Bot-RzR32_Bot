package listener.commands;

import check_create.CheckCategory;
import config.PropertiesFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.LogBack;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Blacklist_Command extends ListenerAdapter {

    LogBack LB = new LogBack();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            if (PropertiesFile.readsPropertiesFile("bot-channel").isEmpty()) {

                CheckCategory C_Category = new CheckCategory();
                C_Category.checkingCategory(event.getGuild(), "gamecategory");

                event.getMessage().delete().queue();
            } else {
                try {
                    if (!event.getGuild().getTextChannelById(PropertiesFile.readsPropertiesFile("bot-channel")).toString().isEmpty()) {
                    }
                } catch (NullPointerException e) {
                    try {
                        if (!event.getGuild().getTextChannelsByName("bot-channel", false).get(0).getId().isEmpty()) {
                            PropertiesFile.writePropertiesFile("bot-channel", event.getGuild().getTextChannelsByName("bot-channel", false).get(0).getId());
                            event.getChannel().sendMessage("Es ist eine Fehler passiert, bitte führe den __Command__ erneut aus!").queue();
                        }
                        return;
                    } catch (IndexOutOfBoundsException ignored) {
                    }
                }

                if (event.getChannel().getId().equals(PropertiesFile.readsPropertiesFile("bot-channel")) || (event.getGuild().getMember(event.getMember().getUser()).isOwner())) {

                    String[] argArray = event.getMessage().getContentRaw().split(" ");

                    try {
                        /*
                         * blacklist commands to add / remove a game for the user
                         */
                        if (!event.getMember().getUser().isBot()) {
                            if (argArray[0].equalsIgnoreCase(">blacklist") || (argArray[0].equalsIgnoreCase(">bl"))) {

                                if ((argArray[1].equalsIgnoreCase("add") || (argArray[1].equalsIgnoreCase("remove") || (argArray[1].equalsIgnoreCase("list"))))) {

                                    if (argArray[1].equalsIgnoreCase("list")) {
                                        UserBlackList_list(event.getMessage(), event.getMember().getUser());
                                        return;
                                    }

                                    /*
                                     * compine all arguments
                                     */
                                    ArrayList<String> list = new ArrayList<>();

                                    for (int x = 0; x < argArray.length; x++) {
                                        list.add(argArray[x]);

                                        if (list.size() == argArray.length) {
                                            list.remove(argArray[0]);
                                            list.remove(argArray[1]);

                                            if (list.isEmpty()) {
                                                return;
                                            }
                                            //liststring = game
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
                                                        GameToUserBlackList(event.getGuild(), event.getMessage(), event.getMember().getUser(), lines.get(z), argArray[1]);
                                                        return;
                                                    }
                                                    if (lines.size() == z + 1) {
                                                        event.getMessage().addReaction("\u274C").queue();
                                                        return;
                                                    }
                                                }
                                                writer.close();
                                                reader.close();
                                            } catch (IOException e) {
                                                LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
                                            }
                                        }
                                    }
                                } else {
                                    event.getChannel().sendMessage(new EmbedBuilder().setTitle("Blacklist").setColor(Color.RED).setDescription(">blacklist add <game>\n>blacklist remove <game>\n>blacklist list").build()).queue();
                                }

                                //Command for Server Owner - removed a *Game* completly
                            } else if (argArray[0].equalsIgnoreCase(">blacklist+") || (argArray[0].equalsIgnoreCase(">bl+"))) {
                                if (event.getMember().isOwner()) {
                                    if ((argArray[1].equalsIgnoreCase("add") || (argArray[1].equalsIgnoreCase("remove")) || (argArray[1].equalsIgnoreCase("list")))) {

                                        if (argArray[1].equalsIgnoreCase("list")) {
                                            GuildBlacklist_list(event.getMessage(), event.getMember().getUser());
                                            return;
                                        }

                                        /*
                                         * compine all arguments
                                         */
                                        ArrayList<String> list = new ArrayList<>();

                                        for (int x = 0; x < argArray.length; x++) {
                                            list.add(argArray[x]);

                                            if (list.size() == argArray.length) {
                                                list.remove(argArray[0]);
                                                list.remove(argArray[1]);

                                                if (list.isEmpty()) {
                                                    return;
                                                }
                                                //liststring = game
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
                                                            RemoveGameRoleFromGuild(event.getGuild(), event.getMessage(), lines.get(z), argArray[1]);
                                                            return;
                                                        }
                                                        if (lines.size() == z + 1) {
                                                            event.getMessage().addReaction("\u274C").queue();
                                                            return;
                                                        }
                                                    }
                                                    writer.close();
                                                    reader.close();
                                                } catch (IOException e) {
                                                    LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
                                                }
                                            }
                                        }
                                    } else {
                                        event.getChannel().sendMessage(new EmbedBuilder().setTitle("Blacklist").setColor(Color.RED).setDescription(">blacklist add <game>\n>blacklist remove <game>\n>blacklist list").build()).queue();
                                    }
                                } else {
                                    event.getMessage().addReaction("\u274C").queue();
                                }
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        event.getChannel().sendMessage(new EmbedBuilder().setTitle("Blacklist").setColor(Color.RED).setDescription(">blacklist add <game>\n>blacklist remove <game>\n>blacklist list").build()).queue();
                    }
                }
            }
        }
    }

    private void UserBlackList_list(Message message, User user) {
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

    private void GuildBlacklist_list(Message message, User user) {
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

    private void GameToUserBlackList(Guild guild, Message message, User user, String Game, String operator) {
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
                check_if_file_is_empty(user_file);
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
                //game already in the blacklist
                if (lines.toString().contains(Game)) {
                    check_if_file_is_empty(user_file);
                    message.addReaction("\u274C").queue();
                    //game now added to the blacklist
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
                    if (!string.equals(Game)) {
                        writer3.write(string + "\n");
                    } else {
                        message.addReaction("\uD83D\uDC4D").queue();
                    }
                }
                writer3.close();
                check_if_file_is_empty(user_file);
            }
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    private void RemoveGameRoleFromGuild(Guild guild, Message message, String Game, String operator) {

        File dir = new File("config/blacklist/");
        boolean a = dir.mkdir();

        if (operator.equalsIgnoreCase("add")) {
            try {
                /*
                remove the role from the guild
                 */
                guild.getRolesByName(Game, false).get(0).delete().queue();
            } catch (IndexOutOfBoundsException ignored) {
            }
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
                                if (!string.equals(Game)) {
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
                    if (!string.equals(Game)) {
                        writer3.write(string + "\n");
                    } else {
                        message.addReaction("\uD83D\uDC4D").queue();
                    }
                }
                writer3.close();
                check_if_file_is_empty(inputFile);

            } catch (IOException e) {
                LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
            }
        }
    }

    private void check_if_file_is_empty(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            if (br.readLine() == null) {
                file.delete();
            }
            br.close();
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }
}