package com.goatgames.gdk;

import com.goatgames.gdk.logger.ILogger;
import com.goatgames.gdk.logger.SystemOutLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Classed use to test assertions
 */
public class GAssert{

    /**
     * Logger to use when an assertion error occurs
     */
    private static ILogger logger = new SystemOutLogger();

    /**
     * Map of all the stack traces associated with assertions.
     * Where the key is the assertion number, and the value the stacktrace.
     */
    private static Map<Integer, StackTraceElement[]> stackTraces = new HashMap<>();

    /**
     * Used as an index to be given to assertions
     */
    private static int assertIndex = 0;

    /**
     * Assert that test is true otherwise log failure
     *
     * @param test the statement to test
     * @param messageOnFail a message to display in case of failure
     */
    public static boolean that(boolean test, String messageOnFail){
        if(!test){
            logAssertionError(messageOnFail);
        }
        return test;
    }

    /**
     * Assert that test is true otherwise log failure
     *
     * @param test the statement to test
     * @param messageOnFail a message to display in case of failure
     */
    public static boolean that(boolean test, String messageOnFail, String fileName){
        if(!test){
            logAssertionError(messageOnFail, fileName);
        }
        return test;
    }

    /**
     * Assert that something is null
     *
     * @param o something
     * @param messageOnFail message to log on test failure
     */
    public static boolean notNull(Object o, String messageOnFail){
        boolean test = o != null;
        if(!test){
            logAssertionError(messageOnFail);
        }
        return test;
    }

    /**
     * Assert that something is null
     *
     * @param o something
     * @param messageOnFail message to log on test failure
     */
    public static boolean notNull(Object o, String messageOnFail, String fileName){
        boolean test = (o != null);
        if(!test){
            logAssertionError(messageOnFail, fileName);
        }
        return test;
    }

    /**
     * Assert that something is null
     *
     * @param o something
     * @param messageOnFail message to log on test failure
     */
    public static boolean isNull(Object o, String messageOnFail){
        boolean test = (o == null);
        if(!test){
            logAssertionError(messageOnFail);
        }
        return test;
    }

    /**
     * Assert that something is null
     *
     * @param o something
     * @param messageOnFail message to log on test failure
     */
    public static boolean isNull(Object o, String messageOnFail, String fileName){
        boolean test = (o == null);
        if(!test){
            logAssertionError(messageOnFail, fileName);
        }
        return test;
    }

    /**
     * Logs the assertion error to the logger
     *
     * @param message message to log as an error
     */
     private static void logAssertionError(String message){
         final StackTraceElement[] stackTrace = new Exception().getStackTrace();
         int index = saveStackTrace(stackTrace);
         if(logger != null) {
             logger.error(String.format("ASSERTION FAIL(%d) %s at: %s", index, message, stackTrace[2].toString()));
         }
     }

    /**
     * Logs the assertion error to the logger allowing to specify a file name
     *
     * @param messageOnFail message to log as an error
     * @param fileName name of the file where the error occurred
     */
    private static void logAssertionError(String messageOnFail, String fileName) {
        int index = saveStackTrace(new Exception().getStackTrace());
        if(logger != null) {
            logger.error(String.format("ASSERTION FAIL(%d) %s in: %s", index, messageOnFail, fileName));
        }
    }

    /**
     * Stores a certain stack trace in memory and returns the assertion number to which it was associated
     *
     * @param stackTrace the stack trace to save
     * @return the assertion index number
     */
    private static int saveStackTrace(StackTraceElement[] stackTrace){
        int index = nextAssertionIndex();
        stackTraces.put(index, stackTrace);
        return index;
    }

    /**
     * Returns the next number available and changes it to be the next of the next.
     */
    private static int nextAssertionIndex() {
        int returnIndex = assertIndex;
        assertIndex++;
        return returnIndex;
    }

    /**
     * Returns all the stack traces as a Map of all the stack traces associated with their assertions
     * by assertion number. Where the key is the assertion number, and the value the stacktrace.
     *
     * @return the stack traces and their assertion numbers
     */
    public Map<Integer, StackTraceElement[]> getStackTraces(){
        return stackTraces;
    }

    public static ILogger getLogger() {
        return logger;
    }

    public static void setLogger(ILogger logger) {
        GAssert.logger = logger;
    }
}
