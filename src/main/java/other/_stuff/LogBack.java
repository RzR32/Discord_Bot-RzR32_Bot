package other._stuff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LogBack {

    public void log(String _class, String msg, String level) {
        Logger logger = LoggerFactory.getLogger(_class);
        switch (level) {
            case "trace":
                logger.trace(msg);
                break;
            case "debug":
                logger.debug(msg);
                break;
            case "info":
                logger.info(msg);
                break;
            case "warn":
                logger.warn(msg);
                break;
            case "error":
                logger.error(msg);
                break;
        }
        removeANSICodefromLOG();
    }

    private void removeANSICodefromLOG() {
        File folder = new File("logs/");
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File var_file : listOfFiles) {
            if (var_file.isFile()) {
                File final_file = var_file.getAbsoluteFile();
                try {
                    List<String> lines = Files.readAllLines(Paths.get(final_file.getAbsolutePath()));
                    FileWriter writer = new FileWriter(final_file.getAbsolutePath(), false);
                    for (String string : lines) {
                        writer.write(string.replaceAll("\u001B\\[[;\\d]*m", "") + "\n");
                    }
                    writer.close();
                } catch (IOException e) {
                    if (e.getMessage().equals("Input length = 1")) {
                        return;
                    } else {
                        log(Thread.currentThread().getName(), e.getMessage(), "error");
                    }
                }
            }
        }
    }
}