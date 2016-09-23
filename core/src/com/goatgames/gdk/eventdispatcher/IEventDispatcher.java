package com.goatgames.gdk.eventdispatcher;

/**
 * Interface for event dispatching
 */
public interface IEventDispatcher {

    /**
     * Registers a listener to receive a certain type of events
     *
     * @param listener listener instance
     * @param eventClass class of the event tor register
     */
    void register(IEventListener listener, Class eventClass);

    /**
     * Registers a listener to receive any type of events
     * @param listener
     */
    void register(IEventListener listener);

    /**
     * Stop the dispatching of eventClass to a given listener
     * @param listener
     * @param eventClass
     */
    void unregister(IEventListener listener, Class eventClass);

    /**
     * Stop the dispatching of any event to a certain listener.
     *
     * @param listener listener to unregister
     */
    void unregister(IEventListener listener);

    /**
     * Fires an event to all the concerned listeners starting with the globals (in no particular order)
     * and then the specific (in no particular order)
     *
     * @param e event to fire
     */
    void fireEvent(Event e);

    /**
     * Clears the dispatcher from any listener
     */
    void clear();
}
