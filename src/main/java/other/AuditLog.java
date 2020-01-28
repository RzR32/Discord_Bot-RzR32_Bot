package other;

import config.PropertiesFile;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.HashMap;

public class AuditLog {

    static LogBack LB = new LogBack();

    private HashMap<String, String> last_ID = new HashMap<>();

    //TODO > invitelink

    public void GetEntry(Guild guild, String TargetID, String type, String name) {
        ArrayList<String> list = new ArrayList<>();
        String suffix;
        String art;
        String color;

        for (AuditLogEntry entry : guild.retrieveAuditLogs()) {

            if (type.contains("channel")) {
                suffix = "den";
            } else {
                suffix = "die";
            }

            if (entry.getType().toString().contains("UPDATE")) {
                if (entry.getType().toString().contains("MEMBER")) {
                    if (entry.getChanges().toString().contains("add")) {
                        art = "hinzugefügt";
                        color = ConsoleColor.green;
                        list.add("*" + name + "* von " + guild.getMemberById(TargetID).getEffectiveName());

                    } else if (entry.getChanges().toString().contains("remove")) {
                        art = "entfernt";
                        color = ConsoleColor.red;
                        list.add("*" + name + "* von " + guild.getMemberById(TargetID).getEffectiveName());

                    } else {
                        System.err.println("Error");
                        return;
                    }

                } else {
                    art = "aktualisiert";
                    color = ConsoleColor.yellow;
                    entry.getChanges().forEach((s, auditLogChange) -> {
                        list.add(s + " >>> '" + auditLogChange.getOldValue() + "' -> '" + auditLogChange.getNewValue() + "'");
                    });
                }

            } else if (entry.getType().toString().contains("CREATE")) {
                art = "erstellt";
                color = ConsoleColor.green;
                list.add(name);

            } else if (entry.getType().toString().contains("DELETE")) {
                art = "gelöscht";
                color = ConsoleColor.red;
                list.add(name);

            } else {
                System.err.println("Error");
                return;
            }

            if (entry.getTargetId().equals(TargetID)) {
                String message = "AuditLog > " + entry.getUser().getName() + " hat " + suffix + " " + type + " '" + name + "' " + art + " | " + list.toString();
                if (!last_ID.toString().contains(TargetID) || !last_ID.toString().contains(message)) {
                    last_ID.clear();
                    last_ID.put(TargetID, message);
                } else {
                    return;
                }
                LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "AuditLog" + ConsoleColor.reset + " > " + ConsoleColor.cyan + entry.getUser().getName() + ConsoleColor.Bblue + " hat " + suffix + " " + ConsoleColor.white + type + " *" + name + "* " + color + art + ConsoleColor.reset + " | " + list.toString(), "info");
                if (!PropertiesFile.readsPropertiesFile("logs").isEmpty()) {
                    String channel = PropertiesFile.readsPropertiesFile("logs");
                    guild.getTextChannelById(channel).sendMessage(message).queue();
                }
                return;
            }
        }
    }
}