package com.goatgames.goatengine;

import com.goatgames.goatengine.files.FileSystem;
import com.goatgames.goatengine.utils.DesktopExceptionDialog;
import com.goatgames.goatengine.utils.EngineConfig;
import com.goatgames.goatengine.utils.OrderedProperties;

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
public class GEConfig extends EngineConfig {

    public static final String CONFIG_FILE = "data/ge.ini";     // The file to read the configuration from

    public static final Date LAUNCH_DATE = Calendar.getInstance().getTime(); //The date at which the engine was launched

    /**
     * [DEV GENERAL]
     */
    public static class DevGeneral extends EngineConfig {
        public static String GAME_NAME = "Goat Engine Game";
        public static boolean DEV_CTX = false;                  // Whether or not we are in dev context with stack traces
        public static int VIEWPORT_WIDTH = 840;
        public static int VIEWPORT_HEIGHT = 640;
        public static boolean FULLSCREEN = false;

        private static void loadConfig(OrderedProperties prop){
            DevGeneral.DEV_CTX = getBooleanProperty(DevGeneral.DEV_CTX, prop.getProperty("dev_ctx"));
            DevGeneral.GAME_NAME = applyProperty(DevGeneral.GAME_NAME, prop.getProperty("game_name"));
            DevGeneral.VIEWPORT_WIDTH = getIntProperty(DevGeneral.VIEWPORT_WIDTH, prop.getProperty("viewport_width"));
            DevGeneral.VIEWPORT_HEIGHT = getIntProperty(DevGeneral.VIEWPORT_HEIGHT, prop.getProperty("viewport_height"));
            DevGeneral.FULLSCREEN = getBooleanProperty(DevGeneral.FULLSCREEN, prop.getProperty("fullscreen"));
        }



    }


    /**
     * Contains Settings of the Screen Manager
     */
    public static class ScreenManager extends EngineConfig {

        // ON EMPTY STACK VALUES
        public static final String EXIT = "EXIT";                      // Causes the program to exit correctly
        public static final String CONTINUE = "CONTINUE";              // Causes the program to continue
        public static final String FATAL = "FATAL";                    // Causes the program to exit with error

        public static String ON_EMPTY_STACK = FATAL;                   // Action to take when scrn mngr's stack is empty

        public static final String GAME_SCREEN_EXT = ".ges";           // Extension of Game Screen Config FILE
        public static String SCREEN_DIR = "data/screens/";             // The directory containing screens

        public static String MAIN_SCREEN = "main" + GAME_SCREEN_EXT;   // The main entry Screen (main.ges by default)

        public static String LEVEL_DIR = "data/levels/";               // The Directory containing level config


        private static void loadConfig(OrderedProperties prop){
            applyProperty(GEConfig.ScreenManager.ON_EMPTY_STACK, prop.getProperty("on_empty_stack"));
            GEConfig.ScreenManager.MAIN_SCREEN = applyProperty(GEConfig.ScreenManager.MAIN_SCREEN, prop.getProperty("main_screen"));
            applyProperty(GEConfig.ScreenManager.SCREEN_DIR, FileSystem.sanitiseDir(prop.getProperty("screens_dir")));
        }
    }

    /**
     * [SCRIPTING_ENGINE]
     */
    public static class ScriptingEngine extends EngineConfig {
        public static boolean AUTO_RELOAD = true;              // If we need to reload scripts when their code change
        public static String  SCRIPTS_DIR= "data/scripts/";    // The directory where we store all scripts

        private static void loadConfig(OrderedProperties prop){
            ScriptingEngine.AUTO_RELOAD = getBooleanProperty(ScriptingEngine.AUTO_RELOAD, prop.getProperty("auto_reload"));
            applyProperty(ScriptingEngine.SCRIPTS_DIR, FileSystem.sanitiseDir(prop.getProperty("scripts_directory")));
            applyProperty(Logger.EXCLUDE_LEVEL, prop.getProperty("exclude_lvl"));

        }
    }


    public static class Logger extends EngineConfig {
        // [LOGGER]
        public static String LOG_DIRECTORY = "data/LOG/";                 // The directory where we store the logs
        // the format we use to name log file the %date% keyword will be replaced by the date of engine launch
        public static String FILE_NAME_FORMAT = "%date%_gelog.log";
        public static String EXCLUDE_LEVEL = "NONE";                      //To exclude log levels in log file (Ref. Logger.LogLevels)

        private static void loadConfig(OrderedProperties prop){
            applyProperty(Logger.FILE_NAME_FORMAT, prop.getProperty("log_file_name"));
            applyProperty(Logger.LOG_DIRECTORY, FileSystem.sanitiseDir(prop.getProperty("log_directory")));
            applyProperty(Logger.EXCLUDE_LEVEL, prop.getProperty("exclude_lvl"));
        }
    }


    // CONSOLE
    public static class Console extends EngineConfig {
        public static boolean CONS_ENABLED = false;     // Whether or not the console is enabled


        private static void loadConfig(OrderedProperties prop){
            Console.CONS_ENABLED = getBooleanProperty(Console.CONS_ENABLED, prop.getProperty("cons_enabled"));
        }
    }


    public static void loadConfig(){
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(CONFIG_FILE);
            OrderedProperties prop = new OrderedProperties();
            prop.load(inputStream);

            DevGeneral.loadConfig(prop);
            Logger.loadConfig(prop);
            ScreenManager.loadConfig(prop);
            ScriptingEngine.loadConfig(prop);
            Console.loadConfig(prop);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FileSystem.FileNotFoundException("ge.ini not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
