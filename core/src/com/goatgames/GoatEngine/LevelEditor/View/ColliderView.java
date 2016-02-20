package com.goatgames.goatengine.leveleditor.view;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.physics.BoxCollider;
import com.goatgames.goatengine.physics.CircleCollider;
import com.goatgames.goatengine.physics.Collider;
import com.goatgames.goatengine.physics.PhysicsComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to display Colliders
 */
public class ColliderView extends ComponentView {


    private Collider collider;

    // WORK AROUND BEGIN
    // workaround to initialize staticCollider since super must be first instruction
    private static Collider staticCollider;
    private final Entity entity;

    private static PhysicsComponent init(PhysicsComponent phys, Collider collider){
        ColliderView.staticCollider = collider;
        return phys;
    }
    // WORK AROUND END

    public ColliderView(PhysicsComponent phys, Collider col, Entity entity, Skin skin) {
        super(init(phys, col),skin);
        collider = col;
        this.entity = entity;
        String colType;
        if (collider instanceof BoxCollider) {
            colType = "Box Collider";
        }
        else if (collider instanceof CircleCollider) {
            colType = "Circle Collider";
        }else{
            colType = "Null Pointer";
        }
        checkBoxEnable.setText(col.getTag() + colType);
        row();
    }

    @Override
    protected void initContent() {
        // Init based on type
        Map<String, String> map = staticCollider.toMap();
        for(String key: map.keySet()){
            String value = map.get(key);
            if(value.equals("true") || value.equals("false")){
                addBooleanField(key, Boolean.parseBoolean(value));
            }else{
                TextField field = addStringField(key, value);
            }
        }
    }

    @Override
    protected void onApply() {
        Map<String, String> map = new HashMap<String, String>();

        //Strings
        for(String key: this.stringFields.keySet()){
            map.put(key, this.stringFields.get(key).getText());
        }

        // Booleans
        for(String key: this.booleanFields.keySet()){
            map.put(key, String.valueOf(this.booleanFields.get(key).isChecked()));
        }

        // TODO array fields


        // Special Field
        String colType;
        if (collider instanceof BoxCollider) {
            colType = "box";
        } else if (collider instanceof CircleCollider) {
            colType = "circle";
        }else{
            colType = "Unknown"; // Null pointer
        }
        map.put("type", colType);
        try{
            // Update Collider
            Collider.updateCollider(entity, collider, Collider.defFromMap(map));
        }catch (RuntimeException e){
            new WarningDialog(e.getCause() + "\n" + e.getMessage(), getSkin()).show(this.getStage());
            e.printStackTrace();
        }
        // Update Inspector
        GoatEngine.eventManager.fireEvent(new UpdateInspectorEvent());
    }
}
