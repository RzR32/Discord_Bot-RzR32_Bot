package listener.guild;

import config.PropertiesFile;
import count._main_Counter;
import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import other._guild.AuditLog;

public class _TextChannel extends ListenerAdapter {

    AuditLog AL = new AuditLog();
    _main_Counter c = new _main_Counter();

    @Override
    public void onTextChannelCreate(@NotNull TextChannelCreateEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            c.getint(event.getGuild(), "textchannelcount");
        }
    }

    @Override
    public void onTextChannelDelete(@NotNull TextChannelDeleteEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            if (event.getChannel().getId().equals(PropertiesFile.readsPropertiesFile("bot-zustimmung", "config"))) {
                PropertiesFile.writePropertiesFile("agreement", "", "config");
            }
            c.getint(event.getGuild(), "textchannelcount");
        }
    }

    @Override
    public void onGenericTextChannel(@NotNull GenericTextChannelEvent event) {
        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            AL.GetEntry(event.getGuild(), event.getChannel().getId(), "Textchannel", event.getChannel().getName());
        }
    }
}