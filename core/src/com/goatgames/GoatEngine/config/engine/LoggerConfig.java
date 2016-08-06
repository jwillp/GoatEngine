package com.goatgames.goatengine.config.engine;

import java.util.EnumSet;

/**
 * Configuration for the logger
 */
public class LoggerConfig {
    /**
     * Directory that will contain log files
     */
    public String directory = "LOG/";

    /**
     * Pattern for file names.
     * The special keyword %date% is replaced by LAUNCH_DATE.
     *
     * Ex.
     * For a game called The Legend Of Zelda where the engine is launched on 2016/05/05
     *  if the pattern is as follows: tloz_%date%_160505.gelog
     *  tloz_160505.gelog
     */
    public String file_name_format = "%date%.gelog";

    /**
     * Levels to log, omit one to exclude
     */
    public EnumSet levels = EnumSet.allOf(Levels.class);

    /**
     * The different types of information to log
     */
    public enum Levels{
        INFO,
        WARNING,
        DEBUG,
        ASSERT,
        ERROR,
        FATAL,
    }
}
