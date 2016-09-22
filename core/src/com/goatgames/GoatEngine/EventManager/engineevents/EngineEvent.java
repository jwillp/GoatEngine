package com.goatgames.goatengine.eventmanager.engineevents;

import com.goatgames.gdk.eventdispatcher.Event;

/**
 * Base class for all events fired and handled by the engine
 * If an Event directly inherits from this class, it will only be visible by
 * the engine internally. The entity system and thus scripts won't be notified by these kinds of events.
 * (Unless script instance implements the IEventListener and registers itself with the engine's event manager
 * directly)
 */
public abstract class EngineEvent extends Event {

}
