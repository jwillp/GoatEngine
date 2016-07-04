package com.goatgames.gdk.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Logger displaying log information directly in stdout
 */
public class SystemOutLogger implements ILogger {
    @Override
    public void log(Object message) {
        System.out.println(message);
    }

    /**
     * Logs data for a certain level in a standardised manner
     * @param level (INFO, DEBUG,WARNING,ERROR,FATAL)
     */
    private void logData(String level, Object message){
        // Special case for logging Stack Trace
        if(message instanceof Throwable) {
            Throwable t = (Throwable) message;
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement element : t.getStackTrace()) {
                sb.append(element.toString());
                sb.append("\n");
            }
            message = sb.toString();
        }
        log(String.format("[%s][%s]: %s", getTimeInfo(), level, message));
    }

    @Override
    public void debug(Object message) {
        logData("DEBUG", message);
    }

    @Override
    public void info(Object message) {
        logData("INFO", message);
    }

    @Override
    public void warn(Object message) {
        logData("WARNING", message);
    }

    @Override
    public void error(Object message) {
        logData("ERROR", message);
    }

    @Override
    public void fatal(Object message) {
        logData("FATAL", message);
    }

    /**
     * Returns a string in the form [HH:mm:ss] to be used by the different log methods.
     * e.g. [12:23:45]
     * @return time information
     */
    private String getTimeInfo(){
        return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    }
}
