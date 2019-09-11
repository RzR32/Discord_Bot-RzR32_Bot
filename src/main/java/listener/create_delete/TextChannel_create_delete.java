package listener.create_delete;

import config.PropertiesFile;
import count.Counter;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TextChannel_create_delete extends ListenerAdapter {

    Counter c = new Counter();

    @Override
    public void onTextChannelCreate(TextChannelCreateEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            c.getint(event.getGuild(), "textchannelcount");
        }
    }

    @Override
    public void onTextChannelDelete(TextChannelDeleteEvent event) {
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("false")) {
            if (event.getChannel().getId().equals(PropertiesFile.readsPropertiesFile("bot-zustimmung"))) {
                PropertiesFile.writePropertiesFile("agreement", "");
            }
            c.getint(event.getGuild(), "textchannelcount");
        }
    }
}