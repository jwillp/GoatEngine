package com.brm.GoatEngine.Physics;

/**
 * A Capsule shaped collider.
 * (it is simply two circle colliders and a box collider)
 */
public class CapsuleColliderDef extends ColliderDef {

    // Size
    public float width;
    public float height;


    // Child colliders definitions
    public CircleColliderDef topColliderDef = new CircleColliderDef();
    public BoxColliderDef middleColliderDef = new BoxColliderDef();
    public CircleColliderDef bottomColliderDef = new CircleColliderDef();

}
