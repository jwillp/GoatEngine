package com.goatgames.goatengine.graphicsrendering.camera;

/**
 * Used to manually set the size of the viewport
 */
public class FixedStrategy extends CameraStrategy{

    private final int width;
    private final int height;

    public FixedStrategy(int width, int height){

        this.width = width;
        this.height = height;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }
}
