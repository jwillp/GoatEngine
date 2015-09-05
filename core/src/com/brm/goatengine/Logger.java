package com.brm.goatengine;

import com.badlogic.gdx.Gdx;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *  Log System. Logs events in a log file.
 */
public class Logger {

    private static boolean printToScreen = true;

    private static String LOG_FILE = "game.log";

    private final static String INFO_HEADER = "[INFO]";
    private final static String ERROR_HEADER = "[ERROR]";
    private final static String WARNING_HEADER = "[WARNING]";
    private final static String FATAL_HEADER = "[FATAL]";


    /**
     * Writes the message with the time of display in a log file
     * and prints to the screen
     * @param message
     */
    private static void log(Object message){
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String time = "["+df.format(Calendar.getInstance().getTime())+"]";
        // TODO move from Gdx Dependency go FileSystem Class
        Gdx.files.local(LOG_FILE).writeString(time + " " + message + "\n", true, StandardCharsets.UTF_8.toString());
        print(message);
    }

    /**
     * Prints in the program's console
     */
    private static void print(Object message){
        if(printToScreen){
            System.out.println(message);
        }
    }

    /**
     * Writes a regular status update message
     * @param message
     */
    public static void info(Object message){
        log(INFO_HEADER + " " + message);
    }

    /**
     * Simply writes the message in the console screen
     * without loggin it. Useful to test values and debug things
     * @param message
     */
    public static void debug(Object message){
        print(message);
    }

    /**
     * Writes an important but not erroneous message
     * [WARNING]
     * @param message
     */
    public static void warn(Object message){
        log(WARNING_HEADER + " " + message);
    }

    /**
     * Writes an erroneous message (recoverable errors)
     * [ERROR]
     * @param message
     */
    public static void error(Object message){
        log(ERROR_HEADER + " " + message);
    }

    /**
     * Writes a fatal error message (no recovery)
     * [FATAL]
     * @param message
     */
    public static void fatal(Object message){
        log(FATAL_HEADER + " " + message);
        //TODO display message box
    }

    /**
     * Enables the screen printing
     */
    public void enableScreenPrinting(){
        printToScreen = true;
    }

    /**
     * Disable the screen printing
     */
    public void disableScreenPrinting(){
        printToScreen = false;
    }

    /**
     * Changes the path and name of the log file
     * @param logfilePath
     */
    public void setLogFilePath(String logfilePath){
        LOG_FILE = logfilePath;
    }



}
