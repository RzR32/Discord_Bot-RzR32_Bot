package other;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class BackUp {

    LogBack LB = new LogBack();

    private File file_original = new File("backup.zip");
    private File file_temp = new File("backup_temp.zip");
    private File file_final = new File("backup_final.zip");

    private SimpleDateFormat SDF = new SimpleDateFormat("YYYY-MM-DD_HH-mm");
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
            LB.log(Thread.currentThread().getName(), "Create Backup *" + output.getAbsoluteFile().getName() + "*", "info");
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

    private void checkoldbackup() throws Exception {
        if (!file_original.exists()) {
            file_original.createNewFile();
        }
        if (!file_temp.exists()) {
            file_temp.createNewFile();
        }
        if (!file_final.exists()) {
            file_final.createNewFile();
        }

        boolean bool_backup = backup.mkdir();

        File[] file = new File("backup/").listFiles();

        ArrayList<File> list = new ArrayList<>();
        assert file != null;
        Collections.addAll(list, file);
        Collections.sort(list);

        FileOutputStream fos = new FileOutputStream(file_temp.getName());
        ZipOutputStream zos = new ZipOutputStream(fos);
        zos.setMethod(ZipOutputStream.DEFLATED);
        zos.setLevel(5);

        int backup_count = 7;
        int size = list.size();
        int cal = size - backup_count + 1;

        for (int x = 0; x < cal; x++) {
            /*ZIP*/
            Zip(zos, list.get(x), list.get(x).getParentFile().getName(), cal);
            LB.log(Thread.currentThread().getName(), "ZIP Backup *" + list.get(x).getAbsoluteFile().getName() + "* to Backup.zip", "info");
            /*DELETE*/
            deleteFolder(list.get(x));
            LB.log(Thread.currentThread().getName(), "", "info");
        }
        boolean a = file_temp.delete();
        boolean b = file_original.delete();

        if (!file_original.exists() && !file_temp.exists()) {
            boolean c = file_final.renameTo(file_original);
        }
        zos.flush();
        fos.flush();
        zos.close();
        fos.close();
    }

    private void Zip(ZipOutputStream zos, File fileToZip, String parrentDirectoryName, int cal) {
        try {
            if (fileToZip == null || !fileToZip.exists()) {
                return;
            }

            String zipEntryName = fileToZip.getName();
            if (parrentDirectoryName != null && !parrentDirectoryName.isEmpty()) {
                if (!parrentDirectoryName.equals("backup")) {
                    zipEntryName = parrentDirectoryName + "/" + fileToZip.getName();
                }
            }

            if (fileToZip.isDirectory()) {
                for (File file : fileToZip.listFiles()) {
                    Zip(zos, file, zipEntryName, cal);
                }
            } else {
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(fileToZip);
                zos.putNextEntry(new ZipEntry(zipEntryName));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }

            if (cal > 0) {
                ZipOutputStream outStream = new ZipOutputStream(new FileOutputStream(file_final));
                readZip(outStream, file_original.getName());
                readZip(outStream, file_temp.getName());
                outStream.close();
            }
        } catch (IOException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void readZip(ZipOutputStream outStream, String targetFile) {
        try {
            ZipInputStream inStream = new ZipInputStream(new FileInputStream(targetFile));

            byte[] buffer = new byte[1024];
            int len;

            for (ZipEntry e; (e = inStream.getNextEntry()) != null; ) {
                outStream.putNextEntry(e);
                while ((len = inStream.read(buffer)) > 0) {
                    outStream.write(buffer, 0, len);
                }
            }
            inStream.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteFolder(File folder) {
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