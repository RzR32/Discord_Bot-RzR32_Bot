package listener.other;

import check_create.CheckCategory;
import check_create.CheckChannel;
import config.CheckFiles_Folder;
import config.PropertiesFile;
import count.Counter;
import count.GamePlayingCount;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import other.ConsoleColor;
import other.LogBack;
import other.Pause;

public class Guild_Ready extends ListenerAdapter {

    LogBack LB = new LogBack();
    Pause P = new Pause();

    private int pause_int = 2500;

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + " > Verbunden!" + ConsoleColor.reset, "info");
        /*
        check files/folder
         */
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "Checking for missing files/folder..." + ConsoleColor.reset, "info");
        CheckFiles_Folder C_Files = new CheckFiles_Folder();
        C_Files.checkingFiles();
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "file check, done!" + ConsoleColor.reset, "info");
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + ConsoleColor.reset, "info");

        /*
         * check *StartUp* key in files...
         */
        if (PropertiesFile.readsPropertiesFile("first-startup").equals("true")) {
            for (Member member : event.getGuild().getMembers()) {
                if (member.isOwner()) {
                    for (TextChannel textChannel : event.getGuild().getTextChannels()) {
                        if (textChannel.getName().equals(member.getId())) {
                            for (Message message : textChannel.getIterableHistory()) {
                                if (message.getContentRaw().startsWith("Hey " + member.getAsMention() + ", here I help you to setup me :D")) {
                                    message.delete().queue();
                                }
                            }
                            Message(textChannel, member);
                            return;
                        }
                    }
                    event.getGuild().createTextChannel(member.getId()).queue(textChannel -> {
                        textChannel.createPermissionOverride(event.getGuild().getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                        textChannel.createPermissionOverride(event.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE).queue();
                        Message(textChannel, member);
                    });
                }
            }
            return;
        }

        /*
        check the category
         */
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "Checking for Category´s..." + ConsoleColor.reset, "info");
        CheckCategory C_Category = new CheckCategory();
        C_Category.StartChecking(event.getGuild());
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "category check, done!" + ConsoleColor.reset, "info");
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + ConsoleColor.reset, "info");

        P.pause(Thread.currentThread(), pause_int);

        /*
        check for counter channel
         */
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "Checking for counter Channel´s..." + ConsoleColor.reset, "info");
        Counter c = new Counter();
        c.StartCounter(event.getGuild());
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "counter Channel check, done!!" + ConsoleColor.reset, "info");
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + ConsoleColor.reset, "info");

        P.pause(Thread.currentThread(), pause_int);

        /*
        check the other channel
         */
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "Checking for other Channel´s..." + ConsoleColor.reset, "info");
        CheckChannel C_Channel = new CheckChannel();
        C_Channel.StartChecking(event.getGuild());
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "other Channel check, done!" + ConsoleColor.reset, "info");
        LB.log(Thread.currentThread().getName(), ConsoleColor.Bwhite + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + ConsoleColor.reset, "info");

        P.pause(Thread.currentThread(), pause_int);

        /*
        GamePlayingCount
         */
        GamePlayingCount GPC = new GamePlayingCount();
        GPC.startCounter(event.getGuild());
        LB.log(Thread.currentThread().getName(), ConsoleColor.backBmagenta + " > Bot gestartet!" + ConsoleColor.reset, "info");
    }

    private void Message(TextChannel textChannel, Member member) {
        textChannel.sendMessage("Hey " + member.getAsMention() + ", here I help you to setup me :D\n\n" +
                "By the way, nobody can see what you do here (private channel) :wink:\n\n" +
                "So let's go, first write __>settings list__, to see what you can disable.\n\n" +
                "This will give you a list of what you can set, for example if you want to disable the *logs*, then do **>settings set >logs_on false**\n\n" +
                "If you set the Twitch settings to true, you can change the name with **>twitch <name>**\n\n" +
                "If you are ready, or do not want to get this message again, write **>ready ** (this channel will be delete then!) > After this, you must start the bot again!").queue();
    }
}