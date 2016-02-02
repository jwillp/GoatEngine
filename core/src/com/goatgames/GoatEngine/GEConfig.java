package com.goatgames.goatengine;

import com.goatgames.goatengine.files.FileSystem;
import com.goatgames.goatengine.utils.EngineConfig;
import com.goatgames.goatengine.utils.OrderedProperties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Goat Engine Config class
 * contains all the settings for the engine (and it's sub modules) to work
 * correctly, by reading them from a file
 */
public class GEConfig extends EngineConfig {

    public static final String DATA_DIR = "data/";  // The directory containing all the data
    public static final String CONFIG_FILE = "ge.ini";     // The file to read the configuration from

    public static final Date LAUNCH_DATE = Calendar.getInstance().getTime(); //The date at which the engine was launched

    public static final String BUILD_VERSION = "160131";
    //public static final String BUILD_VERSION = new SimpleDateFormat("YYMMDD").format(GEConfig.LAUNCH_DATE);


    /**
     * [DEV GENERAL]
     */
    public static class DevGeneral extends EngineConfig {
        public static String GAME_NAME = "Goat Engine Game";
        public static String GAME_VERSION = "1.0";
        public static boolean DEV_CTX = false;                  // Whether or not we are in dev context with stack traces
        public static int VIEWPORT_WIDTH = 840;
        public static int VIEWPORT_HEIGHT = 640;
        public static boolean FULLSCREEN = false;

        private static void loadConfig(OrderedProperties prop){
            DevGeneral.DEV_CTX = prop.getProperty("dev_ctx", DevGeneral.DEV_CTX);
            DevGeneral.GAME_NAME = prop.getProperty("game_name", DevGeneral.GAME_NAME);
            DevGeneral.GAME_VERSION = prop.getProperty("game_version", DevGeneral.GAME_VERSION);
            DevGeneral.VIEWPORT_WIDTH = prop.getProperty("viewport_width", DevGeneral.VIEWPORT_WIDTH);
            DevGeneral.VIEWPORT_HEIGHT = prop.getProperty("viewport_height", DevGeneral.VIEWPORT_HEIGHT);
            DevGeneral.FULLSCREEN = prop.getProperty("fullscreen", DevGeneral.FULLSCREEN);
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
        public static String SCREEN_DIR = DATA_DIR + "screens/";             // The directory containing screens

        public static String MAIN_SCREEN = "main" + GAME_SCREEN_EXT;   // The main entry Screen (main.ges by default)

        public static String LEVEL_DIR = DATA_DIR + "levels/";               // The Directory containing level config


        private static void loadConfig(OrderedProperties prop){
            GEConfig.ScreenManager.ON_EMPTY_STACK = prop.getProperty("on_empty_stack", GEConfig.ScreenManager.ON_EMPTY_STACK);
            GEConfig.ScreenManager.MAIN_SCREEN = prop.getProperty("main_screen", GEConfig.ScreenManager.MAIN_SCREEN);
            GEConfig.ScreenManager.SCREEN_DIR = FileSystem.sanitiseDir(
                    prop.getProperty("screens_dir",
                            GEConfig.ScreenManager.SCREEN_DIR));
        }
    }

    /**
     * [SCRIPTING_ENGINE]
     */
    public static class ScriptingEngine extends EngineConfig {
        public static boolean AUTO_RELOAD = true;              // If we need to reload scripts when their code change
        public static String  SCRIPTS_DIR= DATA_DIR + "scripts/";    // The directory where we store all scripts

        private static void loadConfig(OrderedProperties prop){
            ScriptingEngine.AUTO_RELOAD = prop.getProperty("auto_reload", ScriptingEngine.AUTO_RELOAD);
            ScriptingEngine.SCRIPTS_DIR = FileSystem.sanitiseDir(
                    prop.getProperty("scripts_directory",
                            ScriptingEngine.SCRIPTS_DIR));
        }
    }


    public static class Logger extends EngineConfig {
        // [LOGGER]
        public static String LOG_DIRECTORY = DATA_DIR + "LOG/";                 // The directory where we store the logs
        // the format we use to name log file the %date% keyword will be replaced by the date of engine launch
        public static String FILE_NAME_FORMAT = "%date%.gelog";
        public static String EXCLUDE_LEVEL = "NONE";                      //To exclude log levels in log file (Ref. Logger.LogLevels)

        private static void loadConfig(OrderedProperties prop){
            Logger.FILE_NAME_FORMAT = prop.getProperty("log_file_name", Logger.FILE_NAME_FORMAT);
            Logger.LOG_DIRECTORY = FileSystem.sanitiseDir(prop.getProperty("log_directory", Logger.LOG_DIRECTORY));
            Logger.EXCLUDE_LEVEL = prop.getProperty("exclude_lvl", Logger.EXCLUDE_LEVEL);
        }
    }


    // CONSOLE
    public static class Console extends EngineConfig {
        public static boolean CONS_ENABLED = false;     // Whether or not the console is enabled


        private static void loadConfig(OrderedProperties prop){
            Console.CONS_ENABLED = prop.getProperty("cons_enabled", Console.CONS_ENABLED);
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
