package com.goatgames.goatengine.physics;

/**
 * The definition of a Collider
 */
public abstract class ColliderDef{

    public String tag = "";
    public Object userdata = null;
    public float x = 0;                 // Relatively to the body
    public float y = 0;                 // Relatively to the body
    public boolean isSensor = false;


    public static class UnknownColliderTypeException extends RuntimeException{
        public UnknownColliderTypeException(String unkownType){
            super(String.format("Unknown collider type '%s'.", unkownType));
        }
    }

}
