package listener.commands.owner;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class _main_owner extends ListenerAdapter {

    /**
     * Listener for Commands
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            if (!event.getAuthor().isBot()) {
                String[] argArray = event.getMessage().getContentRaw().split(" ");

                /*help for owner*/
                if (argArray[0].equalsIgnoreCase(">help+")) {
                    if (check_owner(event.getGuild(), event.getTextChannel(), event.getMessage(), Objects.requireNonNull(event.getMember()))) {
                        help.command(event.getTextChannel());
                    }

                    /*set the twitchname*/
                } else if (argArray[0].equalsIgnoreCase(">twitch")) {
                    if (check_owner(event.getGuild(), event.getTextChannel(), event.getMessage(), Objects.requireNonNull(event.getMember()))) {
                        twitchname.command(event.getTextChannel(), argArray);
                    }

                    /*recall the playingcount*/
                } else if (argArray[0].equalsIgnoreCase(">playingcount") || argArray[0].equalsIgnoreCase(">pc")) {
                    if (check_owner(event.getGuild(), event.getTextChannel(), event.getMessage(), Objects.requireNonNull(event.getMember()))) {
                        playingcount.command(event.getGuild());
                    }

                    /*remove everything from one "game"*/
                } else if (argArray[0].equalsIgnoreCase(">del")) {
                    if (check_owner(event.getGuild(), event.getTextChannel(), event.getMessage(), Objects.requireNonNull(event.getMember()))) {
                        del.command(event.getGuild(), event.getTextChannel(), event.getMessage(), argArray);
                    }

                    /*delete the bot - need a second confirm (!)*/
                } else if (argArray[0].equalsIgnoreCase(">delete-bot")) {
                    if (check_owner(event.getGuild(), event.getTextChannel(), event.getMessage(), Objects.requireNonNull(event.getMember()))) {
                        delete_bot.command(event.getGuild(), event.getMessage());
                    }

                    /*start the bot - after the first start*/
                } else if (argArray[0].equalsIgnoreCase(">start")) {
                    if (check_owner(event.getGuild(), event.getTextChannel(), event.getMessage(), Objects.requireNonNull(event.getMember()))) {
                        start.command(event.getGuild(), event.getMessage());
                    }

                    /*command for CheckGameOnWebsite to check game on website´s manually*/
                } else if (argArray[0].equalsIgnoreCase(">checkweb")) {
                    if (check_owner(event.getGuild(), event.getTextChannel(), event.getMessage(), Objects.requireNonNull(event.getMember()))) {
                        checkweb.command(event.getTextChannel(), argArray);
                    }

                    /*edit embed message*/
                } else if (argArray[0].equalsIgnoreCase(">edit")) {
                    if (check_owner(event.getGuild(), event.getTextChannel(), event.getMessage(), Objects.requireNonNull(event.getMember()))) {
                        edit.command(event.getGuild(), argArray);
                    }

                    /*check game*/
                } else if (argArray[0].equalsIgnoreCase(">checkgame")) {
                    if (check_owner(event.getGuild(), event.getTextChannel(), event.getMessage(), Objects.requireNonNull(event.getMember()))) {
                        checkgame.command(event.getGuild(), argArray);
                    }

                    /*Blacklist for Guild*/
                } else if (argArray[0].equalsIgnoreCase(">blacklist+") || argArray[0].equalsIgnoreCase(">bl+")) {
                    if (check_owner(event.getGuild(), event.getTextChannel(), event.getMessage(), Objects.requireNonNull(event.getMember()))) {
                        blacklist.command(event.getGuild(), event.getTextChannel(), event.getMember(), event.getMessage(), argArray);
                    }

                    /*delete Message with Filter*/
                } else if (argArray[0].equalsIgnoreCase(">delete-message") || argArray[0].equalsIgnoreCase(">dm")) {
                    if (check_owner(event.getGuild(), event.getTextChannel(), event.getMessage(), Objects.requireNonNull(event.getMember()))) {
                        delete_message.command(event.getGuild(), event.getTextChannel(), event.getMember(), event.getMessage(), argArray);
                    }

                    /*renew agreement message*/
                } else if (argArray[0].equalsIgnoreCase(">agreement")) {
                    if (check_owner(event.getGuild(), event.getTextChannel(), event.getMessage(), Objects.requireNonNull(event.getMember()))) {
                        agreement.command(event.getGuild(), event.getTextChannel(), event.getMessage());
                    }

                    /*move all user in one channel to another*/
                } else if (argArray[0].equalsIgnoreCase(">moveall")) {
                    if (check_owner(event.getGuild(), event.getTextChannel(), event.getMessage(), Objects.requireNonNull(event.getMember()))) {
                        moveall.command(event.getGuild(), event.getTextChannel(), Objects.requireNonNull(event.getMember()), argArray);
                    }
                }
            }
        }
    }

    /**
     * check, if the member is the server owner
     * if yes > make the code
     * if no > answer
     *
     * @param guild   current Server
     * @param channel current TextChannel
     * @param message current Message (for Reaction)
     * @param member  current Member
     * @return true or false
     */
    private boolean check_owner(Guild guild, TextChannel channel, Message message, Member member) {
        boolean owner = false;
        try {
            if (Objects.requireNonNull(guild.getMember(member.getUser())).isOwner()) {
                owner = true;
                message.addReaction("\uD83D\uDC4D").queue();
            } else {
                channel.sendMessage("Dieser Befehl ist nur f\u00fcr den Server Besitzer!").queue();
                message.addReaction("\u274C").queue();
            }
        } catch (ErrorResponseException ignored) {
        }
        return owner;
    }
}