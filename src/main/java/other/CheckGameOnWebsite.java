package other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class CheckGameOnWebsite {

    private String format_string(String string) {
        return string
                .replaceAll(" ", "_")
                .replaceAll("&", "%26")
                //.replaceAll("®", "")
                .replaceAll("\\u00AE", "")
                //.replaceAll("™", "")
                .replaceAll("\\u2122", "")
                .replaceAll("�", "")
                ;
    }

    public String Steam(String game) {
        String game_format = format_string(game);
        String inputline_STEAM;
        String website_game_STEAM = null;
        try {
            URL url_STEAM = new URL("https://www.pcgamingwiki.com/wiki/" + game_format);
            URLConnection conn_STEAM = url_STEAM.openConnection();
            BufferedReader br_STEAM = new BufferedReader(new InputStreamReader(conn_STEAM.getInputStream()));

            boolean game_found_STEAM = false;

            while ((inputline_STEAM = br_STEAM.readLine()) != null) {
                if (inputline_STEAM.contains("href=\"https://store.steampowered.com/app/")) {
                    website_game_STEAM = inputline_STEAM.substring(inputline_STEAM.indexOf("href=") + 6);
                    website_game_STEAM = website_game_STEAM.substring(0, website_game_STEAM.length() - 17);
                    game_found_STEAM = true;
                    break;
                }
            }
            if (game_found_STEAM) {
            } else {
                website_game_STEAM = null;
            }
        } catch (IOException e) {
            website_game_STEAM = null;
        }
        return "[Steam](" + website_game_STEAM + ")";
    }

    public String EpicGames(String game) {
        String game_format = format_string(game);
        String inputline_EPIC;
        String website_game_EPIC = null;
        try {
            URL url_EPIC = new URL("https://www.pcgamingwiki.com/wiki/" + game_format);
            URLConnection conn_EPIC = url_EPIC.openConnection();
            BufferedReader br_EPIC = new BufferedReader(new InputStreamReader(conn_EPIC.getInputStream()));

            boolean game_found_EPIC = false;

            while ((inputline_EPIC = br_EPIC.readLine()) != null) {
                if (inputline_EPIC.contains("href=\"https://store.epicgames.com/")) {
                    website_game_EPIC = inputline_EPIC.substring(inputline_EPIC.indexOf("href=") + 6);
                    website_game_EPIC = website_game_EPIC.substring(0, website_game_EPIC.length() - 27);
                    game_found_EPIC = true;
                    break;
                }
            }
            if (game_found_EPIC) {
            } else {
                website_game_EPIC = null;
            }
        } catch (IOException e) {
            website_game_EPIC = null;
        }
        return "[EpicGames](" + website_game_EPIC + ")";
    }

    public String Blizzard(String game) {
        String game_format = format_string(game);
        String inputline_BLIZZARD;
        String website_game_BLIZZARD = null;
        try {
            URL url_BLIZZARD = new URL("https://www.pcgamingwiki.com/wiki/" + game_format);
            URLConnection conn_BLIZZARD = url_BLIZZARD.openConnection();
            BufferedReader br_BLIZZARD = new BufferedReader(new InputStreamReader(conn_BLIZZARD.getInputStream()));

            boolean game_found_BLIZZARD = false;
            while ((inputline_BLIZZARD = br_BLIZZARD.readLine()) != null) {
                if (inputline_BLIZZARD.contains("href=\"https://shop.battle.net/product/")) {
                    website_game_BLIZZARD = inputline_BLIZZARD.substring(inputline_BLIZZARD.indexOf("href=") + 6);
                    website_game_BLIZZARD = website_game_BLIZZARD.substring(0, website_game_BLIZZARD.length() - 21);
                    game_found_BLIZZARD = true;
                    break;
                }
            }
            if (game_found_BLIZZARD) {
            } else {
                website_game_BLIZZARD = null;
            }
        } catch (IOException e) {
            website_game_BLIZZARD = null;
        }
        return "[Blizzard](" + website_game_BLIZZARD + ")";
    }

    public String Origin(String game) {
        String game_format = format_string(game);
        String inputline_ORIGIN;
        String website_game_ORIGIN = null;
        try {
            URL url_ORIGIN = new URL("https://www.pcgamingwiki.com/wiki/" + game_format);
            URLConnection conn_ORIGIN = url_ORIGIN.openConnection();
            BufferedReader br_ORIGIN = new BufferedReader(new InputStreamReader(conn_ORIGIN.getInputStream()));

            boolean game_found_ORIGIN = false;

            while ((inputline_ORIGIN = br_ORIGIN.readLine()) != null) {
                if (inputline_ORIGIN.contains("href=\"https://www.origin.com/usa/en-us/store/")) {
                    website_game_ORIGIN = inputline_ORIGIN.substring(inputline_ORIGIN.indexOf("href=") + 6);
                    website_game_ORIGIN = website_game_ORIGIN.substring(0, website_game_ORIGIN.length() - 17);
                    game_found_ORIGIN = true;
                    break;
                }
            }
            if (game_found_ORIGIN) {
            } else {
                website_game_ORIGIN = null;
            }
        } catch (IOException e) {
            website_game_ORIGIN = null;
        }
        return "[Origin](" + website_game_ORIGIN + ")";
    }

    public String Uplay(String game) {
        String game_format = format_string(game);
        String inputline_UPLAY;
        String website_game_UPLAY = null;
        try {
            URL url_UPLAY = new URL("https://www.pcgamingwiki.com/wiki/" + game_format);
            URLConnection conn_UPLAY = url_UPLAY.openConnection();
            BufferedReader br_UPLAY = new BufferedReader(new InputStreamReader(conn_UPLAY.getInputStream()));

            boolean game_found_UPLAY = false;

            while ((inputline_UPLAY = br_UPLAY.readLine()) != null) {
                if (inputline_UPLAY.contains("url=http://store.ubi.com/")) {
                    website_game_UPLAY = inputline_UPLAY.substring(inputline_UPLAY.indexOf("com/") + 4);
                    website_game_UPLAY = website_game_UPLAY.substring(0, website_game_UPLAY.length() - 21);
                    website_game_UPLAY = "https://store.ubi.com/de/game?pid=" + website_game_UPLAY;
                    game_found_UPLAY = true;
                    break;
                }
            }
            if (game_found_UPLAY) {
            } else {
                website_game_UPLAY = null;
            }
        } catch (IOException e) {
            website_game_UPLAY = null;
        }
        return "[Uplay](" + website_game_UPLAY + ")";
    }
}