package config;

import java.io.*;
import java.util.Properties;

public class PropertiesFile {

    /*
     * Copyright (c) 2019 K-EY
     */

    private static File file = new File("config/config.prop");

    /**
     * Writes Properties in a .prop(erties) file
     *
     * @param key   category
     * @param value value for category
     */
    public static void writePropertiesFile(String key, String value) {
        /**
         * init Properties and Output stream
         */
        Properties properties = new Properties();
        OutputStream outputStream = null;
        /**
         * load already existing properties
         */
        try {
            checkFile();
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * set output- directory and file name
         */
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        /**
         * set first properties to enter in file
         * like :
         * var1=example
         */
        properties.setProperty(key, value);
        /**
         * saving properties file and content
         */
        try {
            properties.store(outputStream, null);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    /**
     * Checks if File exists
     *
     * @return true if exist
     */
    private static boolean checkFile() {
        try {
            new File("config/").mkdir();
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Error creating " + file.toString());
        }
        return file.isFile() && file.canWrite() && file.canRead();
    }
    /**
     * reads values from .prop(erties) file
     *
     * @param key category for value
     * @return returns value
     */
    public static String readsPropertiesFile(String key) {
        Properties prop = new Properties();
        InputStream input = null;
        checkFile();
        try {
            input = new FileInputStream(file);
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            prop.getProperty(key);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop.getProperty(key);
    }
}