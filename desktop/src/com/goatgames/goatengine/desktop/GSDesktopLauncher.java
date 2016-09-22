package com.goatgames.goatengine.desktop;

import com.goatgames.gdk.eventdispatcher.IEventListener;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.gdk.eventdispatcher.Event;
import com.goatgames.goatengine.eventmanager.EventListener;

/**
 * Game Specific Desktop Launcher example
 */
public class GSDesktopLauncher implements EventListener {

    public void run(){
        GEDesktopLauncher GEDesktopLauncher = new GEDesktopLauncher();
        GEDesktopLauncher.run(this);
    }

    public static void main (String[] arg) {
        GSDesktopLauncher desktopLauncher = new GSDesktopLauncher();
        desktopLauncher.run();
    }

    @Override
    public boolean onEvent(Event e) {
        GoatEngine.logger.debug(e.toString());
        return false;
    }
}
