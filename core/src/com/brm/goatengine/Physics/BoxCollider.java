package com.brm.GoatEngine.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.brm.GoatEngine.Utils.PODType;

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


    @Override
    public PODType toPODType() {
        BoxColliderDef def = new BoxColliderDef();
        def.width = width;
        def.height = height;
        def.isSensor = this.isSensor();
        def.tag = this.tag;
        Vector2 pos = getCentroid();
        def.x = pos.x;
        def.y = pos.y;
        return def;
    }


    /**
     * Returns center position of polygon
     * Formula Centroid = ( (vertex1.x + vertex2.x + vertexN.x) / nbVector,
     *                      (vertex1.y + vertex2.y + vertexN.y) / nbVector)
     *
     * @return centroid position
     */
    public Vector2 getCentroid(){
        PolygonShape shape = (PolygonShape) fixture.getShape();
        Vector2 delta = new Vector2(0,0);
        for(int i=0; i<shape.getVertexCount(); i++){
            Vector2 v = new Vector2();
            shape.getVertex(i,v);
            delta.x += v.x;
            delta.y += v.y;
        }

        delta.x /= shape.getVertexCount();
        delta.y /= shape.getVertexCount();
        return delta;
    }
}

