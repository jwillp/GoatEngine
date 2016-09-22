package com.goatgames.goatengine.input;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.goatgames.gdk.eventdispatcher.Event;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.input.events.touch.*;

/**
 * Manages Gesture Input events. A gesture could be a pinch with two
 * fingers to indicate the desire to zoom, a tap or double tap, a long
 * press and so on.
 */
public class GestureManager implements GestureDetector.GestureListener {

    private final InputManager inputManager;

    public GestureManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    /**
     *
     * @param x
     * @param y
     * @param pointer
     * @param button
     * @return
     */
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        fireEvent(new TouchDownEvent(x,y));
        return false;
    }

    /**
     * Called when a tap occured. A tap happens if a touch went down on the screen and was lifted again without moving outside
     * of the tap square. The tap square is a rectangular area around the initial touch position as specified on construction
     * time of the {@link GestureDetector}.
     *
     * @param x
     * @param y
     * @param count  the number of taps.
     * @param button
     */
    @Override
    public boolean tap(float x, float y, int count, int button) {
        fireEvent(new TapEvent(x,y,count));
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        fireEvent(new LongPressEvent(x,y));
        return false;
    }

    /**
     * Called when the user dragged a finger over the screen and lifted it. Reports the last known velocity of the finger in
     * pixels per second.
     *
     * @param velocityX velocity on x in seconds
     * @param velocityY velocity on y in seconds
     * @param button
     */
    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        fireEvent(new FlingEvent(velocityX,velocityY));
        return false;
    }

    /**
     * Called when the user drags a finger over the screen.
     *
     * @param x
     * @param y
     * @param deltaX the difference in pixels to the last drag event on x.
     * @param deltaY the difference in pixels to the last drag event on y.
     */
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        fireEvent(new PanEvent(x,y,deltaX,deltaY));
        return false;
    }

    /**
     * Called when no longer panning.
     *
     * @param x
     * @param y
     * @param pointer
     * @param button
     */
    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        fireEvent(new PanStoppedEvent(x,y));
        return false;
    }

    /**
     * Called when the user performs a pinch zoom gesture. The original distance is the distance in pixels when the gesture
     * started.
     *
     * @param initialDistance distance between fingers when the gesture started.
     * @param distance        current distance between fingers.
     */
    @Override
    public boolean zoom(float initialDistance, float distance) {
        fireEvent(new ZoomEvent(initialDistance,distance));
        return false;
    }

    /**
     * Called when a user performs a pinch zoom gesture. Reports the initial positions of the two involved fingers and their
     * current positions.
     *
     * @param initialPointer1
     * @param initialPointer2
     * @param pointer1
     * @param pointer2
     */
    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        GoatEngine.eventManager.fireEvent(new PinchEvent(initialPointer1,initialPointer2,pointer1,pointer2));
        return false;
    }


    private void fireEvent(Event event){
        // TODO get event manager as Reference in ctor?
        GoatEngine.eventManager.fireEvent(event);
    }
}
