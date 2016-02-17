package com.goatgames.goatengine.eventmanager;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Manages Game Events and the listeners
 */
public class EventManager{

    LinkedHashMap<Long, Event> events = new LinkedHashMap<Long, Event>();
    ArrayList<GameEventListener> listeners = new ArrayList<GameEventListener>();

    public EventManager() {


    }

    /**
     * Adds a new Listener to the listeners list
     * @param listener
     */
    public void registerListener(GameEventListener listener){
        this.listeners.add(listener);
    }

    /**
     * Removes a new Listener from the listeners list
     * @param listener
     */
    public void unregisterListener(GameEventListener listener){
        this.listeners.remove(listener);
    }


    /**
     * Returns the current time
     */
    private long getCurrentTime(){
        return System.currentTimeMillis();
    }

    /**
     * Fires an event to all the listeners logging it
     * @param e
     */
    public void fireEvent(Event e){
        fireEvent(e, true);
    }


    /**
     * Fires an event to all the listeners
     * @param e event
     * @param mustLogEvent whether or not to log
     */
    public void fireEvent(Event e, boolean mustLogEvent) {
        if(mustLogEvent)
            this.events.put(getCurrentTime(), e);
        for(GameEventListener listener: this.listeners){
            listener.onEvent(e);
        }
    }
}
