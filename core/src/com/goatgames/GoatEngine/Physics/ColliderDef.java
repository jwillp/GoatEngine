package com.goatgames.goatengine.physics;

import java.util.Map;

/**
 * The definition of a Collider
 */
public abstract class ColliderDef{

    public String tag = "";
    public Object userdata = null;
    public float x = 0;                 // Relatively to the body
    public float y = 0;                 // Relatively to the body
    public boolean isSensor = false;

    /**
     * Makes a collider def from a Map
     * @param colliderData
     * @return
     */
    public static ColliderDef colliderDefFromNormalisedData(Map<String,String> colliderData){
        String colType = colliderData.get("type");
        ColliderDef colDef;
        // Circle Collider //
        if(colType.equals("circle")){
            colDef = new CircleColliderDef();
            CircleColliderDef circDef = (CircleColliderDef)colDef;
            circDef.radius = Float.parseFloat(colliderData.get("radius"));

            // Box Collider //
        }else if(colType.equals("box")){
            colDef = new BoxColliderDef();
            BoxColliderDef boxDef = (BoxColliderDef)colDef;
            boxDef.width = Float.parseFloat(colliderData.get("width"));
            boxDef.height = Float.parseFloat(colliderData.get("height"));

        }else if(colType.equals("capsule")){
            colDef = new CapsuleColliderDef();
            CapsuleColliderDef capDef = (CapsuleColliderDef)colDef;
            capDef.width = Float.parseFloat(colliderData.get("width"));
            capDef.height = Float.parseFloat(colliderData.get("height"));
        }else{
            // Throw Unknown Collider Type Exception
            throw new UnknownColliderTypeException(colType);
        }

        // Shared attributes among collider types
        colDef.tag = colliderData.get("tag");
        colDef.isSensor = Boolean.parseBoolean(colliderData.get("is_sensor"));
        colDef.x = Float.parseFloat(colliderData.get("position_x"));
        colDef.y = Float.parseFloat(colliderData.get("position_y"));
        return colDef;
    }

    public static class UnknownColliderTypeException extends RuntimeException{
        public UnknownColliderTypeException(String unknownType){
            super(String.format("Unknown collider type '%s'.", unknownType));
        }
    }

}
