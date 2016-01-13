package com.goatgames.goatengine.eventmanager.engineevents;

import com.goatgames.goatengine.eventmanager.Event;

/**
 * All Engine specific game events
 */
public class EngineEvents extends Event {

    /**
     * Triggered at the end of a game tick
     */
    public static class GameTickEndEvent extends Event {
    }

    /**
     * Triggered right before a game tick
     */
    public static class GameTickBeginEvent extends Event {
    }



    /**
     * Triggered when the tick of logic begins
     */
    public static class LogicTickBeginEvent extends Event {
    }

    /**
     * Triggered when the tick of logic begins
     */
    public static class LogicTickEndEvent extends Event {
    }


    /**
     * Triggered when the tick of render begins
     */
    public static class RenderTickBeginEvent extends Event {
    }

    /**
     * Triggered when the tick of render begins
     */
    public static class RenderTickEndEvent extends Event {
    }





    public static class ScreenResizedEvent extends Event {
        public final int newWidth;
        public final int newHeight;

        public ScreenResizedEvent(int newWidth, int newHeight) {
            this.newWidth = newWidth;
            this.newHeight = newHeight;
        }
    }



}
