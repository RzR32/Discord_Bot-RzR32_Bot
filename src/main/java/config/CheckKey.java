package config;

import other.LogBack;

import java.util.ArrayList;

public class CheckKey {

    LogBack LB = new LogBack();

    public void checking() {
        ArrayList<String> list = new ArrayList<>();
        list.add("agreement");
        list.add("bot-channel");
        list.add("bot-zustimmung");
        list.add("categorycount");
        list.add("gamecategory");
        list.add("gamecount");
        list.add("gamerolecount");
        list.add("games");
        list.add("logs");
        list.add("maincount");
        list.add("membercount");
        list.add("playingcount");
        list.add("rolecount");
        list.add("streamcategory");
        list.add("textchannelcount");
        list.add("TOKEN");
        list.add("twitchcount");
        list.add("twitchname");
        list.add("voicechannelcount");
        for (String key : list) {
            if (PropertiesFile.readsPropertiesFile(key) == null || PropertiesFile.readsPropertiesFile(key).isEmpty() || PropertiesFile.readsPropertiesFile(key).isBlank()) {
                LB.log(Thread.currentThread().getName(), "Missing Key found *" + key + "* !", "warn");
                PropertiesFile.writePropertiesFile(key, "");
            }
        }
    }
}