package com.goatgames.goatengine.utils;

import com.badlogic.gdx.Gdx;
import com.goatgames.goatengine.GEConfig;

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


    private static String logFile;

    /**
     * Writes the message with the time of display in a log file
     * and prints to the screen
     * @param message
     */
    private static void log(String level, Object message){
        if (!GEConfig.getArray("logger.levels").contains(level, false))return;

        String logTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        String time = "["+logTime+"] ";

        if(logFile == null){
            String longDate = new SimpleDateFormat("YYYYMMDDHHmmss").format(GEConfig.LAUNCH_DATE);
            String outputFile = GEConfig.getString("logger.directory") +
                                GEConfig.getString("logger.file_name_format").replace("%date%", longDate);




            String osInfo = String.format("OS: %s  Version: %s Architecture: %s",
                    System.getProperty("os.name"),
                    System.getProperty("os.version"),
                    System.getProperty("os.arch"));

            String envXml = envToXml(outputFile, GEConfig.LAUNCH_DATE.toString(), osInfo);
            Gdx.files.local(outputFile).writeString(envXml + "\n", true, StandardCharsets.UTF_8.toString());
            logFile = outputFile;
        }

        // File Line
        String stack = new Exception().getStackTrace()[2].toString();
        int firstBracket = stack.indexOf('(');
        String fileAndLine = stack.substring(firstBracket + 1, stack.indexOf(')', firstBracket));


        String xml = logToXml(level, message, fileAndLine, time);

        Gdx.files.local(logFile).writeString(xml + "\n", true, StandardCharsets.UTF_8.toString());
        print(message);
    }


    /**
     * Returns the environnement information in an XML format
     * @return
     */
    private static String envToXml(String logName, String logDate, String systemOs){

        String buildCtx = GEConfig.getBoolean("dev_ctx") ? "DEV" : "PROD";
        String engineBuild = GEConfig.BUILD_VERSION + "" + buildCtx;

        return "<title>"+ logName +"</title>" +
        "<environement>" +
            "<date>" + logDate + "</date>" +
            "<systemos>" + systemOs + "</systemos>" +
            "<enginebuild>" + engineBuild + "</enginebuild>" +
            "<game>" + GEConfig.getString("game.name") + "</game>" +
            "<gamebuild>" + GEConfig.getString("game.version") + "</gamebuild>" +
        "</environement>";
    }

    /**
     * Returns a log entry in xml format
     * @param level
     * @param message
     * @param fileLine
     * @param timeStamp
     * @return
     */
    private static String logToXml(String level, Object message, String fileLine, String timeStamp){


        timeStamp = "<timestamp>" + timeStamp + "</timestamp>" + "\n";
        level = "<level>" + level + "</level>" + "\n";
        fileLine = "<file>"+ fileLine +"</file>" + "\n";
        message = "<message>" + message + "</message>" + "\n";


        return "<entry>" + "\n" +  timeStamp + level + message + fileLine  + "</entry>" + "\n";

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
        print(LEVEL_DEBUG + ": " + message);
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
