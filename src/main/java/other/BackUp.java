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

    private SimpleDateFormat SDF = new SimpleDateFormat("YYYY-MM-dd_HH-mm");
    private Date D = new Date();
    private String date = SDF.format(D);

    private SimpleDateFormat SDF_m = new SimpleDateFormat("MM");
    private Date D_m = new Date();
    private String date_m = SDF_m.format(D_m);

    private static ArrayList<File> list = new ArrayList<>() {{
        add(new File("config/"));
        add(new File("logs/"));
    }};

    public void timer() {
        Runnable runnable = () -> {
            try {
                getFiles();
                OldBackUp();
                TimeUnit.HOURS.sleep(24);
            } catch (Exception e) {
                LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
            }
        };
        new Thread(runnable).start();
    }

    private void getFiles() {
        try {
            if (new File("backup/" + "/" + date + "/").exists()) {
                return;
            }

            for (File file_list : list) {

                File b_backup = new File("backup/");
                boolean bool_backup = b_backup.mkdir();

                File b_date = new File(b_backup + "/" + date + "/");
                boolean bool_date = b_date.mkdir();

                File b_folder = new File(b_date.getPath() + "/" + file_list + "/");
                boolean bool_folder = b_folder.mkdir();

                for (File file : file_list.listFiles()) {
                    if (!file.getName().contains("png")) {
                        if (file.isDirectory()) {
                            File file_out = new File(b_folder + "/" + file.getName());
                            boolean b = file_out.mkdir();
                            if (b) {
                                for (File file1 : file.listFiles()) {
                                    copyFile(file1, "config", file1.getParentFile().getName());
                                }
                            }
                        } else if (file.isFile()) {
                            if (file.getName().contains(".log")) {
                                if (file.getName().contains(date.substring(0, 10))) {
                                    copyFile(file, "logs", null);
                                }
                            } else {
                                copyFile(file, "config", null);
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
        }
    }

    private void copyFile(File file, String MainFolder, String Parent) {
        try {
            File file_out;
            if (Parent != null) {
                file_out = new File("backup/" + date + "/" + MainFolder + "/" + Parent + "/" + file.getName());
            } else {
                file_out = new File("backup/" + date + "/" + MainFolder + "/" + file.getName());
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

    private void OldBackUp() throws Exception {
        File backup = new File("backup/");
        if (backup.listFiles().length > 0) {
            boolean bool_backup = backup.mkdir();

            int backup_count = 7;
            int size = backup.listFiles().length;
            int cal = size - backup_count;

            if (cal < 1) {
            } else {
                if (!file_original.exists()) {
                    file_original.createNewFile();
                }
                if (!file_temp.exists()) {
                    file_temp.createNewFile();
                }
                if (!file_final.exists()) {
                    file_final.createNewFile();
                }

                File[] file = new File("backup/").listFiles();

                ArrayList<File> list = new ArrayList<>();
                assert file != null;
                Collections.addAll(list, file);
                Collections.sort(list);

                for (int x = 0; x < cal; x++) {
                    int month_file = Integer.parseInt(list.get(x).getName().substring(5, 7));
                    /*ZIP*/
                    FileOutputStream fos = new FileOutputStream(file_temp.getName());
                    ZipOutputStream zos = new ZipOutputStream(fos);
                    zos.setMethod(ZipOutputStream.DEFLATED);
                    zos.setLevel(5);

                    ZIP_BackUp(zos, list.get(x), list.get(x).getParentFile().getName(), cal, month_file);

                    zos.flush();
                    fos.flush();
                    zos.close();
                    fos.close();
                    /*DELETE*/
                    deleteFolder(list.get(x));
                }
                boolean a = file_original.delete();
                boolean b = file_temp.delete();
                if (!file_original.exists() && !file_temp.exists()) {
                    boolean c = file_final.renameTo(file_original);
                }
            }
        }
    }

    private void ZIP_BackUp(ZipOutputStream zos, File fileToZip, String parrentDirectoryName, int cal, int month_file) {
        try {
            if (fileToZip == null || !fileToZip.exists()) {
                return;
            }

            String zipEntryName = fileToZip.getName();
            if (parrentDirectoryName != null && !parrentDirectoryName.isEmpty()) {
                if (!parrentDirectoryName.equals("backup")) {
                    zipEntryName = parrentDirectoryName + "/" + fileToZip.getName();
                } else {
                    zipEntryName = month(month_file) + "/" + fileToZip.getName();
                }
            }

            if (fileToZip.isDirectory()) {
                for (File file : fileToZip.listFiles()) {
                    ZIP_BackUp(zos, file, zipEntryName, cal, month_file);
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
                outStream.flush();
                outStream.close();
            }
        } catch (Exception e) {
            LB.log(Thread.currentThread().getName(), e.getMessage(), "error");
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
            inStream.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
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

    static String month(int month_file) {
        if (month_file == 1) {
            return "January";
        } else if (month_file == 2) {
            return "February";
        } else if (month_file == 3) {
            return "March";
        } else if (month_file == 4) {
            return "April";
        } else if (month_file == 5) {
            return "May";
        } else if (month_file == 6) {
            return "June";
        } else if (month_file == 7) {
            return "July";
        } else if (month_file == 8) {
            return "August";
        } else if (month_file == 9) {
            return "September";
        } else if (month_file == 10) {
            return "October";
        } else if (month_file == 11) {
            return "November";
        } else if (month_file == 12) {
            return "December";
        } else {
            return "error";
        }
    }
}