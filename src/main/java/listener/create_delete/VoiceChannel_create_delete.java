package listener.create_delete;

import count.Counter;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VoiceChannel_create_delete extends ListenerAdapter {

    Counter c = new Counter();

    @Override
    public void onVoiceChannelCreate(VoiceChannelCreateEvent event) {
        c.getint(event.getGuild(), "voicechannelcount");
    }

    @Override
    public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
        c.getint(event.getGuild(), "voicechannelcount");
    }
}
