package listener.commands.member;

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

public class gamerole {

    private static final LogBack LB = new LogBack();

    public static void command(Guild guild, TextChannel channel, Member member, Message message) {
        String[] argArray = message.getContentRaw().split(" ");

        if (argArray[1] != null) {
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
                /*
                gamerole commands to add / remove a game for the user
                */
                if (!member.getUser().isBot()) {
                    if (argArray[0].equalsIgnoreCase(">gamerole") || (argArray[0].equalsIgnoreCase(">gr"))) {

                        if ((argArray[1].equalsIgnoreCase("add") || (argArray[1].equalsIgnoreCase("remove") || (argArray[1].equalsIgnoreCase("list"))))) {

                            if (argArray[1].equalsIgnoreCase("list")) {
                                GameRoleList(guild, message);
                                return;
                            }

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
                                        GameRole_Add_Remove_From_User(guild, message, member, lines.get(z), argArray[1]);
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
                        }
                    }
                } else {
                    channel.sendMessage(new EmbedBuilder().setTitle("GameRole").setColor(Color.RED).setDescription(">gamerole add <game>\n>gamerole remove <game>").build()).queue();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                channel.sendMessage(new EmbedBuilder().setTitle("GameRole").setColor(Color.RED).setDescription(">gamerole add <game>\n>gamerole remove <game>\n>gamerole list").build()).queue();
            }
        }
    }

    private static void GameRole_Add_Remove_From_User(Guild guild, Message message, Member member, String Game, String operator) {
        try {
            File dir = new File("config/blacklist/");
            boolean a = dir.mkdir();
            File dir1 = new File("config/blacklist/" + guild.getId() + "/");
            boolean a1 = dir1.mkdir();

            User user = member.getUser();

            BufferedWriter createfile = new BufferedWriter(new FileWriter("config/blacklist/" + guild.getId() + "/GuildBlackList.txt", StandardCharsets.UTF_8));
            createfile.close();
            BufferedWriter createfile1 = new BufferedWriter(new FileWriter("config/blacklist/" + guild.getId() + "/" + user.getId() + ".txt"));
            createfile1.close();

            List<String> line1 = Files.readAllLines(Paths.get("config/blacklist/" + guild.getId() + "/" + user.getId() + ".txt"), StandardCharsets.UTF_8);
            List<String> line2 = Files.readAllLines(Paths.get("config/blacklist/" + guild.getId() + "/GuildBlackList.txt"), StandardCharsets.UTF_8);

            /*
            if game is blacklisted
            */
            if (line2.contains(Game) || line1.contains(Game)) {
                message.getChannel().sendMessage("Das Spiel ist in __einer__ Blacklist, daher kann das Spiel dir nicht hinzugef\u00f6gt werden").queue();
                message.addReaction("\u274C").queue();
                return;
            }

            /*
            if game as role dont exit
            */
            for (Role role : guild.getRoles()) {
                if (role.getName().equalsIgnoreCase(Game)) {

                    /*
                    add or remove it
                    */
                    if (operator.equalsIgnoreCase("add")) {

                        if (!member.getRoles().toString().contains(Game)) {
                            guild.addRoleToMember(member, guild.getRolesByName(Game, false).get(0)).queue();
                            message.addReaction("\uD83D\uDC4D").queue();
                        } else {
                            message.addReaction("\u274C").queue();
                        }
                    } else if (operator.equalsIgnoreCase("remove")) {

                        if (member.getRoles().toString().contains(Game)) {
                            guild.removeRoleFromMember(member, guild.getRolesByName(Game, false).get(0)).queue();
                            message.addReaction("\uD83D\uDC4D").queue();
                        } else {
                            message.addReaction("\u274C").queue();
                        }
                    }
                    return;
                }
            }
            message.addReaction("\u274C").queue();

        } catch (IOException | IndexOutOfBoundsException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }

    private static void GameRoleList(Guild guild, Message message) {
        try {
            message.addReaction("\uD83D\uDC4D").queue();
            EmbedBuilder builder = new EmbedBuilder().setTitle("GameRole´s").setDescription("Von den folgenden Spielen gibt es bereits Rollen!\n\n");

            List<String> lines = Files.readAllLines(Paths.get("config/games.txt"), StandardCharsets.UTF_8);

            if (lines.toString() == null || lines.isEmpty()) {
                builder.setDescription("Es sind aktuell noch keine Rollen vorhanden!");
                message.getChannel().sendMessage(builder.build()).queue();
                return;
            }

            for (String string : lines) {
                for (Role role : guild.getRoles()) {
                    if (string.equalsIgnoreCase(role.getName())) {
                        builder.getDescriptionBuilder().append(" > " + string + "\n");
                    }
                }
            }
            message.getChannel().sendMessage(builder.build()).queue();

        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }
}