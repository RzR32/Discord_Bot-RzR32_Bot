package other;

public class ConsoleColor {

    /*Color for Console (output)                                */
    /*                                                          */
    public static final String reset = "\u001b[0m";             //
    public static final String bold = "\u001b[1m";              //
    public static final String underline = "\u001b[4m";         //  not in use
    public static final String reversed = "\u001b[7m";          //  not in use
    /*normal text color                                         */
    public static final String black = "\u001b[30m";            //  text in the GamePlayingCount and other Counter
    public static final String red = "\u001b[31m";              //  status - DnD    | and use in some method so it looks like LogBack
    public static final String green = "\u001b[32m";            //  status - online | and use in some method so it looks like LogBack
    public static final String yellow = "\u001b[33m";           //  status - idle   | and use in some method so it looks like LogBack
    public static final String blue = "\u001b[34m";             //  not in use
    public static final String magenta = "\u001b[35m";          //  not in use
    public static final String cyan = "\u001b[36m";             //  member
    public static final String white = "\u001b[37m";            //  activity | old status
    /*the "B" means Bright                                      */
    public static final String Bblack = "\u001b[30;1m";         //  member status - offline
    public static final String Bred = "\u001b[31;1m";           //  member, removed from list   | left guild
    public static final String Bgreen = "\u001b[32;1m";         //  member, added to list       | join guild
    public static final String Byellow = "\u001b[33;1m";        //  counter text color
    public static final String Bblue = "\u001b[34;1m";          //  text for activity
    public static final String Bmagenta = "\u001b[35;1m";       //  text for status
    public static final String Bcyan = "\u001b[36;1m";          //  not in use
    public static final String Bwhite = "\u001b[37;1m";         //  text at the start
    /*the "back" means background                               */
    public static final String backblack = "\u001b[40m";        //  not in use
    public static final String backred = "\u001b[41m";          //  delete AuditLog
    public static final String backgreen = "\u001b[42m";        //  create AuditLog
    public static final String backyellow = "\u001b[43m";       //  update AuditLog
    public static final String backblue = "\u001b[44m";         //  text in the front (caps)
    public static final String backmagenta = "\u001b[45m";      //  not in use
    public static final String backcyan = "\u001b[46m";         //  not in use
    public static final String backwhite = "\u001b[47m";        //  not in use
    /*self explaining?                                          */
    public static final String backBblack = "\u001b[40;1m";     //  not in use
    public static final String backBred = "\u001b[41;1m";       //  changed Counter
    public static final String backBgreen = "\u001b[42;1m";     //  NOT changed Counter
    public static final String backByellow = "\u001b[43;1m";    //  GamePlayingCount
    public static final String backBblue = "\u001b[44;1m";      //  not in use
    public static final String backBmagenta = "\u001b[45;1m";   //  dis- / connection
    public static final String backBcyan = "\u001b[46;1m";      //  not in use
    public static final String backBwhite = "\u001b[47;1m";     //  not in use
}