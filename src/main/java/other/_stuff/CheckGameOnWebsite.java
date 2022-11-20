package other._stuff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class CheckGameOnWebsite {

    public String[] startcheck(String game) {
        String website_game = check_game_name(format_string(game));

        String[] new_lines = {"null", "null", "null", "null", "null", "null", "null"};

        new_lines[0] = Steam(website_game);
        new_lines[1] = EpicGames(website_game);
        new_lines[2] = Blizzard(website_game);
        new_lines[3] = Origin(website_game);
        new_lines[4] = Uplay(website_game);
        new_lines[5] = Official(website_game);
        new_lines[6] = Discord(website_game);

        return new_lines;
    }

    private String check_game_name(String game_format) {
        String inputlineALL;
        String website_game = null;
        String string_first_word = null;
        try {
            URL url_ALL = new URL("https://www.pcgamingwiki.com/w/index.php?search=" + game_format + "&title=Special%3ASearch&profile=default&fulltext=1");
            URLConnection conn_ALL = url_ALL.openConnection();
            BufferedReader br_ALL = new BufferedReader(new InputStreamReader(conn_ALL.getInputStream()));

            while ((inputlineALL = br_ALL.readLine()) != null) {
                //not found
                if (inputlineALL.contains("There is a page named")) {
                    String title_web = inputlineALL;
                    title_web = title_web.substring(title_web.indexOf("title") + 7);
                    title_web = title_web.substring(0, title_web.indexOf("\""));
                    if (game_format.contains(" ")) {
                        string_first_word = game_format.substring(0, game_format.indexOf(" "));
                    }
                    if (string_first_word == null) {
                        string_first_word = game_format;
                    }
                    if (title_web.contains("_")) {
                        title_web = title_web.substring(0, title_web.indexOf("_"));
                    }
                    if (title_web.toLowerCase().startsWith(string_first_word.toLowerCase()) || game_format.toLowerCase().equalsIgnoreCase(title_web.toLowerCase())) {
                        website_game = inputlineALL.substring(inputlineALL.indexOf("href=") + 6);
                        website_game = "https://www.pcgamingwiki.com" + website_game.substring(0, website_game.indexOf("\""));
                        break;
                    }

                } else if (inputlineALL.contains("Page title matches") && inputlineALL.contains("href")) {
                    String new_url = inputlineALL;
                    if (new_url.contains("href")) {
                        new_url = new_url.substring(new_url.indexOf("href=") + 12);
                        new_url = new_url.substring(0, new_url.indexOf("\""));
                        if (game_format.contains(" ")) {
                            string_first_word = game_format.substring(0, game_format.indexOf(" "));
                        }
                        if (string_first_word == null) {
                            string_first_word = game_format;
                        }
                        if (new_url.toLowerCase().startsWith(string_first_word.toLowerCase())) {
                            website_game = "https://www.pcgamingwiki.com/wiki/" + new_url.replaceAll("&", "%20");
                            break;
                        }
                    }

                } else if (inputlineALL.contains("Page title matches")) {
                    br_ALL.readLine();
                    String new_url = br_ALL.readLine();
                    if (new_url.contains("href")) {
                        new_url = new_url.substring(new_url.indexOf("href=") + 12);
                        new_url = new_url.substring(0, new_url.indexOf("\""));
                        website_game = "https://www.pcgamingwiki.com/wiki/" + new_url;
                        break;
                    }

                } else if (inputlineALL.startsWith("<ul class='mw-search-results'><li><div class='mw-search-result-heading'>")) {
                    inputlineALL = inputlineALL.substring(inputlineALL.indexOf("href=") + 6);
                    inputlineALL = inputlineALL.substring(0, inputlineALL.indexOf("\""));

                    if (game_format.contains(" ")) {
                        string_first_word = game_format.substring(0, game_format.indexOf(" "));
                    }
                    if (string_first_word == null) {
                        string_first_word = game_format;
                    }
                    if (inputlineALL.substring(6).toLowerCase().startsWith(string_first_word.toLowerCase())) {
                        website_game = "https://www.pcgamingwiki.com" + inputlineALL;
                    }
                    break;
                }
            }
        } catch (IOException e) {
            website_game = null;
        }
        return website_game;
    }

    private String format_string(String string) {
        return string
                .replaceAll("\\u00AE", "") //®
                .replaceAll("\\u2122", "") //™
                .replaceAll("\\u00B2", "") //²
                .replaceAll("\\u00B3", "") //³
                .replaceAll("&", "+%26+")
                .replaceAll("'", "%27")
                ;
    }

    private String Steam(String website) {
        String inputline_STEAM;
        String website_game_STEAM = null;
        URL url_STEAM;
        try {
            url_STEAM = new URL(website);
            URLConnection conn_STEAM = url_STEAM.openConnection();
            BufferedReader br_STEAM = new BufferedReader(new InputStreamReader(conn_STEAM.getInputStream()));

            while ((inputline_STEAM = br_STEAM.readLine()) != null) {
                //right URL for the game
                if (inputline_STEAM.contains("href=\"https://store.steampowered.com/app/") && !inputline_STEAM.contains("/workshop/")) {
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

    private String EpicGames(String website) {
        String inputline_EPIC;
        String website_game_EPIC = null;
        boolean game_found = false;
        try {
            URL url_EPIC = new URL(website);
            URLConnection conn_EPIC = url_EPIC.openConnection();
            BufferedReader br_EPIC = new BufferedReader(new InputStreamReader(conn_EPIC.getInputStream()));

            while ((inputline_EPIC = br_EPIC.readLine()) != null) {
                //right URL for the game
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

    private String Blizzard(String website) {
        String inputline_BLIZZARD;
        String website_game_BLIZZARD = null;
        try {
            URL url_BLIZZARD = new URL(website);
            URLConnection conn_BLIZZARD = url_BLIZZARD.openConnection();
            BufferedReader br_BLIZZARD = new BufferedReader(new InputStreamReader(conn_BLIZZARD.getInputStream()));

            while ((inputline_BLIZZARD = br_BLIZZARD.readLine()) != null) {
                //right URL for the game
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

    private String Origin(String website) {
        String inputline_ORIGIN;
        String website_game_ORIGIN = null;
        boolean game_found = false;
        try {
            URL url_ORIGIN = new URL(website);
            URLConnection conn_ORIGIN = url_ORIGIN.openConnection();
            BufferedReader br_ORIGIN = new BufferedReader(new InputStreamReader(conn_ORIGIN.getInputStream()));

            while ((inputline_ORIGIN = br_ORIGIN.readLine()) != null) {
                //right URL for the game
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

    private String Uplay(String website) {
        String inputline_UPLAY;
        String website_game_UPLAY = null;
        try {
            URL url_UPLAY = new URL(website);
            URLConnection conn_UPLAY = url_UPLAY.openConnection();
            BufferedReader br_UPLAY = new BufferedReader(new InputStreamReader(conn_UPLAY.getInputStream()));

            while ((inputline_UPLAY = br_UPLAY.readLine()) != null) {
                //right URL for the game
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

    private String Official(String website) {
        String inputline_OFFICIAL;
        String website_game_OFFICIAL = null;
        try {
            URL url_OFFICIAL = new URL(website);
            URLConnection conn_OFFICIAL = url_OFFICIAL.openConnection();
            BufferedReader br_OFFICIAL = new BufferedReader(new InputStreamReader(conn_OFFICIAL.getInputStream()));

            while ((inputline_OFFICIAL = br_OFFICIAL.readLine()) != null) {
                //right URL for the game
                if (inputline_OFFICIAL.contains("Official website")) {
                    website_game_OFFICIAL = inputline_OFFICIAL.substring(inputline_OFFICIAL.indexOf("href=") + 6);
                    website_game_OFFICIAL = website_game_OFFICIAL.substring(0, website_game_OFFICIAL.indexOf("\""));
                    break;
                }
            }
        } catch (IOException e) {
            website_game_OFFICIAL = null;
        }
        return "[Official](" + website_game_OFFICIAL + ")";
    }

    private String Discord(String website) {
        String inputline_DISCORD;
        String website_game_DISCORD = null;
        try {
            URL url_DISCORD = new URL(website);
            URLConnection conn_DISCORD = url_DISCORD.openConnection();
            BufferedReader br_DISCORD = new BufferedReader(new InputStreamReader(conn_DISCORD.getInputStream()));
            boolean first = false;

            while ((inputline_DISCORD = br_DISCORD.readLine()) != null) {
                //right URL for the game
                if (inputline_DISCORD.contains("Discord") && !first) {
                    first = true;
                } else if (inputline_DISCORD.contains("Discord") && inputline_DISCORD.contains("/store/skus/") && first) {
                    website_game_DISCORD = inputline_DISCORD.substring(inputline_DISCORD.indexOf("href=") + 6);
                    website_game_DISCORD = website_game_DISCORD.substring(0, website_game_DISCORD.indexOf("\""));
                    break;
                }
            }
        } catch (IOException e) {
            website_game_DISCORD = null;
        }
        return "[Discord](" + website_game_DISCORD + ")";
    }
}