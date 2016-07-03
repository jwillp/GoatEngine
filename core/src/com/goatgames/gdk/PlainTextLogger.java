package com.goatgames.gdk;

import com.goatgames.goatengine.files.IFileHandle;
import com.goatgames.goatengine.files.IFileManager;

/**
 * Logger writing information in a text file
 */
public class PlainTextLogger extends SystemOutLogger {

    private final IFileManager fileManager;  // File manager to use for locating the log file and handling it
    private IFileHandle fileHandle;         // Represents the file handle on the log file

    public PlainTextLogger(String logFilePath, IFileManager fileManager){
        this.fileManager = fileManager;
        setLogFilePath(logFilePath);
    }

    @Override
    public void log(Object message) {

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

        fileHandle.writeString(message.toString(), true);
    }

    public void setLogFilePath(String logFilePath) {
        fileHandle = fileManager.getFileHandle(logFilePath);
    }
}
