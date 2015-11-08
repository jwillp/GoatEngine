package com.brm.GoatEngine.Utils;

import com.badlogic.gdx.Gdx;
import com.brm.GoatEngine.GEConfig;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *  Log System. Logs events in a log file.
 */
public class Logger {

    private static boolean printToScreen = true;


    // LEVELS
    private final static String LEVEL_INFO = "INFO";
    private final static String LEVEL_DEBUG = "DEBUG";
    private final static String LEVEL_WARNING = "WARNING";
    private final static String LEVEL_ERROR = "ERROR";
    private final static String LEVEL_FATAL = "FATAL";



    /**
     * Writes the message with the time of display in a log file
     * and prints to the screen
     * @param message
     */
    private static void log(String level, Object message){
        if(!GEConfig.Logger.EXCLUDE_LEVEL.equals(level)){
            String logTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
            String time = "["+logTime+"] ";

            String longDate = new SimpleDateFormat("YYYYMMDDHHmmss").format(GEConfig.LAUNCH_DATE);
            String outputFile = GEConfig.Logger.LOG_DIRECTORY + GEConfig.Logger.FILE_NAME_FORMAT.replace("%date%", longDate);

            Gdx.files.local(outputFile).writeString(
                    time + createHeader(level) + message + "\n", true, StandardCharsets.UTF_8.toString()
            );
            print(message);
        }
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
        log(LEVEL_INFO, message);
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
        log(LEVEL_WARNING, message);
    }

    /**
     * Writes an erroneous message
     * [ERROR]
     * @param message
     */
    public static void error(Object message){
        log(LEVEL_ERROR, message);
    }

    /**
     * Writes a fatal error message
     * [FATAL]
     * @param message
     */
    public static void fatal(Object message){
        log(LEVEL_FATAL, message);
        //TODO display message box
    }


    private static String createHeader(final String level){
        return "[" + level + "]" + " ";
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


    /***
     * Logs an Exception's stack trace
     * @param e
     */
    public static void logStackTrace(Throwable e){
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        boolean oldVal = printToScreen;
        printToScreen = false;
        fatal(sb.toString());
        printToScreen = oldVal;
    }



}
