package other;

import config.PropertiesFile;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.HashMap;

public class AuditLog {

    static LogBack LB = new LogBack();

    private final HashMap<String, String> last_ID = new HashMap<>();

    //TODO > invitelink

    public void GetEntry(Guild guild, String TargetID, String type, String name) {
        try {
            ArrayList<String> list = new ArrayList<>();
            String suffix = null;
            String art = null;
            String color = null;

            for (AuditLogEntry entry : guild.retrieveAuditLogs()) {

                if (type.contains("channel")) {
                    suffix = "den";
                } else {
                    suffix = "die";
                }

                if (entry.getType().toString().contains("UPDATE")) {
                    if (entry.getType().toString().contains("MEMBER")) {
                        if (entry.getChanges().toString().contains("add")) {
                            art = "hinzugef\u00f6gt";
                            color = ConsoleColor.backgreen;
                            list.add("*" + name + "* von " + guild.getMemberById(TargetID).getEffectiveName());

                        } else if (entry.getChanges().toString().contains("remove")) {
                            art = "entfernt";
                            color = ConsoleColor.backred;
                            list.add("*" + name + "* von " + guild.getMemberById(TargetID).getEffectiveName());

                        } else if (entry.getChanges().toString().contains("update")) {
                            art = "aktualisiert";
                            color = ConsoleColor.backyellow;
                            list.add("*" + name + "* von " + guild.getMemberById(TargetID).getEffectiveName());
                        }

                    } else {
                        art = "aktualisiert";
                        color = ConsoleColor.backyellow;
                        entry.getChanges().forEach((s, auditLogChange) -> {
                            list.add(s + " >>> '" + auditLogChange.getOldValue() + "' -> '" + auditLogChange.getNewValue() + "'");
                        });
                    }

                } else if (entry.getType().toString().contains("CREATE")) {
                    art = "erstellt";
                    color = ConsoleColor.backgreen;
                    list.add(name);

                } else if (entry.getType().toString().contains("DELETE")) {
                    art = "gel\u00f6scht";
                    color = ConsoleColor.backred;
                    list.add(name);

                } else if (entry.getChanges().toString().contains("update")) {
                    art = "aktualisiert";
                    color = ConsoleColor.backyellow;
                    list.add(name);
                }

                if (entry.getTargetId().equals(TargetID)) {
                    String message = "AuditLog > " + entry.getUser().getName() + " hat " + suffix + " " + type + " '" + name + "' " + art + " | " + list.toString();
                    if (!last_ID.toString().contains(TargetID) || !last_ID.toString().contains(message)) {
                        last_ID.clear();
                        last_ID.put(TargetID, message);
                    } else {
                        return;
                    }
                    LB.log(Thread.currentThread().getName(), ConsoleColor.backblue + "AuditLog" + ConsoleColor.reset + " > " + ConsoleColor.cyan + entry.getUser().getName() + ConsoleColor.Bcyan + " hat " + suffix + " " + type + " *" + name + "* " + color + art + ConsoleColor.reset + " | " + list.toString(), "info");
                    if (!PropertiesFile.readsPropertiesFile("logs", "config").isEmpty()) {
                        String channel = PropertiesFile.readsPropertiesFile("logs", "config");
                        guild.getTextChannelById(channel).sendMessage(message).queue();
                    }
                    return;
                }
            }
        } catch (NullPointerException ignored) {
        }
    }
}