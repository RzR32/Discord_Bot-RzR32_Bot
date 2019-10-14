package other;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BackUp {

    LogBack LB = new LogBack();

    private SimpleDateFormat SDF = new SimpleDateFormat("HH.mm.ss_dd.MM.yy");
    private Date D = new Date();
    private String date = SDF.format(D);
    private File backup = new File("backup/");
    private File output = new File("backup/" + date + "/");
    private File input = new File("config/");

    public void timer() {
        Runnable runnable = () -> {
            try {
                checkoldbackup();
                getFiles();
                TimeUnit.HOURS.sleep(24);
            } catch (Exception e) {
                LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
            }
        };
        new Thread(runnable).start();
    }

    private void getFiles() {
        boolean bool_backup = backup.mkdir();
        boolean bool_output = output.mkdir();
        if (bool_output) {
            LB.log(Thread.currentThread().getName(), "Create Backup *" + output + "*", "info");
            for (File file : input.listFiles()) {
                if (!file.getName().contains("png")) {
                    if (file.isDirectory()) {
                        File file_out = new File("backup/" + date + "/" + file.getName());
                        boolean b = file_out.mkdir();
                        if (b) {
                            for (File file1 : file.listFiles()) {
                                copyFile(file1, file1.getParentFile().getName());
                            }
                        }
                    } else if (file.isFile()) {
                        copyFile(file, null);
                    }
                }
            }
        }
    }

    private void copyFile(File file, String Parent) {
        try {
            File file_out;
            if (Parent != null) {
                file_out = new File("backup/" + date + "/" + Parent + "/" + file.getName());
            } else {
                file_out = new File("backup/" + date + "/" + file.getName());
            }
            boolean b = file_out.createNewFile();
            if (b) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file_out));
                List<String> lines = Files.readAllLines(file.toPath());

                for (String line : lines) {
                    writer.write(line + "\n");
                    writer.flush();
                }
                writer.close();
            }
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }

    private void checkoldbackup() {
        boolean bool_backup = backup.mkdir();
        for (File _file : new File("backup/").listFiles()) {
            int x = new File("backup/").listFiles().length;
            if (x >= 7) {
                deleteFolder(_file);
            }
        }
    }

    private static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }
}