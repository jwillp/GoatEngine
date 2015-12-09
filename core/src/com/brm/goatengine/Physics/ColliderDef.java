package com.brm.GoatEngine.Physics;

import com.brm.GoatEngine.Utils.PODType;

/**
 * The definition of a Collider
 */
public abstract class ColliderDef extends PODType{

    public String tag = "";
    public Object userdata = null;
    public float x = 0;                 // Relatively to the body
    public float y = 0;                 // Relatively to the body
    public boolean isSensor = false;



    public static class UnkownColliderTypeException extends RuntimeException{
        public UnkownColliderTypeException(String unkownType){
            super("Unknown collider type '" + unkownType + "'.");
        }
    }

}
