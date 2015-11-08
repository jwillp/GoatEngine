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


    /**
     * Contains Settings of the Screen Manager
     */
    public static class ScreenManager{

        // ON EMPTY STACK VALUES
        public static final String EXIT = "EXIT";                      // Causes the program to exit correctly
        public static final String CONTINUE = "CONTINUE";              // Causes the program to continue
        public static final String FATAL = "FATAL";                    // Causes the program to exit with error

        public static String ON_EMPTY_STACK = FATAL;                   // Action to take when scrn mngr's stack is empty

        public static final String GAME_SCREEN_EXT = ".ges";           // Extension of Game Screen Config FILE
        public static String MAIN_SCREEN = "main" + GAME_SCREEN_EXT;   // The main entry Screen (main.ges by default)
        public static String SCREEN_DIR = "data/screens/";                    // The directory containing screens

        public static String LEVEL_DIR = "data/levels/";                   // The Directory containing level config
    }

    /**
     * [DEV GENERAL]
     */
    public static class DevGeneral{

        public static boolean DEV_CTX = false;                  // Wether or not we are in dev context with stack traces
    }


    /**
     * [SCRIPTING_ENGINE]
     */
    public static class ScriptingEngine{
        public static boolean AUTO_RELOAD = true;              // If we need to reload scripts when their code change
        public static String  SCRIPTS_DIR= "data/scripts/";    // The directory where we store all scripts
    }


    public static class Logger{
        // [LOGGER]
        public static String LOG_DIRECTORY = "LOG/";                          // The directory where we store the logs
        // the format we use to name log file the %date% keyword will be replaced by the date of engine launch
        public static String FILE_NAME_FORMAT = "%date%_gelog.log";
        public static String EXCLUDE_LEVEL = "NONE";                      //To exclude log levels in log file (Ref. Logger.LogLevels)
    }


    // CONSOLE
    public static class Console{
        public static boolean CONS_ENABLED = false;     // Whether or not the console is enabled
    }


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
        DevGeneral.DEV_CTX = getBooleanProperty(DevGeneral.DEV_CTX, prop.getProperty("dev_ctx"));
    }

    private static void loadScreenManagerConfig(OrderedProperties prop){
        applyProperty(GEConfig.ScreenManager.ON_EMPTY_STACK, prop.getProperty("on_empty_stack"));
        applyProperty(GEConfig.ScreenManager.MAIN_SCREEN, prop.getProperty("main_screen"));
        applyProperty(GEConfig.ScreenManager.SCREEN_DIR, FileSystem.sanitiseDir(prop.getProperty("screens_dir")));
    }

    private static void loadScriptEngineConfig(OrderedProperties prop){
        ScriptingEngine.AUTO_RELOAD = getBooleanProperty(ScriptingEngine.AUTO_RELOAD, prop.getProperty("auto_reload"));
        applyProperty(ScriptingEngine.SCRIPTS_DIR, FileSystem.sanitiseDir(prop.getProperty("scripts_directory")));
        applyProperty(Logger.EXCLUDE_LEVEL, prop.getProperty("exclude_lvl"));

    }

    private static void loadLoggerConfig(OrderedProperties prop){
        applyProperty(Logger.FILE_NAME_FORMAT, prop.getProperty("log_file_name"));
        applyProperty(Logger.LOG_DIRECTORY, FileSystem.sanitiseDir(prop.getProperty("log_directory")));
        applyProperty(Logger.EXCLUDE_LEVEL, prop.getProperty("exclude_lvl"));
    }

    private static void loadConsoleConfig(OrderedProperties prop){
        Console.CONS_ENABLED = getBooleanProperty(Console.CONS_ENABLED, prop.getProperty("cons_enabled"));
    }



}
