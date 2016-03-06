package com.goatgames.goatengine.graphicsrendering.camera;

import com.badlogic.gdx.Gdx;

/**
 * Ensure a certain number of units is always displayed on the X axis
 */
public class FixedWidthStrategy extends CameraStrategy {

    private final int nbUnits;

    public FixedWidthStrategy(int nbUnits){

        this.nbUnits = nbUnits;
    }

    @Override
    public float getWidth() {
        return nbUnits;
    }

    @Override
    public float getHeight() {
        float ppu = Gdx.graphics.getWidth() / getWidth();
        return Gdx.graphics.getHeight() / ppu;
    }
}
