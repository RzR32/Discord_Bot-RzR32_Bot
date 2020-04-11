package twitch;

import config.PropertiesFile;
import other.LogBack;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MakeRequest {

    LogBack LB = new LogBack();

    public String[] doRequest(String url_extra) {
        String CLIENT_ID = PropertiesFile.readsPropertiesFile("Twitch_ClientID");
        String[] list = new String[0];
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
            if (e.getMessage().contains("400")) {
                LB.log(Thread.currentThread().getName(), "Error: Server response 400, maybe Client_ID is wrong or missing!", "error");
            } else {
                LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
            }
        }
        return list;
    }
}