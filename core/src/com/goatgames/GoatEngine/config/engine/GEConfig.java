package com.goatgames.goatengine.config.engine;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.ParseException;
import bsh.TargetError;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Class loading the Global engine settings
 */
public class GEConfig {
    /**
     * Directory containing the internal data files of the engine.
     * These are not game specific and are mandatory for the engine to work properly.
     */
    public final String PRIV_DATA_DIRECTORY = "priv_data/";

    /**
     * File used to override the engine config for game specific necessities
     */
    public final String GAME_SPEC_CONFIG_FILE = PRIV_DATA_DIRECTORY + "ge.bsh";

    /**
     * Used to store the date at which the engine was launched.
     * Useful for tracking how long has the game been running,
     * or to be used as information in logs.
     */
    public final Date LAUNCH_DATE = Calendar.getInstance().getTime(); // The date at which the engine was launched

    /**
     * Global flag used by the code base to detect whether or not the current build is for development purposes.
     * This allows the enabling of features such as (non exhaustive list)
     *  - Development console.
     *  - FPS display in title bar of application
     *  - Resolution display in title bar of application
     *  Some debug features can only be enabled if this flag is set to true
     *  Such as script auto reload, ui debug rendering.
     *  Therefore it can help disable most debug features.
     */
    public boolean dev_ctx = false;

    /**
     * Engine configuration for the current game
     */
    public GameConfig game = new GameConfig();

    /**
     * Configuration for the logger
     */
    public LoggerConfig logger = new LoggerConfig();

    /**
     * Configuration for the screen manager
     */
    public ScreenManagerConfig screen = new ScreenManagerConfig();

    /**
     * Configuration for scripting engine
     */
    public static ScriptingConfig scripting = new ScriptingConfig();

    /**
     * Configuration for the assets
     */
    public AssetsConfig assets = new AssetsConfig();

    /**
     * Configuration for the ui
     */
    public UIConfig ui = new UIConfig();

    /**
     * Configuration for the prefabs
     */
    public PrefabConfig prefab = new PrefabConfig();

    /**
     * If true launches game in fullscreen
     */
    public boolean fullscreen = false;
    public ResolutionConfig resolution = new ResolutionConfig();

    /**
     * Loads the configuration override for game specific needs
     */
    public void load(){
        Interpreter interpreter = new Interpreter();
        // Set variables
        try {
            interpreter.set("date", new Date());
            interpreter.set("config", new GEConfig());
            interpreter.source(GAME_SPEC_CONFIG_FILE);
            sanitize();
        } catch (TargetError e) {
            e.printStackTrace();
            // TODO Log
        } catch (ParseException e ) {
            // Parsing error
            e.printStackTrace();
            // TODO Log
        } catch (EvalError e ) {
            e.printStackTrace();
            // General Error evaluating script
            // TODO Log
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new MissingConfigFileException(
                    String.format("%s is missing from internal data. Please Contact Goat Game Support for assistance. ",
                            GAME_SPEC_CONFIG_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sanitises some configuration values.
     */
    private void sanitize(){

        // Make sure paths end with a forward slash
        scripting.directory = sanitizeDirectory(scripting.directory);
        logger.directory = sanitizeDirectory(logger.directory);
        assets.fontsDirectory = sanitizeDirectory(assets.fontsDirectory);
        assets.texturesDirectory = sanitizeDirectory(assets.texturesDirectory);
    }

    /**
     * Sanitizes a directory to make sure it ends with a forward slash
     * @param directory directory to sanitise
     * @return sanitised directory name
     */
    private static String sanitizeDirectory(String directory){
        if(!directory.endsWith("/")){
            return directory + "/";
        }
        return directory;
    }

    /**
     * Thrown when the internal priv_data ge.conf is missing
     */
    private static class MissingConfigFileException extends RuntimeException {
        public MissingConfigFileException(String msg){
            super(msg);
        }
    }
}
