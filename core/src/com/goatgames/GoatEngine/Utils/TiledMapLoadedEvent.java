package com.goatgames.goatengine.utils;

import com.goatgames.goatengine.eventmanager.Event;

/**
 * Triggered when a tilemap is loaded
 */
public class TiledMapLoadedEvent extends Event {
    public final String mapFileName;

    public TiledMapLoadedEvent(String mapFileName) {
        this.mapFileName = mapFileName;
    }
}
