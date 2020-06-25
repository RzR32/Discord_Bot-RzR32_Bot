package listener.commands.owner;

import count.GamePlayingCount._main_GamePlayingCount;
import net.dv8tion.jda.api.entities.Guild;

public class playingcount {

    public static void command(Guild guild) {
        _main_GamePlayingCount GPC = new _main_GamePlayingCount();
        GPC.startCounter(guild);
    }
}