package other._stuff;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BotSettings {

    private static final File file = new File("config/config.prop");
    private static final LogBack LB = new LogBack();

    /*send new message*/
    public static void send_list(TextChannel channel) {
        try {
            StringBuilder new_message = new StringBuilder("----------\nSettings\n```diff\n");
            List<String> lines = Files.readAllLines(Paths.get(String.valueOf(file)), StandardCharsets.UTF_8);
            for (String string : lines) {
                if (string.startsWith(">")) {
                    if (string.contains("false")) {
                        new_message.append("- ").append(string).append("\n");
                    } else {
                        new_message.append("+ ").append(string).append("\n");
                    }
                    send_key(channel, string);
                }
            }
            new_message.append("```");
            channel.sendMessage(new_message).queue();
        } catch (ErrorResponseException ignored) {
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }

    /*if message found update it, otherwise send new*/
    public static void check_list(TextChannel channel) {
        try {
            StringBuilder new_message = new StringBuilder("----------\nSettings\n```diff\n");
            List<String> lines = Files.readAllLines(Paths.get(String.valueOf(file)), StandardCharsets.UTF_8);
            for (String string : lines) {
                if (string.startsWith(">")) {
                    if (string.contains("false")) {
                        new_message.append("- ").append(string).append("\n");
                    } else {
                        new_message.append("+ ").append(string).append("\n");
                    }
                }
            }
            new_message.append("```");

            /*if message already send - get message*/
            for (Message message : channel.getIterableHistory()) {
                if (message.getContentRaw().contains("----------\nSettings")) {
                    message.editMessage(new_message).queue();
                    return;
                }
            }
            /*other wise send new*/
            channel.sendMessage(new_message).queue();
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }

    /*send the config key - with reaction*/
    private static void send_key(TextChannel channel, String key) {
        key = key.substring(1);
        if (key.contains("false")) {
            key = key.substring(0, key.indexOf("false") - 4);
        } else {
            key = key.substring(0, key.indexOf("true") - 4);
        }
        channel.sendMessage(key).queue(message -> message.addReaction("U+1f44e").queue());
    }
}