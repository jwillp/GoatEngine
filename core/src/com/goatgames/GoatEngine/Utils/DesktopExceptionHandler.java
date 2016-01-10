package com.goatgames.goatengine.utils;

import com.ezware.dialog.task.TaskDialogs;

/**
 * Uncaught Exception handler throwing a GUI Dialog
 */
public class DesktopExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        TaskDialogs.showException(e);
        Logger.fatal("AN ERROR OCCURRED");
        Logger.fatal(e.getMessage());
        Logger.logStackTrace(e);
    }
}
