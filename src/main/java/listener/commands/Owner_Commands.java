package listener.commands;

import config.PropertiesFile;
import count.Counter;
import count.GamePlayingCount;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.LogBack;
import writeFile.RemoveStringFromFile;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Owner_Commands extends ListenerAdapter {

    LogBack LB = new LogBack();

    private boolean bool_timer = false;
    private boolean bool_is_confirmed = false;

    /**
     * Listener for Commands
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            if (!event.getAuthor().isBot()) {
                String[] argArray = event.getMessage().getContentRaw().split(" ");
                    /*
                    help for owner
                     */
                if (argArray[0].equalsIgnoreCase(">help+")) {
                    if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                        event.getChannel().sendMessage(new EmbedBuilder().setTitle("HELP for Owner").setColor(Color.YELLOW).setDescription("" +
                                "Diese Befehle sind nur für den Server Besitzer!\n" +
                                "\n" +
                                "> >twitch <name> (don´t update the counter instantly)\n" +
                                "\n" +
                                "> >playingcount\n" +
                                "\n" +
                                "> >agreement\n" +
                                "\n" +
                                "> >settings\n" +
                                "\n" +
                                "> >dm (**D**elete **M**essage´s)\n" +
                                "\n" +
                                "> >del <game>\n" +
                                "\n" +
                                "> >delete-bot (!Need to be confirm!!)\n" +
                                "\n").build()).queue();
                        event.getMessage().addReaction("\uD83D\uDC4D").queue();
                    } else {
                        event.getChannel().sendMessage("Dieser Befehl ist nur für den Server Besitzer!").queue();
                        event.getMessage().addReaction("\u274C").queue();
                    }

                        /*
                        set the twitchname
                         */
                } else if (argArray[0].equalsIgnoreCase(">twitch")) {
                    if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                        try {
                            if (argArray[1] == null) {
                            } else {
                                PropertiesFile.writePropertiesFile("twitchname", argArray[1]);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            event.getChannel().sendMessage("Error: It´s look that you forgot the name of the Twitch User").queue();
                        }
                    } else {
                        event.getChannel().sendMessage("Dieser Befehl ist nur für den Server Besitzer!").queue();
                        event.getMessage().addReaction("\u274C").queue();
                    }

                } else if (argArray[0].equalsIgnoreCase(">playingcount") || argArray[0].equalsIgnoreCase(">pc")) {
                    if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                        GamePlayingCount GPC = new GamePlayingCount();
                        GPC.startCounter(event.getGuild());
                    } else {
                        event.getChannel().sendMessage("Dieser Befehl ist nur für den Server Besitzer!").queue();
                        event.getMessage().addReaction("\u274C").queue();
                    }

                        /*
                        set *first-startup* to false
                         */
                } else if (argArray[0].equalsIgnoreCase(">ready")) {
                    if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                        PropertiesFile.writePropertiesFile("first-startup", "false");
                        for (TextChannel textChannel : event.getGuild().getTextChannels()) {
                            if (textChannel.getName().equals(event.getMember().getId())) {
                                textChannel.delete().queue();
                            }
                        }
                        System.exit(0);
                    } else {
                        event.getChannel().sendMessage("Dieser Befehl ist nur für den Server Besitzer!").queue();
                        event.getMessage().addReaction("\u274C").queue();
                    }

                        /*
                        > remove game from file
                        > delete the gamerole (if created)
                        > delete the ONE message in #games
                         */
                } else if (argArray[0].equalsIgnoreCase(">del")) {
                    try {
                        if (argArray[1] == null) {
                        } else {
                            if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                                ArrayList<String> list = new ArrayList<>();
                                for (int x = 0; x < argArray.length; x++) {
                                    list.add(argArray[x]);
                                    if (list.size() == argArray.length) {
                                        list.remove(argArray[0]);
                                        if (list.isEmpty()) {
                                            return;
                                        }
                                        /*
                                        liststring = game
                                         */
                                        String liststring = String.join(" ", list);
                                        /*
                                        remove game from file
                                         */
                                        RemoveStringFromFile RSFF = new RemoveStringFromFile();
                                        RSFF.remove(event.getGuild(), "games", liststring);
                                        /*
                                        delete the gamerole
                                         */
                                        for (Role role : event.getGuild().getRoles()) {
                                            try {
                                                List<String> lines = Files.readAllLines(Paths.get("config/games.txt"), StandardCharsets.UTF_8);
                                                if (lines.contains(liststring)) {
                                                    if (role.getName().equalsIgnoreCase(liststring)) {
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
                                        for (Message message : event.getGuild().getTextChannelById(PropertiesFile.readsPropertiesFile("games")).getIterableHistory()) {
                                            if (message.getEmbeds().get(0).getTitle().equalsIgnoreCase(liststring)) {
                                                message.delete().queue();
                                                break;
                                            }
                                        }
                                        /*
                                        call gamecounter
                                         */
                                        Counter c = new Counter();
                                        c.getint(event.getGuild(), "gamecount");
                                        LB.log(Thread.currentThread().getName(), "Das Spiel *" + liststring + "* wurde aus der Liste entfern!", "info");
                                        event.getMessage().addReaction("\uD83D\uDC4D").queue();
                                    }
                                }
                            } else {
                                event.getChannel().sendMessage("Dieser Befehl ist nur für den Server Besitzer!").queue();
                                event.getMessage().addReaction("\u274C").queue();
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        event.getChannel().sendMessage("Hey, you forgot the game name!").queue();
                    }
                    /*
                    delete the bot - need a second confirm (!)
                     */
                } else if (argArray[0].equalsIgnoreCase(">delete-bot")) {
                    if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                        if (!bool_timer) {
                            timer(event.getGuild(), event.getMessage(), 11);
                        } else {
                            event.getMessage().addReaction("\uD83D\uDC4D").queue();
                            bool_is_confirmed = true;
                        }
                    } else {
                        event.getMessage().addReaction("\u274C").queue();
                    }
                }
            }
        }
    }

    private void timer(Guild guild, Message message, int x) {
        final int f_x = x - 1;
        if (f_x >= 0) {
            bool_timer = true;
            Runnable runnable = () -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    if (f_x == 10) {
                        message.addReaction("U+1f51f").queue();
                    } else if (f_x == 0) {
                        message.delete().queue();
                        if (bool_is_confirmed) {
                            final_delete_the_Bot(guild);
                        }
                    } else {
                        message.addReaction("U+3" + f_x + "U+20e3").queue();
                    }
                    timer(guild, message, f_x);
                } catch (Exception e) {
                    LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
                }
            };
            new Thread(runnable).start();
        } else {
            bool_timer = false;
        }
    }

    private void final_delete_the_Bot(Guild guild) {
        /*
        set *first-startup* to true (counter will be disabled with this)
         */
        PropertiesFile.writePropertiesFile("first-startup", "true");
        /*
        delete the gamerole´s
         */
        for (Role role : guild.getRoles()) {
            try {
                List<String> lines = Files.readAllLines(Paths.get("config/games.txt"), StandardCharsets.UTF_8);
                if (lines.contains(role.getName()) || role.getName().equals("GameRole")) {
                    role.delete().queue();
                    break;
                }
            } catch (IOException e) {
                LB.log(Thread.currentThread().getName(), e.getMessage(), "info");
            }
        }
        /*
        delete all bot created category, with their channels
         */
        try {
            ArrayList<String> list_category = new ArrayList<>();
            list_category.add(PropertiesFile.readsPropertiesFile("maincount"));
            list_category.add(PropertiesFile.readsPropertiesFile("gamecategory"));
            list_category.add(PropertiesFile.readsPropertiesFile("streamcategory"));
            for (String s : list_category) {
                Category category = guild.getCategoryById(s);
                for (GuildChannel channel : category.getChannels()) {
                    channel.delete().complete();
                }
                category.delete().complete();
            }
        } catch (IllegalArgumentException | NullPointerException ignored) {
        }
        /*
        delete all folder & .txt files, also clear the config.prop (without the TOKEN AND again the *first-startup* key)
         */
        ArrayList<String> list_token = new ArrayList<>();
        list_token.add(PropertiesFile.readsPropertiesFile("TOKEN"));
        ArrayList<File> list_file = new ArrayList<>();
        list_file.add(new File("config/blacklist/" + guild.getId() + "/"));
        list_file.add(new File("config/blacklist/"));
        list_file.add(new File("config/"));
        for (File directory : list_file) {
            if (directory.exists()) {
                File[] files = directory.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
        }
        try {
            FileWriter writer = new FileWriter(new File("config/config.prop"));
            writer.write("");
            String s = list_token.toString().replace("[", "").replace("]", "");
            PropertiesFile.writePropertiesFile("TOKEN", s);
            PropertiesFile.writePropertiesFile("first-startup", "true");
        } catch (IOException ignored) {
        }
        /*
        stop the bot
         */
        System.exit(0);
    }
}