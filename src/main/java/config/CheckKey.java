package config;

import other.LogBack;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CheckKey {

    LogBack LB = new LogBack();


    private static ArrayList<String> list = new ArrayList<>() {{
        add("TOKEN");
        add("first-startup");
        /*
        Category
         */
        add("maincount");
        add(">maincount_on");

        add("gamecategory");
        add(">gamecategory_on");

        add("streamcategory");
        add(">streamcategory_on");
        /*
        Twitch
        (if stream is enabled)
         */
        add("twitchname");
        add(">twitchname_on");

        add("twitchcount");
        add(">twitchcount_on");
        /*
        Counter - Guild
        (if main is enabled)
         */
        add("membercount");
        add(">membercount_on");

        add("rolecount");
        add(">rolecount_on");

        add("gamerolecount");
        add(">gamerolecount_on");

        add("categorycount");
        add(">categorycount_on");

        add("textchannelcount");
        add(">textchannelcount_on");

        add("voicechannelcount");
        add(">voicechannelcount_on");
        /*
        Counter - Other
        (if game is enabled)
         */
        add("gamecount");
        add(">gamecount_on");

        add("playingcount");
        add(">playingcount_on");
        /* - */
        add("games");
        add(">games_on");

        add("logs");
        add(">logs_on");
        /*
        Bot
        (if game is enabled)
         */
        add("bot-channel");
        add(">bot-channel_on");

        add("bot-zustimmung");
        add(">bot-zustimmung_on");

        add("agreement");
    }};

    public void StartChecking() {
        for (String category : list) {
            checking(category);
        }
    }

    public void checking(String key) {
        if (PropertiesFile.readsPropertiesFile(key) == null || PropertiesFile.readsPropertiesFile(key).isEmpty() || PropertiesFile.readsPropertiesFile(key).isBlank()) {
            LB.log(Thread.currentThread().getName(), "Missing Key found *" + key + "* !", "warn");
            if (key.contains("_on") || key.equals("first-startup")) {
                PropertiesFile.writePropertiesFile(key, "true");
            } else {
                PropertiesFile.writePropertiesFile(key, "");
            }
        }
        SortKey();
    }

    public void SortKey() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("config/config.prop"), StandardCharsets.UTF_8);
            ArrayList<String> file_list = new ArrayList<>();
            for (int x = 0; x < list.size(); x++) {
                for (String token : lines) {
                    if (!token.startsWith("#") && (!token.startsWith("/*"))) {
                        if (token.startsWith(list.get(x))) {
                            if (x == 0) {
                                file_list.add("/*   Config for the Discord - Bot | Made by RzR32    */");
                            } else if ((x % 2) == 0) {
                                file_list.add("/**/");
                            }
                            file_list.add(token);
                        }
                    }
                }
            }
            FileWriter writer = new FileWriter("config/config.prop", false);
            for (String s : file_list) {
                writer.write(s + "\n");
            }
            writer.close();
        } catch (IOException ignored) {
        }
    }
}