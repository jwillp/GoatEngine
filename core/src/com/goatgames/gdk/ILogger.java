package com.goatgames.gdk;

/**
 * Logger, to log information somewhere (console, file, network etc.)
 */
public interface ILogger {

    /**
     * Logs a message without any meaning being attached to it (e.g. debug,info,warning,error,fatal)
     * @param message the message to log
     */
    void log(Object message);

    /**
     * Logs a message as debug information. These kind of information are useful only during developpement and should
     * normally not appear in production logs.
     * @param message the message to log as debug information
     */
    void debug(Object message);

    /**
     * Logs a message as an information. This is normally to provide information in the log file about what is going on
     * in the program.
     * @param message the message to log as information
     */
    void info(Object message);

    /**
     * Logs a message as warning i.e. an important message but not erroneous.
     * @param message the message to log as a warning
     */
    void warn(Object message);

    /**
     * Logs a message as an error. The program should normally provide means to recover from that error,
     * although not mandatory.
     * @param message the message to log as an error
     */
    void error(Object message);

    /**
     * Logs a message as a fatal error. The program is normally completely unable to recover from these kind of errors
     * @param message the message to log as a fatal error
     */
    void fatal(Object message);
}
