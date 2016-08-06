package com.goatgames.goatengine.config.gamescreen;

import com.badlogic.gdx.math.Vector2;

/**
 * Physics System Config.
 * This is used by game screen to configure the
 * Physics System
 */
public class PhysicsConfig {
    /**
     * The Gravity to apply initially to the engine
     */
    public Vector2 gravity = Vector2.Zero.cpy();
}
