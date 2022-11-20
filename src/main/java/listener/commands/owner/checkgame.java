package listener.commands.owner;

import listener.member._Activity;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.RichPresence;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class checkgame {

    public static void command(Guild guild, String[] argArray) {
        if (argArray[1] != null) {
            /*
            compine all arguments
            */
            ArrayList<String> list = new ArrayList<>();
            for (String s : argArray) {
                list.add(s);
                if (list.size() == argArray.length) {
                    //remove command thing
                    list.remove(argArray[0]);
                    if (list.isEmpty()) {
                        return;
                    }
                }
            }
            String game_name = String.join(" ", list);

            _Activity GfM = new _Activity();
            Activity act = new Activity() {
                @Override
                public boolean isRich() {
                    return false;
                }

                @Nullable
                @Override
                public RichPresence asRichPresence() {
                    return null;
                }

                @Nonnull
                @Override
                public String getName() {
                    return game_name;
                }

                @Nullable
                @Override
                public String getUrl() {
                    return null;
                }

                @Nonnull
                @Override
                public ActivityType getType() {
                    return ActivityType.DEFAULT;
                }

                @Nullable
                @Override
                public Timestamps getTimestamps() {
                    return null;
                }

                @Nullable
                @Override
                public Emoji getEmoji() {
                    return null;
                }
            };
            GfM.EditMessagesFromGames(guild, act);
        }
    }
}