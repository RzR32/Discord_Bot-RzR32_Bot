package listener.guild;

import config.PropertiesFile;
import count._main_Counter;
import net.dv8tion.jda.api.events.channel.voice.GenericVoiceChannelEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import other._guild.AuditLog;

public class _VoiceChannel extends ListenerAdapter {

    AuditLog AL = new AuditLog();
    _main_Counter c = new _main_Counter();

    @Override
    public void onVoiceChannelCreate(@NotNull VoiceChannelCreateEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            c.getint(event.getGuild(), "voicechannelcount");
        }
    }

    @Override
    public void onVoiceChannelDelete(@NotNull VoiceChannelDeleteEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            c.getint(event.getGuild(), "voicechannelcount");
        }
    }

    @Override
    public void onGenericVoiceChannel(@NotNull GenericVoiceChannelEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            AL.GetEntry(event.getGuild(), event.getChannel().getId(), "Voicechannel", event.getChannel().getName());
        }
    }
}