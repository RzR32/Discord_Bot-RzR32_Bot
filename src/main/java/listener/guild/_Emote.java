package listener.guild;

import config.PropertiesFile;
import count._main_Counter;
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent;
import net.dv8tion.jda.api.events.emote.update.GenericEmoteUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import other._guild.AuditLog;

public class _Emote extends ListenerAdapter {

    AuditLog AL = new AuditLog();
    _main_Counter c = new _main_Counter();

    @Override
    public void onEmoteAdded(@NotNull EmoteAddedEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            c.getint(event.getGuild(), "emotecount");
        }
    }

    @Override
    public void onEmoteRemoved(@NotNull EmoteRemovedEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            c.getint(event.getGuild(), "emotecount");
        }
    }

    @Override
    public void onGenericEmoteUpdate(@NotNull GenericEmoteUpdateEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            AL.GetEntry(event.getGuild(), event.getEmote().getId(), "Voicechannel", event.getEmote().getName());
        }
    }
}