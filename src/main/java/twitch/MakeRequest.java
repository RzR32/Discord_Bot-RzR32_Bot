package twitch;

import config.PropertiesFile;
import other._stuff.LogBack;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MakeRequest {

    LogBack LB = new LogBack();

    public String[] doRequest(String url_extra) {
        if (PropertiesFile.readsPropertiesFile("TOKEN", "twitchtoken") == null || PropertiesFile.readsPropertiesFile("TOKEN", "twitchtoken").isEmpty()) {
            LB.log(Thread.currentThread().getName(), "No Twitch Token, cant make Request!", "error");
            return null;
        } else {
            String CLIENT_ID = PropertiesFile.readsPropertiesFile("TOKEN", "twitchtoken");
            String[] list;
            try {
                URL url = new URL("https://api.twitch.tv/kraken/" + url_extra);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

                con.setRequestMethod("GET");
                con.setRequestProperty("accept", "application/vnd.twitchtv.v5+json");
                con.setRequestProperty("client-id", CLIENT_ID);
                con.connect();

                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                String out = null;
                while ((line = reader.readLine()) != null) {
                    out = line;
                }
                con.disconnect();

                list = out.split(",");
            } catch (IOException e) {
                LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
                list = null;
            }
            return list;
        }
    }
}