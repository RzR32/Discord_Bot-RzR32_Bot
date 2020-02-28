package listener.guild;

import config.PropertiesFile;
import count.Counter;
import net.dv8tion.jda.api.events.channel.voice.GenericVoiceChannelEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.AuditLog;

public class _VoiceChannel extends ListenerAdapter {

    AuditLog AL = new AuditLog();
    Counter c = new Counter();

    @Override
    public void onVoiceChannelCreate(VoiceChannelCreateEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            c.getint(event.getGuild(), "voicechannelcount");
        }
    }

    @Override
    public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            c.getint(event.getGuild(), "voicechannelcount");
        }
    }

    @Override
    public void onGenericVoiceChannel(GenericVoiceChannelEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            AL.GetEntry(event.getGuild(), event.getChannel().getId(), "Voicechannel", event.getChannel().getName());
        }
    }
}