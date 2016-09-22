package com.goatgames.goatengine.eventmanager;

import com.goatgames.gdk.eventdispatcher.Event;

/**
 * Class used by scripts to send their own events in order to communicate.
 * Cross scripting communication.
 * Events should inherit GameEvent for GameSpecific needs and not Engine Events.
 * These should be considered different from EngineEvents
 */
public class GameEvent extends Event {
}
