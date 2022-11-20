package config._Check;

import config.PropertiesFile;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CheckKey {

    private static final ArrayList<String> list = new ArrayList<>() {{
        /*
        start
        */
        add("first_startup");
        add("bot_starting");
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

        add(">clips_on");
        add(">videos_on");

        add("clips_number");
        add("clips_period");

        add("videos_number");
        add("videos_archice_true?");

        add("videos_uplaod_true?");
        add("videos_highlight_true?");
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

        add("emotecount");
        add(">emotecount_on");
        /*
        Counter - Other
        (if game is enabled)
        */
        add("gamecount");
        add(">gamecount_on");

        add("playingcount");
        add(">playingcount_on");

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
        /*
        other
        */
        add("agreement");
    }};

    public void StartChecking() {
        PropertiesFile.writePropertiesFile("bot_starting", "true", "config");
        for (String key : list) {
            checking(key);
        }
    }

    public void checking(String key) {
        if (PropertiesFile.readsPropertiesFile(key, "config") == null || PropertiesFile.readsPropertiesFile(key, "config").isEmpty() || PropertiesFile.readsPropertiesFile(key, "config").isBlank()) {
            if (key.contains(">logs_on")) {
                PropertiesFile.writePropertiesFile(key, "false", "config");
            } else if (key.equals("bot_starting") || key.contains("_on") || key.equals("first_startup")) {
                PropertiesFile.writePropertiesFile(key, "true", "config");
            } else {
                PropertiesFile.writePropertiesFile(key, "", "config");
            }
        }
    }

    public void SortKey() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("config/config.prop"), StandardCharsets.UTF_8);
            ArrayList<String> file_list = new ArrayList<>();
            for (int x = 0; x < list.size(); x++) {
                for (String line : lines) {
                    if (!line.startsWith("#")) {
                        if (line.startsWith(list.get(x))) {
                            if (x == 0) {
                                file_list.add("#    Config for the Discord - Bot | Made by RzR32    #");
                            } else if ((x % 2) == 0) {
                                file_list.add("\n");
                            }
                            file_list.add("\n" + line);
                        }
                    }
                }
            }
            FileWriter writer = new FileWriter("config/config.prop", false);
            for (String s : file_list) {
                writer.write(s);
            }
            writer.close();
        } catch (IOException ignored) {
        }
    }
}