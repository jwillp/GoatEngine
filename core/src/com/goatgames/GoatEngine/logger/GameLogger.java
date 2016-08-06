package com.goatgames.goatengine.logger;

import com.goatgames.gdk.logger.ILogger;
import com.goatgames.gdk.logger.SystemOutLogger;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.config.engine.LoggerConfig;
import com.goatgames.goatengine.files.IFileHandle;
import com.goatgames.goatengine.files.IFileManager;
import com.goatgames.goatengine.utils.NotImplementedException;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * The game logger logs information about the game in a special XML format.
 * This special format will then be read and used by external tools such as the Goat Engine Log Viewer (GELV)
 */
public class GameLogger implements ILogger {

    public final IFileManager fileManager;
    public IFileHandle logFileHandle;

    private final SystemOutLogger systemOutLogger; // Log to the console if enabled

    public GameLogger(IFileManager fileManager) {
        this.fileManager = fileManager;
        this.logFileHandle = null;
        systemOutLogger = new SystemOutLogger();
    }

    /**
     * Logs a string in the file for a certain level. (Converting it to XML as necessary)
     * @param level
     * @param message
     */
    private void logData(LoggerConfig.Levels level, Object message){
        // Check if the file was created, if not create it and add defualt data (OS Env, date, game name etc ..)
        if(logFileHandle == null){
            // Create a new log file
            String longDate = new SimpleDateFormat("YYYYMMDDHHmmss").format(GoatEngine.config.LAUNCH_DATE);
            final String LOGGER_DIRECTORY = GoatEngine.config.logger.directory;
            final String LOG_FILE_FORMAT = GoatEngine.config.logger.file_name_format;
            String logFilePath =  LOGGER_DIRECTORY + LOG_FILE_FORMAT.replace("%date%", longDate);
            setLogFilePath(logFilePath);

            // Dump environment information in the file
            String osInfo = String.format("OS: %s  Version: %s Architecture: %s",
                    System.getProperty("os.name"),
                    System.getProperty("os.version"),
                    System.getProperty("os.arch")
            );

            String envXml = envToXml(logFilePath, GoatEngine.config.LAUNCH_DATE.toString(), osInfo);
            logFileHandle.writeString(envXml + "\n", true, StandardCharsets.UTF_8.toString());
        }

        // Check if logging of the level is permitted with config
        if (!GoatEngine.config.logger.levels.contains(level))return;

        // File Line (outputs the line at which the call to log the message was done)
        String stack = new Exception().getStackTrace()[2].toString();
        int firstBracket = stack.indexOf('(');
        String fileAndLine = stack.substring(firstBracket + 1, stack.indexOf(')', firstBracket));

        // Convert message to XML entry and logs it
        String xml = logToXml(level, message, fileAndLine, getTimeInfo());
        logFileHandle.writeString(xml + "\n", true, StandardCharsets.UTF_8.toString());
    }

    /**
     * Returns a string in the form [HH:mm:ss] to be used by the different log methods.
     * e.g. [12:23:45]
     * @return time information
     */
    private String getTimeInfo(){
        String logTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        return String.format("[%s]", logTime);
    }

    @Override
    public void log(Object message) {
        if(mustPrintToScreen()) systemOutLogger.log(message);
        throw new NotImplementedException();
    }

    @Override
    public void debug(Object message) {
        if(mustPrintToScreen()) systemOutLogger.debug(message);
        logData(LoggerConfig.Levels.DEBUG, message);
    }

    @Override
    public void info(Object message) {
        if(mustPrintToScreen()) systemOutLogger.info(message);
        logData(LoggerConfig.Levels.INFO, message);
    }

    @Override
    public void warn(Object message) {
        if(mustPrintToScreen()) systemOutLogger.warn(message);
        logData(LoggerConfig.Levels.WARNING, message);
    }

    @Override
    public void error(Object message) {
        if(mustPrintToScreen()) systemOutLogger.error(message);
        logData(LoggerConfig.Levels.ERROR, message);
    }

    @Override
    public void fatal(Object message) {
        if(mustPrintToScreen()) systemOutLogger.fatal(message);
        logData(LoggerConfig.Levels.FATAL, message);
    }

    /**
     * Returns the environment information in XML format
     * @param logFileSimpleName the simple name of the log file (with extension)
     * @param logDate the date of creation of the log file
     * @param systemOS the current system OS
     * @return a string of the environment in XML format
     */
    private String envToXml(String logFileSimpleName, String logDate, String systemOS){
        String buildCtx = GoatEngine.config.dev_ctx ? "DEV" : "PROD";
        String engineBuild = GoatEngine.BUILD_VERSION + "" + buildCtx;

        return "<title>"+ logFileSimpleName +"</title>" +
                "<environement>" +
                "<date>" + logDate + "</date>" +
                "<systemos>" + systemOS + "</systemos>" +
                "<enginebuild>" + engineBuild + "</enginebuild>" +
                "<game>" + GoatEngine.config.game.name + "</game>" +
                "<gamebuild>" + GoatEngine.config.game.version + "</gamebuild>" +
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
    private static String logToXml(LoggerConfig.Levels level, Object message, String fileLine, String timeStamp){
        timeStamp = "<timestamp>" + timeStamp + "</timestamp>" + "\n";
        String strlevel = "<level>" + level.toString() + "</level>" + "\n";
        fileLine = "<file>"+ fileLine +"</file>" + "\n";
        message = "<message>" + message + "</message>" + "\n";
        return "<entry>" + "\n" +  timeStamp + strlevel + message + fileLine  + "</entry>" + "\n";
    }

    /**
     * Indicates if the logger must print to screen or not
     * @return true if the logger must print to screen, otherwise false.
     */
    private boolean mustPrintToScreen(){
        return true; // TODO;
    }

    /**
     * Sets the file path of the log file
     * @param filePath the path of the log file to use
     */
    public void setLogFilePath(String filePath){
        logFileHandle = fileManager.getFileHandle(filePath);
    }
}
