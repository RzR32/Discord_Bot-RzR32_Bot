package listener.commands;

import check_create.CheckCategory;
import config.PropertiesFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
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

public class GameRole_Command extends ListenerAdapter {

    LogBack LB = new LogBack();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            if (event.isFromType(ChannelType.TEXT)) {
                if (PropertiesFile.readsPropertiesFile("bot-channel") == null || PropertiesFile.readsPropertiesFile("bot-channel").isEmpty()) {

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
                        } catch (IndexOutOfBoundsException e1) {
                            LB.log(Thread.currentThread().getName(), e1.getMessage(), "error");
                        }
                    }

                    if (event.getChannel().getId().equals(PropertiesFile.readsPropertiesFile("bot-channel")) || event.getGuild().getMember(event.getMember().getUser()).isOwner()) {

                        String[] argArray = event.getMessage().getContentRaw().split(" ");

                        try {
                            /*
                             * gamerole commands to add / remove a game for the user
                             */
                            if (!event.getMember().getUser().isBot()) {
                                if (argArray[0].equalsIgnoreCase(">gamerole") || (argArray[0].equalsIgnoreCase(">gr"))) {

                                    if ((argArray[1].equalsIgnoreCase("add") || (argArray[1].equalsIgnoreCase("remove") || (argArray[1].equalsIgnoreCase("list"))))) {

                                        if (argArray[1].equalsIgnoreCase("list")) {
                                            GameRoleList(event.getGuild(), event.getMessage());
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
                                                            GameRole_Add_Remove_From_User(event.getGuild(), event.getMessage(), event.getMember(), event.getMember().getUser(), lines.get(z), argArray[1]);
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
                                        event.getChannel().sendMessage(new EmbedBuilder().setTitle("GameRole").setColor(Color.RED).setDescription(">gamerole add <game>\n>gamerole remove <game>").build()).queue();
                                    }
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            event.getChannel().sendMessage(new EmbedBuilder().setTitle("GameRole").setColor(Color.RED).setDescription(">gamerole add <game>\n>gamerole remove <game>\n>gamerole list").build()).queue();
                        }
                    }
                }
            }
        }
    }

    private void GameRole_Add_Remove_From_User(Guild guild, Message message, Member member, User user, String Game, String operator) {
        try {
            File dir = new File("config/blacklist/");
            boolean a = dir.mkdir();
            File dir1 = new File("config/blacklist/" + guild.getId() + "/");
            boolean a1 = dir1.mkdir();

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
                message.getChannel().sendMessage("Das Spiel ist in __einer__ Blacklist, daher kann das Spiel dir nicht hinzugefügt werden").queue();
                message.addReaction("\u274C").queue();
                return;
            }

            /*
            if game as role dont exit
             */
            for (Role role : guild.getRoles()) {
                if (role.getName().equals(Game)) {

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

    private void GameRoleList(Guild guild, Message message) {
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