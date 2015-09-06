package com.brm.GoatEngine;

import com.brm.GoatEngine.Files.FileSystem;
import com.brm.GoatEngine.Utils.OrderedProperties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Goat Engine Config class
 * contains all the settings for the engine (and it's sub modules) to work
 * correctly, by reading them from a file
 */
public class GEConfig {

    public static final String CONFIG_FILE = "data/eg.ini";     // The file to read the configuration from

    public static final Date LAUNCH_DATE = Calendar.getInstance().getTime(); //The date at which the engine was launched

    // [SCREEN_MNGR]
    // Actions to take if the scrn mngr's stack is empty
    public static final String SCRN_MNGR_EXIT = "EXIT";           // Causes the program to exit correctly
    public static final String SCRN_MNGR_CONTINUE = "CONTINUE";   // Causes the program to continue
    public static final String SCRN_MNGR_FATAL = "FATAL";         // Causes the program to exit with error

    public static String SCRN_MNGR_ON_EMPTY_STACK = SCRN_MNGR_FATAL; // Action to take when scrn mngr's stack is empty

    // [SCRIPTING_ENGINE]
    public static boolean SCRPT_ENG_AUTO_RELOAD = true;        // If we need to reload scripts when their code change
    public static String SCRPT_ENG_SCRIPTS_DIR= "scripts/";    // The directory where we store all scripts


    // [LOGGER]
    public static String LOG_DIRECTORY = "LOG/";                          // The directory where we store the logs
    // the format we use to name log file the %date% keyword will be replaced by the date of engine launch
    public static String LOG_FILE_NAME = LOG_DIRECTORY + "%date%_gelog.log";
    public static String LOG_EXCLUDE_LEVEL = "NONE";   //To exclude log levels in log file (Ref. Logger.LogLevels)




    public static void loadConfig(){
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(CONFIG_FILE);
            OrderedProperties prop = new OrderedProperties();
            prop.load(inputStream);

            loadLoggerConfig(prop);
            loadScreenManagerConfig(prop);
            loadScriptEngineConfig(prop);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void loadScreenManagerConfig(OrderedProperties prop){
        SCRN_MNGR_ON_EMPTY_STACK = prop.getProperty("on_empty_stack");
    }

    private static void loadScriptEngineConfig(OrderedProperties prop){
        SCRPT_ENG_AUTO_RELOAD = Boolean.parseBoolean(prop.getProperty("auto_reload"));
        SCRPT_ENG_SCRIPTS_DIR = FileSystem.sanitiseDir(prop.getProperty("scripts_directory"));
        LOG_EXCLUDE_LEVEL = prop.getProperty("exclude_lvl");

    }

    private static void loadLoggerConfig(OrderedProperties prop){
        LOG_FILE_NAME = prop.getProperty("log_file_name");
        LOG_DIRECTORY = FileSystem.sanitiseDir(prop.getProperty("log_directory"));
        LOG_EXCLUDE_LEVEL = prop.getProperty("exclude_lvl");
    }

}
