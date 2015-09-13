package com.brm.GoatEngine.Physics;

/**
 * A Rectangle Collider
 */
public class BoxCollider extends Collider {

    float width;
    float height;

    /**
     * Returns the height
     * @return
     */
    public float getHeight(){
        return height;
    }

    /**
     * Returns the width
     * @return
     */
    public float getWidth(){
        return width;
    }


    /**
     * Sets the size of the box
     * @param width
     * @param height
     */
    public void setSize(float width, float height){
        this.width = width;
        this.height = height;

        // TODO see if it is possible to update the fixture

    }


}

