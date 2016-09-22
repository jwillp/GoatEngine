package com.goatgames.goatengine.eventmanager;

import com.goatgames.gdk.GAssert;
import com.goatgames.gdk.eventdispatcher.Event;
import com.goatgames.gdk.eventdispatcher.EventDispatcher;
import com.goatgames.goatengine.eventmanager.engineevents.EngineEvent;

/**
 * Class used by the engine to track all EngineEvents.
 * This manager can only fire EngineEvents
 */
public class EngineEventManager extends EventDispatcher {

    @Override
    public void fireEvent(Event e) {
        if(GAssert.that(e instanceof EngineEvent, "Event " + e.getClass().getSimpleName() + " is not an Engine Event")) {
            super.fireEvent(e);
        }
    }
}
