package listener.commands;

import check_create.CheckCategory;
import config.PropertiesFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class League_Command extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            if (event.isFromType(ChannelType.TEXT)) {
                if (!event.getAuthor().isBot()) {
                    if (event.getMessage().getContentRaw().startsWith(">")) {
                        if (PropertiesFile.readsPropertiesFile("bot-channel").isEmpty()) {
                            CheckCategory C_Category = new CheckCategory();
                            C_Category.checkingCategory(event.getGuild(), "gamecategory");
                            event.getMessage().delete().queue();
                        } else {
                            String[] argArray = event.getMessage().getContentRaw().split(" ");
                            if (argArray[0].equals(">league")) {
                                /*
                                compine all arguments
                                 */
                                ArrayList<String> list_name = new ArrayList<>();

                                for (int x = 1; x < argArray.length; x++) {
                                    list_name.add(argArray[x]);
                                }
                                String username = String.join(" ", list_name);

                                try {
                                    String inputline;
                                    String SUBstring;

                                    /*
                                    list for solo/duo
                                    */
                                    List<String> list_1 = new ArrayList<>();
                                    /*
                                    list for flexi
                                    */
                                    List<String> list_2 = new ArrayList<>();
                                    /*
                                    list for summoner details
                                    */
                                    List<String> list_3 = new ArrayList<>();

                                    URL url = new URL("https://euw.op.gg/summoner/userName=" + username.replace(" ", "+"));
                                    URLConnection conn = url.openConnection();

                                    //########################################################################################################################//

                                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                    while ((inputline = br.readLine()) != null) {

                                        /*
                                        Summoner Icon
                                        */
                                        if (inputline.contains("ProfileImage")) {
                                            SUBstring = inputline.trim().substring(12);
                                            SUBstring = SUBstring.substring(0, SUBstring.indexOf(".jpg") + 4);
                                            list_3.add("URL" + "https://" + SUBstring.replace(";", "&"));

                                        /*
                                        Summoner Level
                                        */
                                        } else if (inputline.contains("<span class=\"Level tip\" title=\"Level\">")) {
                                            SUBstring = inputline.trim().substring(38);
                                            SUBstring = SUBstring.substring(0, SUBstring.length() - 7);
                                            list_3.add("Summoner Name > " + username);
                                            list_3.add("Summoner Level > " + SUBstring);

                                        /*
                                        last update
                                        */
                                        } else if (inputline.contains("Last updated: <span class=' _timeago _timeCount'")) {
                                            /*
                                            get only the year
                                            */
                                            String year;
                                            year = inputline.trim().substring(108);
                                            year = year.substring(0, year.length() - 22);

                                            /*
                                            get only the month
                                            */
                                            String month;
                                            month = inputline.trim().substring(113);
                                            month = month.substring(0, month.length() - 19);

                                            /*
                                            get only the date
                                            */
                                            String date;
                                            date = inputline.trim().substring(116);
                                            date = date.substring(0, date.length() - 16);

                                            /*
                                            get only the hour
                                            */
                                            String hour;
                                            hour = inputline.trim().substring(119);
                                            hour = hour.substring(0, hour.length() - 13);
                                            int i_hour = Integer.parseInt(hour);
                                            int i_date = Integer.parseInt(date);

                                            if (i_hour < 8) {
                                                i_hour = i_hour + 24 - 8;
                                                i_date = i_date - 1;
                                            } else {
                                                i_hour = i_hour - 8;
                                            }

                                            /*
                                            get minute & seconds
                                            */
                                            String resttime;
                                            resttime = inputline.trim().substring(122);
                                            resttime = resttime.substring(0, resttime.length() - 7);

                                            list_3.add("Last Update (on op.gg) > " + i_date + "-" + month + "-" + year + " | " + i_hour + ":" + resttime);

                                        /*
                                        URL for Solo/Duo Image
                                        */
                                        } else if (inputline.contains("<div class=\"Medal")) {
                                            SUBstring = br.readLine().trim().substring(12);
                                            SUBstring = SUBstring.substring(0, SUBstring.indexOf(".png") + 4);
                                            list_1.add("URL" + "https://" + SUBstring.replace(";", "&"));

                                        /*
                                        Solo/Duo
                                        */
                                        } else if (inputline.contains("<div class=\"TierRank unranked")) {
                                            list_1.add("Unranked");
                                        } else if (inputline.contains("<div class=\"TierRank\"")) {
                                            String rank;
                                            String points;
                                            String win;
                                            String lose;
                                            String ratio;

                                            rank = inputline.trim().substring(22);
                                            rank = rank.substring(0, rank.length() - 6);

                                            br.readLine();
                                            br.readLine();

                                            points = br.readLine().trim().replace(" ", "");

                                            br.readLine();
                                            br.readLine();
                                            br.readLine();

                                            win = br.readLine().trim().substring(19);
                                            win = win.substring(0, win.length() - 7);

                                            lose = br.readLine().trim().substring(21);
                                            lose = lose.substring(0, lose.length() - 7);

                                            br.readLine();

                                            ratio = br.readLine().trim().substring(23);
                                            ratio = ratio.substring(0, ratio.length() - 7).replace("Ratio", "Rate");

                                            list_1.add(rank);
                                            list_1.add(points);
                                            list_1.add(win + "/" + lose);
                                            list_1.add(ratio);

                                        /*
                                        URL for Flexi Image
                                        */
                                        } else if (inputline.contains("<div class=\"sub-tier\">")) {
                                            SUBstring = br.readLine().trim().substring(12);
                                            SUBstring = SUBstring.substring(0, SUBstring.indexOf(".png") + 4);
                                            list_2.add("URL" + "https://" + SUBstring.replace(";", "&"));

                                        /*
                                        Flexi
                                        */
                                        } else if (inputline.contains("<div class=\"sub-tier__rank-tier unranked\">")) {
                                            list_2.add("Unranked");
                                        } else if (inputline.contains("<div class=\"sub-tier__rank-tier \">")) {
                                            String rank;
                                            String points;
                                            String win_lose;
                                            String ratio;
                                            String line;

                                            rank = br.readLine().trim();

                                            br.readLine();

                                            line = br.readLine();

                                            points = line.substring(line.indexOf(">") + 1, line.indexOf("LP") + 2);

                                            win_lose = line.substring(line.indexOf("/") + 2, line.indexOf("</span") - 1).replace(" ", "/");

                                            br.readLine();

                                            ratio = br.readLine().trim();

                                            list_2.add(rank);
                                            list_2.add(points);
                                            list_2.add(win_lose);
                                            list_2.add(ratio);
                                        }
                                    }
                                    br.close();

                                    /*
                                    Finish, now create the builder
                                    */
                                    if (list_1.isEmpty()) {
                                        event.getChannel().sendMessage(event.getMember().getAsMention() + ", Summoner *" + username + "* not found! (only EUW)").queue();
                                        return;
                                    }

                                    EmbedBuilder builder_1 = new EmbedBuilder().setTitle("League of Legends, " + username + " (SoloQ/DuoQ)");
                                    EmbedBuilder builder_2 = new EmbedBuilder().setTitle("League of Legends, " + username + " (FlexiQ)");
                                    EmbedBuilder builder_3 = new EmbedBuilder().setTitle("League of Legends, " + username + " (Summoner)");

                                    /*
                                    URL for OP.GG
                                    */
                                    String iconurl = "https://static-s.aa-cdn.net/img/ios/605722237/0ccfe17ce50eef3ab49494737efb17d2";

                                    for (String string : list_3) {
                                        if (string.startsWith("URL")) {
                                            builder_3.setThumbnail(string.substring(3));
                                        } else {
                                            builder_3.getDescriptionBuilder().append(string + "\n");
                                        }
                                    }

                                    for (String string : list_1) {
                                        if (string.startsWith("URL")) {
                                            builder_1.setThumbnail(string.substring(3));
                                        } else {
                                            builder_1.getDescriptionBuilder().append(string + "\n");
                                        }
                                    }

                                    for (String string : list_2) {
                                        if (string.startsWith("URL")) {
                                            builder_2.setThumbnail(string.substring(3));
                                        } else {
                                            builder_2.getDescriptionBuilder().append(string + "\n");
                                        }
                                    }

                                    builder_1.setFooter("Data from op.gg", iconurl);
                                    builder_2.setFooter("Data from op.gg", iconurl);
                                    builder_3.setFooter("Data from op.gg", iconurl);

                                    event.getChannel().sendMessage(builder_3.build()).queue();
                                    event.getChannel().sendMessage(builder_1.build()).queue();
                                    event.getChannel().sendMessage(builder_2.build()).queue();

                                } catch (IOException e) {
                                    System.err.println(e.getMessage());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}