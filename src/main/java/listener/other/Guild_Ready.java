package listener.other;

import config.PropertiesFile;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import other._stuff.BotSettings;
import other._stuff.ConsoleColor;
import other._stuff.LogBack;
import other._stuff.starting_all;

public class Guild_Ready extends ListenerAdapter {

    private static final LogBack LB = new LogBack();

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {


        if (PropertiesFile.readsPropertiesFile("first_startup", "config").equals("false")) {
            starting_all sa = new starting_all();
            sa.make_start(event.getGuild());
        } else {
            LB.log(Thread.currentThread().getName(), ConsoleColor.backBred + "Server: *" + event.getGuild().getId() + "*  | *" + event.getGuild().getName() + "* setup!" + ConsoleColor.reset, "warn");
        }
        CreateSetupChannel(event.getGuild());
    }

    private void CreateSetupChannel(Guild guild) {
        boolean found_message = false;
        boolean found_list = false;

        for (Member member : guild.getMembers()) {
            if (member.isOwner()) {
                for (TextChannel textChannel : guild.getTextChannels()) {
                    if (textChannel.getName().equals(member.getId())) {
                        /*check if the message gets found*/
                        for (Message message : textChannel.getIterableHistory()) {
                            if (message.getContentRaw().startsWith("Hey " + member.getAsMention() + ", here I help you to setup me :D")) {
                                found_message = true;
                            }
                            if (message.getContentRaw().startsWith("Settings")) {
                                found_list = true;
                            }
                        }
                        if (!found_message) {
                            Message(textChannel, member);
                        }
                        if (!found_list) {
                            BotSettings.check_list(textChannel);
                        }
                        return;
                    }
                }
                guild.createTextChannel(member.getId()).queue(textChannel -> {
                    textChannel.createPermissionOverride(guild.getSelfMember()).setAllow(Permission.VIEW_CHANNEL).queue();
                    textChannel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                    Message(textChannel, member);
                    BotSettings.send_list(textChannel);
                });
            }
        }
    }

    private void Message(TextChannel textChannel, Member member) {
        textChannel.sendMessage("Hey " + member.getAsMention() + ", here I help you to setup me :D\n" +
                "By the way, nobody can see what you do here (private channel) :wink:\n" +
                "So let's go, add the Reaction to **deactivate** the function.\n" +
                "If you set the Twitch settings to true, you can change the name with **>twitch <name>**\n" +
                "If is your first start and your settings not ready, write **>start** - to finally start the bot!\n" +
                "(Then all channel´s (thats activated) will be created)").queue();
    }
}