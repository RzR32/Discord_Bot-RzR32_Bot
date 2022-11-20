package listener.commands.member;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import other._stuff.LogBack;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class gamelist {

    private static final LogBack LB = new LogBack();

    public static void command(TextChannel channel, Member member) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("config/list.txt"), StandardCharsets.UTF_8);
            if (!lines.contains(member.getUser().getId())) {
                channel.sendMessage(member.getAsMention() + " du bist nicht in der Liste").queue();
            } else if (lines.contains(member.getUser().getId())) {
                channel.sendMessage(member.getAsMention() + " du bist in der Liste").queue();
            }
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }
}