package com.goatgames.goatengine.desktop;

import com.ezware.dialog.task.TaskDialogs;
import com.goatgames.goatengine.GoatEngine;

/**
 * Uncaught Exception handler throwing a GUI Dialog
 */
public class DesktopExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //GoatEngine.logger.fatal("AN ERROR OCCURRED");
        GoatEngine.logger.fatal(e.getMessage());
        GoatEngine.logger.fatal(e);
        TaskDialogs.showException(e);
    }
}
