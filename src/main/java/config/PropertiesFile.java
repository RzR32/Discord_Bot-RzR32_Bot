package config;

import config._Check.CheckKey;

import java.io.*;
import java.util.Properties;

public class PropertiesFile {

    /*
     * Copyright (c) 2019 K-EY
     */

    private static final File file_path = new File("config");

    /**
     * Writes Properties in a .prop(erties) file
     *
     * @param key   category
     * @param value value for category
     */
    public static void writePropertiesFile(String key, String value, String file) {
        File file_new = new File(file_path + "/" + file + ".prop");
        /**
         * init Properties and Output stream
         */
        Properties properties = new Properties();
        OutputStream outputStream = null;
        /**
         * load already existing properties
         */
        try {
            checkFile(file_new);
            properties.load(new FileInputStream(file_new));
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * set output- directory and file name
         */
        try {
            outputStream = new FileOutputStream(file_new);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * close output
         */
        try {
            assert outputStream != null;
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * RzR32 - start key sorter
         */
        CheckKey CK = new CheckKey();
        CK.SortKey();
    }

    /**
     * Checks if File exists
     *
     * @return true if exist
     */
    private static boolean checkFile(File file) {
        try {
            new File("config/").mkdir();
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error creating " + file);
        }
        return file.isFile() && file.canWrite() && file.canRead();
    }

    /**
     * reads values from .prop(erties) file
     *
     * @param key category for value
     * @return returns value
     */
    public static String readsPropertiesFile(String key, String file) {
        File file_new = new File(file_path + "/" + file + ".prop");
        Properties prop = new Properties();
        InputStream input = null;
        checkFile(file_new);
        try {
            input = new FileInputStream(file_new);
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