package com.goatgames.goatengine.graphicsrendering.camera;

import com.badlogic.gdx.Gdx;

/**
 * Ensure a certain number of units is always displayed on the X axis
 */
public class FixedHeightStrategy extends CameraStrategy {

    private final int nbUnits;

    public FixedHeightStrategy(int nbUnits){

        this.nbUnits = nbUnits;
    }

    @Override
    public float getWidth() {
        float ppu = Gdx.graphics.getHeight() / getHeight();
        return Gdx.graphics.getWidth() / ppu;
    }

    @Override
    public float getHeight() {
        return nbUnits;
    }
}
