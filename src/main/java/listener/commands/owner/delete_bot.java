package listener.commands.owner;

import config.PropertiesFile;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import other.LogBack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class delete_bot {

    private static final LogBack LB = new LogBack();

    private static boolean bool_timer = false;
    private static boolean bool_is_confirmed = false;

    public static void command(Guild guild, Message message) {
        if (!bool_timer) {
            timer_ownercommands(guild, message, 11);
        } else {
            message.addReaction("\uD83D\uDC4D").queue();
            bool_is_confirmed = true;
        }
    }

    private static void timer_ownercommands(Guild guild, Message message, int x) {
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
                        try {
                            message.addReaction("U+3" + f_x + "U+20e3").queue();
                        } catch (ErrorResponseException e) {
                            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
                        }
                    }
                    timer_ownercommands(guild, message, f_x);
                } catch (Exception e) {
                    LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
                }
            };
            new Thread(runnable).start();
        } else {
            bool_timer = false;
        }
    }

    private static void final_delete_the_Bot(Guild guild) {
        /*
        set *first_startup* to true (counter will be disabled with this)
        */
        PropertiesFile.writePropertiesFile("first_startup", "true", "config");
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
            list_category.add(PropertiesFile.readsPropertiesFile("maincount", "config"));
            list_category.add(PropertiesFile.readsPropertiesFile("gamecategory", "config"));
            list_category.add(PropertiesFile.readsPropertiesFile("streamcategory", "config"));
            for (String s : list_category) {
                Category category = guild.getCategoryById(s);
                assert category != null;
                for (GuildChannel channel : category.getChannels()) {
                    channel.delete().complete();
                }
                category.delete().complete();
            }
        } catch (IllegalArgumentException | NullPointerException ignored) {
        }
        /*
        delete all folder & .txt files, also clear the config.prop (without the TOKEN AND again the *first_startup* key)
        */
        ArrayList<String> list_token = new ArrayList<>();
        list_token.add(PropertiesFile.readsPropertiesFile("TOKEN", "discordtoken"));
        ArrayList<File> list_file = new ArrayList<>();
        list_file.add(new File("config/blacklist/" + guild.getId() + "/"));
        list_file.add(new File("config/blacklist/"));
        list_file.add(new File("config/"));
        boolean b;
        for (File directory : list_file) {
            if (directory.exists()) {
                File[] files = directory.listFiles();
                for (File file : files) {
                    b = file.delete();
                    if (!b) {
                        LB.log(Thread.currentThread().getName(), file.getName() + " could not be deleted!", "error");
                    }
                }
            }
        }
        try {
            FileWriter writer = new FileWriter(new File("config/config.prop"));
            writer.write("");
            String s = list_token.toString().replace("[", "").replace("]", "");
            PropertiesFile.writePropertiesFile("TOKEN", s, "discordtoken");
            PropertiesFile.writePropertiesFile("first_startup", "true", "config");
        } catch (IOException ignored) {
        }
        /*
        stop the bot
        */
        System.exit(0);
    }
}