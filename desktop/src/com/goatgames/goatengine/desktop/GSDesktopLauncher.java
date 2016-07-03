package com.goatgames.goatengine.desktop;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.eventmanager.Event;
import com.goatgames.goatengine.eventmanager.GameEventListener;

/**
 * Game Specific Desktop Launcher example
 */
public class GSDesktopLauncher implements GameEventListener{

    public void run(){
        GEDesktopLauncher GEDesktopLauncher = new GEDesktopLauncher();
        GEDesktopLauncher.run(this);
    }

    public static void main (String[] arg) {
        GSDesktopLauncher desktopLauncher = new GSDesktopLauncher();
        desktopLauncher.run();
    }

    @Override
    public void onEvent(Event e) {
        GoatEngine.logger.debug(e.toString());
    }



}
