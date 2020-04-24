package other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class CheckGameOnWebsite {

    private String format_string(String string) {
        return string
                .replaceAll("\\u00AE", "") //®
                .replaceAll("\\u2122", "") //™
                .replaceAll("\\u00B2", "") //²
                .replaceAll("\\u00B3", "") //³
                ;
    }

    public String Steam(String game) {
        String game_format = format_string(game);
        String inputline_STEAM;
        String website_game_STEAM = null;
        URL url_STEAM;
        try {
            if (game_format.contains("pcgamingwiki")) {
                url_STEAM = new URL(game_format);
            } else {
                url_STEAM = new URL("https://www.pcgamingwiki.com/w/index.php?search=" + game_format + "&title=Special%3ASearch&profile=default&fulltext=1");
            }
            URLConnection conn_STEAM = url_STEAM.openConnection();
            BufferedReader br_STEAM = new BufferedReader(new InputStreamReader(conn_STEAM.getInputStream()));

            while ((inputline_STEAM = br_STEAM.readLine()) != null) {
                //not found
                if (inputline_STEAM.contains("There is a page named")) {
                    String title_web = inputline_STEAM;
                    title_web = title_web.substring(title_web.indexOf("title") + 7);
                    title_web = title_web.substring(0, title_web.indexOf("\""));
                    if (game_format.contains(" ")) {
                        game_format = game_format.substring(0, game_format.indexOf(" "));
                    }
                    if (title_web.contains("_")) {
                        title_web = title_web.substring(0, title_web.indexOf("_"));
                    }
                    if (game_format.equalsIgnoreCase(title_web)) {
                        website_game_STEAM = "https://www.pcgamingwiki.com/wiki/" + title_web;
                    }
                }
                //not the URL from the game, just the right name
                if (inputline_STEAM.contains("Page title matches") && inputline_STEAM.contains("href")) {
                    String new_url = inputline_STEAM;
                    if (new_url.contains("href")) {
                        new_url = new_url.substring(new_url.indexOf("href=") + 12);
                        new_url = new_url.substring(0, new_url.indexOf("\""));
                        website_game_STEAM = "https://www.pcgamingwiki.com/wiki/" + new_url;
                        if (game_format.contains(" ")) {
                            game_format = game_format.substring(0, game_format.indexOf(" "));
                        }
                        if (new_url.contains("_")) {
                            new_url = new_url.substring(0, new_url.indexOf("_"));
                        }
                        if (!game_format.equalsIgnoreCase(new_url)) {
                            website_game_STEAM = "https://www.pcgamingwiki.com/wiki/" + new_url;
                        }
                        break;
                    }

                } else if (inputline_STEAM.contains("Page title matches")) {
                    br_STEAM.readLine();
                    String new_url = br_STEAM.readLine();
                    if (new_url.contains("href")) {
                        new_url = new_url.substring(new_url.indexOf("href=") + 12);
                        new_url = new_url.substring(0, new_url.indexOf("\""));
                        website_game_STEAM = "https://www.pcgamingwiki.com/wiki/" + new_url;
                        break;
                    }
                }

                //right URL for the game
                if (inputline_STEAM.contains("href=\"https://store.steampowered.com/app/")) {
                    website_game_STEAM = inputline_STEAM.substring(inputline_STEAM.indexOf("href=") + 6);
                    website_game_STEAM = website_game_STEAM.substring(0, website_game_STEAM.length() - 17);
                    break;
                }
            }

            if (website_game_STEAM != null) {
                website_game_STEAM = website_game_STEAM.replace("utm_source=PCGamingWiki&amp;utm_medium=PCGamingWiki&amp;utm_campaign=PCGamingWik", "").replace("?", "");
            }
        } catch (IOException e) {
            website_game_STEAM = null;
        }
        return "[Steam](" + website_game_STEAM + ")";
    }

    public String EpicGames(String game) {
        String game_format = format_string(game).replaceAll("\\?", "");
        String inputline_EPIC;
        String website_game_EPIC = null;
        boolean game_found = false;
        try {
            URL url_EPIC = new URL("https://www.pcgamingwiki.com/wiki/" + game_format);
            URLConnection conn_EPIC = url_EPIC.openConnection();
            BufferedReader br_EPIC = new BufferedReader(new InputStreamReader(conn_EPIC.getInputStream()));

            while ((inputline_EPIC = br_EPIC.readLine()) != null) {
                if (inputline_EPIC.contains("href=\"https://store.epicgames.com/")) {
                    website_game_EPIC = inputline_EPIC.substring(inputline_EPIC.indexOf("href=") + 6);
                    website_game_EPIC = website_game_EPIC.substring(0, website_game_EPIC.length() - 27);
                    game_found = true;
                    break;
                }
            }

            if (game_found) {
                website_game_EPIC = website_game_EPIC.replace("store.", "").replace("pcgamingwiki", "store/de/product");
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

            while ((inputline_BLIZZARD = br_BLIZZARD.readLine()) != null) {
                if (inputline_BLIZZARD.contains("href=\"https://shop.battle.net/product/")) {
                    website_game_BLIZZARD = inputline_BLIZZARD.substring(inputline_BLIZZARD.indexOf("href=") + 6);
                    website_game_BLIZZARD = website_game_BLIZZARD.substring(0, website_game_BLIZZARD.length() - 21);
                    break;
                }
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
        boolean game_found = false;
        try {
            URL url_ORIGIN = new URL("https://www.pcgamingwiki.com/wiki/" + game_format);
            URLConnection conn_ORIGIN = url_ORIGIN.openConnection();
            BufferedReader br_ORIGIN = new BufferedReader(new InputStreamReader(conn_ORIGIN.getInputStream()));

            while ((inputline_ORIGIN = br_ORIGIN.readLine()) != null) {
                if (inputline_ORIGIN.contains("href=\"https://www.origin.com/usa/en-us/store/")) {
                    website_game_ORIGIN = inputline_ORIGIN.substring(inputline_ORIGIN.indexOf("href=") + 6);
                    website_game_ORIGIN = website_game_ORIGIN.substring(0, website_game_ORIGIN.length() - 17);
                    game_found = true;
                    break;
                }
            }
            if (game_found) {
                website_game_ORIGIN = website_game_ORIGIN.replace("en-us", "de-de");
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

            while ((inputline_UPLAY = br_UPLAY.readLine()) != null) {
                if (inputline_UPLAY.contains("url=http://store.ubi.com/")) {
                    website_game_UPLAY = inputline_UPLAY.substring(inputline_UPLAY.indexOf("com/") + 4);
                    website_game_UPLAY = website_game_UPLAY.substring(0, website_game_UPLAY.length() - 21);
                    website_game_UPLAY = "https://store.ubi.com/de/game?pid=" + website_game_UPLAY;
                    break;
                }
            }
        } catch (IOException e) {
            website_game_UPLAY = null;
        }
        return "[Uplay](" + website_game_UPLAY + ")";
    }

    public String Offical(String game) {
        String game_format = format_string(game);
        String inputline_OFFICAL;
        String website_game_OFFICAL = null;
        try {
            URL url_OFFICAL = new URL("https://www.pcgamingwiki.com/wiki/" + game_format);
            URLConnection conn_OFFICAL = url_OFFICAL.openConnection();
            BufferedReader br_OFFICAL = new BufferedReader(new InputStreamReader(conn_OFFICAL.getInputStream()));

            while ((inputline_OFFICAL = br_OFFICAL.readLine()) != null) {
                if (inputline_OFFICAL.contains("Official website")) {
                    website_game_OFFICAL = inputline_OFFICAL.substring(inputline_OFFICAL.indexOf("href=") + 6);
                    website_game_OFFICAL = website_game_OFFICAL.substring(0, website_game_OFFICAL.length() - 27);
                    break;
                }
            }
        } catch (IOException e) {
            website_game_OFFICAL = null;
        }
        return "[Offical](" + website_game_OFFICAL + ")";
    }
}