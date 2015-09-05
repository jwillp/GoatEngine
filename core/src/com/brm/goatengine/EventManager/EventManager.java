package com.brm.goatengine.EventManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Manages the engine's events and the listeners.
 * In order for classes to receive events, they need to implement the EventListener class
 */
public class EventManager{

    /**
     * List of all the events
     * as a linked HashMap where the key is the time at which they were fired
     */
    LinkedHashMap<Long, GameEvent> events = new LinkedHashMap<Long, GameEvent>();

    /**
     * All event listeners
     */
    ArrayList<GameEventListener> listeners = new ArrayList<GameEventListener>();


    /**
     * Ctor
     */
    public EventManager(){}




    /**
     * Adds a new Listener to the listeners list
     * @param listener the listener to register
     */
    public void registerListener(GameEventListener listener){
        this.listeners.add(listener);
    }

    /**
     * Removes a certain Listener from the listeners list
     * @param listener the listener to unregister
     */
    public void unregisterListener(GameEventListener listener){
        this.listeners.remove(listener);
    }



    /**
     * Fires an event to all the registered listeners
     * @param e
     */
    public void fireEvent(GameEvent e){
        this.events.put(getCurrentTime(), e);
        for(GameEventListener listener: this.listeners){
            listener.onEvent(e);
        }
    }

    /**
     * Returns the current time
     */
    private long getCurrentTime(){
        // TODO abstract in a time module
        return System.currentTimeMillis();
    }


}
