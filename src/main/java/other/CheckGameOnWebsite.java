package other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class CheckGameOnWebsite {

    private String format_string_1(String string) {
        String string_format = string.replaceAll(" ", "-");
        string_format = string_format.replaceAll("'", "");
        string_format = string_format.replaceAll("@", "");
        string_format = string_format.replaceAll("®", "");
        string_format = string_format.replaceAll("™", "");
        string_format = string_format.toLowerCase();
        return (string_format);
    }

    private String format_string_2(String string) {
        String string_format = string.replaceAll(" ", "_");
        string_format = string_format.replaceAll("'", "");
        string_format = string_format.replaceAll("@", "");
        string_format = string_format.replaceAll("®", "");
        string_format = string_format.replaceAll("™", "");
        return (string_format);
    }

    private String format_string_3(String string) {
        String string_format = string.replaceAll("'", "");
        string_format = string_format.replaceAll("@", "");
        string_format = string_format.replaceAll("®", "");
        string_format = string_format.replaceAll("™", "");
        return (string_format);
    }

    public String Steam(String game) {
        String inputline_STEAM;
        String website_game_STEAM = null;
        String game_name_on_website_STEAM;
        try {
            URL url_STEAM = new URL("https://store.steampowered.com/search/?term=" + game);
            URLConnection conn_STEAM = url_STEAM.openConnection();
            BufferedReader br_STEAM = new BufferedReader(new InputStreamReader(conn_STEAM.getInputStream()));

            while ((inputline_STEAM = br_STEAM.readLine()) != null) {
                if (inputline_STEAM.contains("<a href=\"https://store.steampowered.com/app/")) {
                    website_game_STEAM = inputline_STEAM.substring(11);
                    website_game_STEAM = website_game_STEAM.substring(0, website_game_STEAM.indexOf("\""));

                    br_STEAM.readLine();
                    br_STEAM.readLine();
                    br_STEAM.readLine();

                    game_name_on_website_STEAM = br_STEAM.readLine().trim().substring(20);
                    game_name_on_website_STEAM = game_name_on_website_STEAM.substring(0, game_name_on_website_STEAM.length() - 7);
                    if (game.length() >= format_string_3(game_name_on_website_STEAM).length() / 2) {
                        if (format_string_3(game_name_on_website_STEAM).contains(game)) {
                        } else {
                            website_game_STEAM = null;
                        }
                    } else {
                        website_game_STEAM = null;
                    }
                    break;
                }
            }
        } catch (IOException e) {
            website_game_STEAM = null;
        }
        return "[Steam](" + website_game_STEAM + ")";
    }

    public String EpicGames(String game) {
        String game_format = format_string_1(game);
        String inputline_EPIC;
        String website_game_EPIC = "https://www.epicgames.com/store/de/product/" + game_format + "/home";
        try {
            URL url_EPIC = new URL("https://www.pcgamingwiki.com/wiki/" + format_string_2(game));
            URLConnection conn_EPIC = url_EPIC.openConnection();
            BufferedReader br_EPIC = new BufferedReader(new InputStreamReader(conn_EPIC.getInputStream()));

            boolean game_found = false;

            while ((inputline_EPIC = br_EPIC.readLine()) != null) {
                if (inputline_EPIC.contains("href=\"https://store.epicgames.com/")) {
                    game_found = true;
                    break;
                }
            }
            if (game_found) {
            } else {
                website_game_EPIC = null;
            }
        } catch (IOException e) {
            website_game_EPIC = null;
        }
        return "[EpicGames](" + website_game_EPIC + ")";
    }

    public String Blizzard(String game) {
        String game_format = format_string_1(game);
        String inputline_BLIZZARD;
        String website_game_BLIZZARD = "https://eu.shop.battle.net/de-de/product/" + game_format;
        try {
            URL url_BLIZZARD = new URL("https://eu.battle.net/support/de/");
            URLConnection conn_BLIZZARD = url_BLIZZARD.openConnection();
            BufferedReader br_BLIZZARD = new BufferedReader(new InputStreamReader(conn_BLIZZARD.getInputStream()));

            while ((inputline_BLIZZARD = br_BLIZZARD.readLine()) != null) {
                if (inputline_BLIZZARD.contains(game)) {
                    break;
                } else if (inputline_BLIZZARD.contains("</html>")) {
                    website_game_BLIZZARD = null;
                }
            }
        } catch (IOException e) {
            website_game_BLIZZARD = null;
        }
        return "[Blizzard](" + website_game_BLIZZARD + ")";
    }

    public String Origin(String game) {
        String inputline_ORIGIN;
        String website_game_ORIGIN = null;
        try {
            URL url_ORIGIN = new URL("https://www.pcgamingwiki.com/wiki/" + format_string_2(game));
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
        String inputline_UPLAY;
        String website_game_UPLAY = null;
        try {
            URL url_UPLAY = new URL("https://www.pcgamingwiki.com/wiki/" + format_string_2(game));
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