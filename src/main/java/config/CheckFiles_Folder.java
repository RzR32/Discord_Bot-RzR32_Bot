package config;

import other.LogBack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CheckFiles_Folder {

    LogBack LB = new LogBack();

    /*
    check files
    >   games.txt
    >   list.txt
    >   members.txt
     */
    public void checkingFiles() {
        boolean bool = false;
        ArrayList<File> files = new ArrayList<>();
        /*
        files
         */
        files.add(new File("config/games.txt"));
        files.add(new File("config/list.txt"));
        files.add(new File("config/members.txt"));
        ArrayList<File> folder = new ArrayList<>();
        /*
        folder
         */
        folder.add(new File("config/blacklist/"));

        /*
        check files
         */
        for (File file : files) {
            if (!file.exists()) {
                try {
                    bool = file.createNewFile();
                    if (bool = true) {
                        LB.log(Thread.currentThread().getName(), "File *" + file + "* was created!", "info");
                    }
                } catch (IOException error) {
                    LB.log(Thread.currentThread().getName(), "Error: " + file, "error");
                }
            }
        }

        /*
        check Folder
         */
        for (File file : folder) {
            if (!file.exists()) {
                bool = file.mkdir();
                if (bool = true) {
                    LB.log(Thread.currentThread().getName(), "Folder *" + file + "* was created!", "info");
                }
            }
        }
    }
}