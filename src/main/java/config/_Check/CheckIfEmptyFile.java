package config._Check;

import other._stuff.LogBack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CheckIfEmptyFile {

    private static final LogBack LB = new LogBack();

    public static void check_if_file_is_empty(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            if (br.readLine() == null) {
                file.delete();
            }
            br.close();
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }
}