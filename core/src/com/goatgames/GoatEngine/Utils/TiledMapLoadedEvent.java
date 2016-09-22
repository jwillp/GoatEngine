package com.goatgames.goatengine.utils;

import com.goatgames.goatengine.eventmanager.engineevents.EngineEvent;

/**
 * Triggered when a tilemap is loaded
 */
public class TiledMapLoadedEvent extends EngineEvent {
    public final String mapFileName;

    public TiledMapLoadedEvent(String mapFileName) {
        this.mapFileName = mapFileName;
    }
}
