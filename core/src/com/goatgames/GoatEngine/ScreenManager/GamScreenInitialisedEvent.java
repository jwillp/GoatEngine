package com.goatgames.goatengine.screenmanager;

import com.goatgames.gdk.eventdispatcher.Event;

/**
 * Triggered when a GameScreen is initialised
 */
public class GamScreenInitialisedEvent extends Event {

    public final String screenName;

    public GamScreenInitialisedEvent(String screenName){
        this.screenName = screenName;
    }
}
