package com.goatgames.goatengine.eventmanager.engineevents;

import com.goatgames.goatengine.eventmanager.GameEvent;

/**
 * All Engine specific game events
 */
public class EngineEvents extends GameEvent {

    /**
     * Triggered at the end of a game tick
     */
    public static class GameTickEndEvent extends GameEvent{
    }

    /**
     * Triggered right before a game tick
     */
    public static class GameTickBeginEvent extends GameEvent {
    }



    /**
     * Triggered when the tick of logic begins
     */
    public static class LogicTickBeginEvent extends GameEvent {
    }

    /**
     * Triggered when the tick of logic begins
     */
    public static class LogicTickEndEvent extends GameEvent {
    }


    /**
     * Triggered when the tick of render begins
     */
    public static class RenderTickBeginEvent extends GameEvent{
    }

    /**
     * Triggered when the tick of render begins
     */
    public static class RenderTickEndEvent extends GameEvent{
    }





    public static class ScreenResizedEvent extends GameEvent{
        public final int newWidth;
        public final int newHeight;

        public ScreenResizedEvent(int newWidth, int newHeight) {
            this.newWidth = newWidth;
            this.newHeight = newHeight;
        }
    }



}
