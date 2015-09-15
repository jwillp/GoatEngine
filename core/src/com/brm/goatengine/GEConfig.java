package com.brm.GoatEngine;

import com.brm.GoatEngine.Files.FileSystem;
import com.brm.GoatEngine.Utils.GameConfig;
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
public class GEConfig extends GameConfig {

    public static final String CONFIG_FILE = "data/ge.ini";     // The file to read the configuration from

    public static final Date LAUNCH_DATE = Calendar.getInstance().getTime(); //The date at which the engine was launched


    //[DEV GENERAL]
    public static boolean DEV_CTX = false;                  // Wether or not we are in dev context with stack traces


    // [SCREEN_MNGR]
    // Actions to take if the scrn mngr's stack is empty
    public static final String SCRN_MNGR_EXIT = "EXIT";           // Causes the program to exit correctly
    public static final String SCRN_MNGR_CONTINUE = "CONTINUE";   // Causes the program to continue
    public static final String SCRN_MNGR_FATAL = "FATAL";         // Causes the program to exit with error
    public static final String SCRN_MNGR_GAME_SCREEN_EXT = ".ges";      // Extension of Game Screen Config FILE

    public static  String SCRN_MNGR_MAIN_SCREEN = "main" + SCRN_MNGR_GAME_SCREEN_EXT;   // The main entry Screen
    public static String SCRN_MNGR_DIR = "data/screens/";                // The directory containing screens
    public static String SCRN_MNGR_MAP_DIR = "data/maps/";               // The Directory containing map congfig

    public static String SCRN_MNGR_ON_EMPTY_STACK = SCRN_MNGR_FATAL; // Action to take when scrn mngr's stack is empty

    // [SCRIPTING_ENGINE]
    public static boolean SCRPT_ENG_AUTO_RELOAD = true;        // If we need to reload scripts when their code change
    public static String  SCRPT_ENG_SCRIPTS_DIR= "data/scripts/";    // The directory where we store all scripts


    // [LOGGER]
    public static String LOG_DIRECTORY = "LOG/";                          // The directory where we store the logs
    // the format we use to name log file the %date% keyword will be replaced by the date of engine launch
    public static String LOG_FILE_NAME = "%date%_gelog.log";
    public static String LOG_EXCLUDE_LEVEL = "NONE";   //To exclude log levels in log file (Ref. Logger.LogLevels)



    // CONSOLE
    public static boolean CONS_ENABLED = false;     // Whether or not the console is enabled


    public static void loadConfig(){
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(CONFIG_FILE);
            OrderedProperties prop = new OrderedProperties();
            prop.load(inputStream);

            loadGeneralConfig(prop);
            loadLoggerConfig(prop);
            loadScreenManagerConfig(prop);
            loadScriptEngineConfig(prop);
            loadConsoleConfig(prop);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadGeneralConfig(OrderedProperties prop){
        DEV_CTX = getBooleanProperty(DEV_CTX, prop.getProperty("dev_ctx"));
    }

    private static void loadScreenManagerConfig(OrderedProperties prop){
        applyProperty(SCRN_MNGR_ON_EMPTY_STACK, prop.getProperty("on_empty_stack"));
        applyProperty(SCRN_MNGR_MAIN_SCREEN, prop.getProperty("main_screen"));
        applyProperty(SCRN_MNGR_DIR , FileSystem.sanitiseDir(prop.getProperty("screens_dir")));
    }

    private static void loadScriptEngineConfig(OrderedProperties prop){
        SCRPT_ENG_AUTO_RELOAD = getBooleanProperty(SCRPT_ENG_AUTO_RELOAD, prop.getProperty("auto_reload"));
        applyProperty(SCRPT_ENG_SCRIPTS_DIR, FileSystem.sanitiseDir(prop.getProperty("scripts_directory")));
        applyProperty(LOG_EXCLUDE_LEVEL, prop.getProperty("exclude_lvl"));

    }

    private static void loadLoggerConfig(OrderedProperties prop){
        applyProperty(LOG_FILE_NAME, prop.getProperty("log_file_name"));
        applyProperty(LOG_DIRECTORY, FileSystem.sanitiseDir(prop.getProperty("log_directory")));
        applyProperty(LOG_EXCLUDE_LEVEL, prop.getProperty("exclude_lvl"));
    }

    private static void loadConsoleConfig(OrderedProperties prop){
        CONS_ENABLED = getBooleanProperty(CONS_ENABLED, prop.getProperty("cons_enabled"));
    }



}
