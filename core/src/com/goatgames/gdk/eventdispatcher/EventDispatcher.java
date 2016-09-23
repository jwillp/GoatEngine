package com.goatgames.gdk.eventdispatcher;

import com.goatgames.gdk.GAssert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple implementation of an event dispatcher allowing
 * Listeners to register to certain types of events and other, or to any events
 */
public class EventDispatcher implements IEventDispatcher{

    /**
     * Listeners must register themselves for certain types of events
     */
    Map<Class, List<IEventListener>> listeners;

    /**
     * Listeners listening to all kinds of events
     */
    List<IEventListener> globalListeners;

    public EventDispatcher() {
        listeners = new HashMap<>();
        globalListeners = new ArrayList<>(10);
    }

    @Override
    public void register(IEventListener listener, Class eventClass) {
        List<IEventListener> classListeners = this.listeners.get(eventClass);
        if(classListeners == null){
            classListeners = new ArrayList<>(10);
            listeners.put(eventClass, classListeners);
        }

        if(GAssert.that(!classListeners.contains(listener),
                String.format("Listener %s already registered for %s", listener, eventClass.getSimpleName()))){
            classListeners.add(listener);
        }
    }

    @Override
    public void register(IEventListener listener) {
        if(GAssert.that(!globalListeners.contains(listener),
                String.format("Global listener %s already registered", listener))){
            globalListeners.add(listener);
        }
    }

    @Override
    public void unregister(IEventListener listener, Class eventClass) {
        final List<IEventListener> classListener = this.listeners.get(eventClass);
        if(classListener != null && classListener.contains(listener)){
            classListener.remove(listener);
        }
    }

    @Override
    public void unregister(IEventListener listener) {
        // Remove from global
        globalListeners.remove(listener);

        // Remove from specific
        for(Class eventClass : this.listeners.keySet()){
            unregister(listener, eventClass);
        }
    }

    /**
     * Fires an event to all the concerned listeners (in no particular order)
     *
     * @param e event to fire
     */
    public void fireEvent(Event e){
        // Specific
        for(IEventListener listener: getListenersForEvent(e.getClass())){
            // consume
            if(listener.onEvent(e)) return;
        }
    }

    @Override
    public void clear() {
        this.globalListeners.clear();
        this.listeners.clear();
    }

    /**
     * Returns a list of the listeners concerned about a certain event type including globals
     *
     * @param eventClass class of the event
     * @return list of listeners or an empty list no registered listeners are listening to this event
     */
    public List<IEventListener> getListenersForEvent(Class eventClass){
        List<IEventListener> result = new ArrayList<>(this.globalListeners);
        final List<IEventListener> eventListeners = this.listeners.get(eventClass);
        if(eventListeners != null){
            result.addAll(eventListeners);
        }
        return result;
    }
}
