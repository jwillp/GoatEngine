package com.goatgames;

import com.goatgames.goatengine.eventmanager.Event;

/**
 * Called at the end of the logic loop (the update loop) after all systems have been updated.
 * (useful for logic that needs to execute after the physics loop. Or even camera handling.
 * This will ensure that the character has moved completely before the camera tracks its position.
 */
public class LateUpdateEvent extends Event {
}
