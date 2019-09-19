package listener.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.LogBack;
import other.Pause;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DeleteMessage_Command extends ListenerAdapter {

    LogBack LB = new LogBack();

    AtomicInteger count = new AtomicInteger();

    private boolean bool_timer = false;
    private boolean bool_is_confirmed = false;

    private TextChannel main_channel = null;

    private static HashMap<Integer, String> list_out = new HashMap<>();
    private static List<String> list_target = new ArrayList<>();
    private static ArrayList<String> list = new ArrayList<>() {{
        add("-c");
        add("-t");
        add("-d");
        add("-r");
        add("-u");
        add("-n");
        add("-s");
    }};

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            String[] argArray = event.getMessage().getContentRaw().split(" ");
            if (!event.getAuthor().isBot()) {

                if (argArray[0].equalsIgnoreCase(">dm")) {
                    if (event.getGuild().getMember(event.getMember().getUser()).isOwner()) {
                        try {
                            if (argArray[1] == null) {
                            } else {
                                if (argArray[1].equals("confirm")) {
                                    if (!bool_timer) {
                                        event.getChannel().sendMessage(event.getMember().getAsMention() + " Aktuell läuft kein Timer!").queue();
                                        return;
                                    } else {
                                        event.getMessage().addReaction("\uD83D\uDC4D").queue();
                                        bool_is_confirmed = true;
                                    }
                                    return;
                                }
                                if (!bool_timer) {
                                    /*check if argArray contains -c AND -t*/
                                    if (Arrays.toString(argArray).contains("-c") && Arrays.toString(argArray).contains("-t")) {
                                        event.getChannel().sendMessage(event.getMember().getAsMention() + " Error: You can´t search in category AND textchannel!").queue();
                                        event.getMessage().addReaction("\u274C").queue();
                                        /*check if argArray contains -c AND -t*/
                                    } else if (Arrays.toString(argArray).contains("-r") && Arrays.toString(argArray).contains("-u")) {
                                        event.getChannel().sendMessage(event.getMember().getAsMention() + " Error: You can´t search in role AND user!").queue();
                                        event.getMessage().addReaction("\u274C").queue();
                                    } else {
                                        list_out.clear();
                                        for (int var = 0; var < argArray.length; var++) {
                                            if (var % 2 == 0) {
                                                if (var > 0) {
                                                    for (int x = 0; x < list.size(); x++) {
                                                        if (argArray[var - 1].equalsIgnoreCase(list.get(x))) {
                                                            /*set first all to null*/
                                                            String category = null;
                                                            String textchannel = null;
                                                            String date = null;
                                                            String role = null;
                                                            String user = null;
                                                            String number = null;
                                                            String string = null;

                                                            /*set all to var*/
                                                            if (x == 0) {
                                                                if (event.getGuild().getCategoryById(argArray[var]) != null) {
                                                                    category = argArray[var];
                                                                } else {
                                                                    event.getChannel().sendMessage(event.getMember().getAsMention() + " Error: The ID is not a category!").queue();
                                                                    return;
                                                                }
                                                            } else if (x == 1) {
                                                                if (event.getGuild().getTextChannelById(argArray[var]) != null) {
                                                                    textchannel = argArray[var];
                                                                } else {
                                                                    event.getChannel().sendMessage(event.getMember().getAsMention() + " Error: The ID is not a textchannel!").queue();
                                                                    return;
                                                                }
                                                            } else if (x == 2) {
                                                                if (argArray[var].length() == 8) {
                                                                    try {
                                                                        int d = Integer.parseInt(argArray[var].substring(0, 2));
                                                                        int m = Integer.parseInt(argArray[var].substring(3, 5));
                                                                        int y = Integer.parseInt(argArray[var].substring(6, 8));
                                                                        if (d > 31 || m > 12) {
                                                                            System.err.println("Error: one of your number is to big");
                                                                            return;
                                                                        }
                                                                    } catch (NumberFormatException e) {
                                                                        System.err.println("Error: input is not a int!");
                                                                        return;
                                                                    }
                                                                } else {
                                                                    System.err.println("Error: Right format is *DD-MM-YY*!");
                                                                    return;
                                                                }
                                                                date = argArray[var];
                                                            } else if (x == 3) {
                                                                if (event.getGuild().getRoleById(argArray[var]) != null) {
                                                                    role = argArray[var];
                                                                } else {
                                                                    event.getChannel().sendMessage(event.getMember().getAsMention() + " Error: The ID is not a role!").queue();
                                                                    return;
                                                                }
                                                            } else if (x == 4) {
                                                                if (event.getGuild().getMemberById(argArray[var]) != null) {
                                                                    user = argArray[var];
                                                                } else {
                                                                    event.getChannel().sendMessage(event.getMember().getAsMention() + " Error: The ID is not a user!").queue();
                                                                    return;
                                                                }
                                                            } else if (x == 5) {
                                                                Integer.parseInt(argArray[var]);
                                                                number = argArray[var];
                                                            } else if (x == 6) {
                                                                string = argArray[var];
                                                            }
                                                            /*put string into the hashmap, with the right position*/
                                                            if (category != null) {
                                                                list_out.put(0, category);
                                                            }
                                                            if (textchannel != null) {
                                                                list_out.put(1, textchannel);
                                                            }
                                                            if (date != null) {
                                                                list_out.put(2, date);
                                                            }
                                                            if (role != null) {
                                                                list_out.put(3, role);
                                                            }
                                                            if (user != null) {
                                                                list_out.put(4, user);
                                                            }
                                                            if (number != null) {
                                                                list_out.put(5, number);
                                                            }
                                                            if (string != null) {
                                                                list_out.put(6, string);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        main_channel = event.getTextChannel();
                                        control(event.getGuild(), list_out, "count");
                                    }
                                } else {
                                    event.getChannel().sendMessage("Es läuft bereits ein Timer!").queue();
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            event.getChannel().sendMessage(new EmbedBuilder().setTitle("Message-Del").setDescription("\n" +
                                    "```(Category)\n-c <category ID> | one category as target```" +
                                    "```(Texchannel)\n-t <textchannel ID> | one textchannel as target```" +
                                    "```(Date)\n-d <date> | (DD-MM-YY) | filter, all messages with this date```" +
                                    "```(Role)\n-r <role ID> | fitler, for one role```" +
                                    "```(User)\n -u <user ID> | filter, for one user```" +
                                    "```(Number)\n-n <int> | filter, within x messages```" +
                                    "```(String)\n-s <string> | filter, if message contain the string```").build()).queue();
                        } catch (NumberFormatException e) {
                            event.getChannel().sendMessage(event.getMember().getAsMention() + " Error: The ID is wrong, or was not found!").queue();
                        }
                    } else {
                        event.getChannel().sendMessage("Dieser Befehl ist nur für den Server Besitzer!").queue();
                        event.getMessage().addReaction("\u274C").queue();
                    }
                }
            }
        }
    }

    /**
     * control if all is right and if this exits on the server
     *
     * @param guild
     * @param string_long
     * @param cmd
     */
    private void control(Guild guild, HashMap<Integer, String> string_long, String cmd) {
        Category category = null;
        TextChannel textChannel = null;
        String date = null;
        Role role = null;
        Member user = null;
        Integer number = null;
        String string = null;

        int int_count = 0;

        if (string_long.get(0) != null) {
            category = guild.getCategoryById(string_long.get(0));
        }
        if (string_long.get(1) != null) {
            textChannel = guild.getTextChannelById(string_long.get(1));
        }
        if (string_long.get(2) != null) {
            date = string_long.get(2);
        }
        if (string_long.get(3) != null) {
            role = guild.getRoleById(string_long.get(3));
        }
        if (string_long.get(4) != null) {
            user = guild.getMemberById(string_long.get(4));
        }
        if (string_long.get(5) != null) {
            number = Integer.parseInt(string_long.get(5));
        }
        if (string_long.get(6) != null) {
            string = string_long.get(6);
        }

        /*ALL textchannel in ONE specific category*/
        if (category != null) {
            for (TextChannel channel : guild.getCategoryById(category.getId()).getTextChannels()) {
                list_target.add(channel.getId());
            }
            /*ONE specific textchannel*/
        } else if (textChannel != null) {
            list_target.add(textChannel.getId());
            /*list_del > list of ALL textchannel´s*/
        } else {
            for (TextChannel channel : guild.getTextChannels()) {
                list_target.add(channel.getId());
            }
        }

        for (String target : list_target) {
            if (cmd.equals("delete")) {
                Pause P = new Pause();
                P.pause(Thread.currentThread(), 5000);
            }

            if (number != null && date != null) {
                for (Message message : guild.getTextChannelById(target).getIterableHistory()) {
                    if (int_count < number) {
                        String d = message.getTimeCreated().getDayOfMonth() + "";
                        String m = message.getTimeCreated().getMonthValue() + "";
                        String m1;
                        if (m.length() == 1) {
                            m1 = "0" + m;
                        } else {
                            m1 = m;
                        }
                        String y = message.getTimeCreated().getYear() + "";
                        String y1 = y.substring(2, 4);
                        String out = d + "-" + m1 + "-" + y1;
                        if (out.equals(date)) {
                            int_count++;
                            if (user != null && role != null) {
                                delete_count_Message(guild, message, role, user.getUser(), string, cmd);
                            } else if (user == null && role != null) {
                                delete_count_Message(guild, message, role, null, string, cmd);
                            } else if (user != null && role == null) {
                                delete_count_Message(guild, message, null, user.getUser(), string, cmd);
                            } else {
                                delete_count_Message(guild, message, null, null, string, cmd);
                            }
                        }
                    }
                }
            } else if (number != null && date == null) {
                for (Message message : guild.getTextChannelById(target).getIterableHistory()) {
                    if (int_count < number) {
                        int_count++;
                        if (user != null && role != null) {
                            delete_count_Message(guild, message, role, user.getUser(), string, cmd);
                        } else if (user == null && role != null) {
                            delete_count_Message(guild, message, role, null, string, cmd);
                        } else if (user != null && role == null) {
                            delete_count_Message(guild, message, null, user.getUser(), string, cmd);
                        } else {
                            delete_count_Message(guild, message, null, null, string, cmd);
                        }
                    }
                }
            } else if (number == null && date != null) {
                for (Message message : guild.getTextChannelById(target).getIterableHistory()) {
                    String d = message.getTimeCreated().getDayOfMonth() + "";
                    String m = message.getTimeCreated().getMonthValue() + "";
                    String m1;
                    if (m.length() == 1) {
                        m1 = "0" + m;
                    } else {
                        m1 = m;
                    }
                    String y = message.getTimeCreated().getYear() + "";
                    String y1 = y.substring(2, 4);
                    String out = d + "-" + m1 + "-" + y1;
                    if (date.equals(out)) {
                        if (user != null && role != null) {
                            delete_count_Message(guild, message, role, user.getUser(), string, cmd);
                        } else if (user == null && role != null) {
                            delete_count_Message(guild, message, role, null, string, cmd);
                        } else if (user != null && role == null) {
                            delete_count_Message(guild, message, null, user.getUser(), string, cmd);
                        } else {
                            delete_count_Message(guild, message, null, null, string, cmd);
                        }
                    }
                }
            } else {
                for (Message message : guild.getTextChannelById(target).getIterableHistory()) {
                    if (user != null && role != null) {
                        delete_count_Message(guild, message, role, user.getUser(), string, cmd);
                    } else if (user == null && role != null) {
                        delete_count_Message(guild, message, role, null, string, cmd);
                    } else if (user != null && role == null) {
                        delete_count_Message(guild, message, null, user.getUser(), string, cmd);
                    } else {
                        delete_count_Message(guild, message, null, null, string, cmd);
                    }
                }
            }
        }
        /**/
        if (cmd.equals("count")) {
            if (count.get() > 0) {
                EmbedBuilder E_builder = new EmbedBuilder();
                if (count.get() == 1) {
                    E_builder.setTitle("Message-del | Soll wirklich **" + count.get() + "** Nachricht gelöscht werden?");
                } else {
                    E_builder.setTitle("Message-del | Sollen wirklich **" + count.get() + "** Nachrichten gelöscht werden?");
                }
                if (!list_target.isEmpty()) {
                    if (list_target.size() == 1) {
                        E_builder.getDescriptionBuilder().append("Textchannel = ");
                    } else {
                        E_builder.getDescriptionBuilder().append("Textchannels = ");
                    }
                    for (String channel : list_target) {
                        E_builder.getDescriptionBuilder().append(guild.getTextChannelById(channel).getName() + ", ");
                    }
                    E_builder.getDescriptionBuilder().append("\n\n");
                }
                if (date != null) {
                    E_builder.getDescriptionBuilder().append("Date = " + date + "\n\n");
                }
                if (role != null) {
                    E_builder.getDescriptionBuilder().append("Role = " + guild.getRoleById(role.getId()).getName() + "\n\n");
                }
                if (user != null) {
                    E_builder.getDescriptionBuilder().append("User = " + guild.getMemberById(user.getId()).getEffectiveName() + "\n\n");
                }
                if (number != null) {
                    E_builder.getDescriptionBuilder().append("Number = " + number + "\n\n");
                }
                if (string != null) {
                    E_builder.getDescriptionBuilder().append("String = " + string + "\n\n");
                }
                E_builder.getDescriptionBuilder().append("Anzahl = " + count);
                main_channel.sendMessage(E_builder.build()).queue(message1 -> timer(message1, 31, guild, string_long, "delete"));
                if (bool_is_confirmed) {
                    control(guild, list_out, "delete");
                }
            }
        } else {
            bool_timer = false;
            bool_is_confirmed = false;
        }
        list_target.clear();
    }

    private void delete_count_Message(Guild guild, Message message, Role role, User user, String string, String cmd) {
        if (user != null) {
            if (user != null && string != null) {
                if (message.getAuthor().getId().equals(user.getId())) {
                    if (message.getContentRaw().contains(string)) {
                        if (cmd.equals("count")) {
                            count.getAndIncrement();
                        } else {
                            message.delete().queue();
                        }
                    }
                }
            } else if (user == null && string != null) {
                if (message.getContentRaw().contains(string)) {
                    if (cmd.equals("count")) {
                        count.getAndIncrement();
                    } else {
                        message.delete().queue();
                    }
                }
            } else if (user != null && string == null) {
                if (message.getAuthor().getId().equals(user.getId())) {
                    if (cmd.equals("count")) {
                        count.getAndIncrement();
                    } else {
                        message.delete().queue();
                    }
                }
            } else {
                if (cmd.equals("count")) {
                    count.getAndIncrement();
                } else {
                    message.delete().queue();
                }
            }
        } else if (role != null) {
            if (role != null && string != null) {
                for (Role M_role : guild.getMemberById(message.getMember().getId()).getRoles()) {
                    if (M_role.equals(role)) {
                        if (message.getContentRaw().contains(string)) {
                            if (cmd.equals("count")) {
                                count.getAndIncrement();
                            } else {
                                message.delete().queue();
                            }
                        }
                    }
                }
            } else if (role == null && string != null) {
                if (message.getContentRaw().contains(string)) {
                    if (cmd.equals("count")) {
                        count.getAndIncrement();
                    } else {
                        message.delete().queue();
                    }
                }
            } else if (role != null && string == null) {
                for (Role M_role : guild.getMemberById(message.getMember().getId()).getRoles()) {
                    if (M_role.equals(role)) {
                        if (cmd.equals("count")) {
                            count.getAndIncrement();
                        } else {
                            message.delete().queue();
                        }
                    }
                }
            } else {
                if (cmd.equals("count")) {
                    count.getAndIncrement();
                } else {
                    message.delete().queue();
                }
            }
        } else {
            if (cmd.equals("count")) {
                count.getAndIncrement();
            } else {
                message.delete().queue();
            }
        }
    }

    private void timer(Message message, int x, Guild guild, HashMap<Integer, String> string_long, String cmd) {
        final int finalX = x;
        Runnable runnable = () -> {
            if (bool_is_confirmed) {
                control(guild, string_long, cmd);
                return;
            }
            if (finalX >= 0) {
                bool_timer = true;
                try {
                    TimeUnit.SECONDS.sleep(1);
                    if (finalX == 10) {
                        for (Message message1 : message.getChannel().getIterableHistory()) {
                            if (message1.getId().equals(message.getId())) {
                                message.addReaction("U+1f51f").queue();
                            }
                        }
                    } else if (finalX <= 9) {
                        for (Message message1 : message.getChannel().getIterableHistory()) {
                            if (message1.getId().equals(message.getId())) {
                                message.addReaction("U+3" + finalX + "U+20e3").queue();
                            }
                            if (finalX == 0) {
                                message.addReaction("\u274C").queue();
                                return;
                            }
                        }
                    }
                    timer(message, finalX - 1, guild, string_long, cmd);
                } catch (InterruptedException e) {
                    LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
                }
            } else {
                bool_timer = false;
            }
        };
        new Thread(runnable).start();
    }
}