package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.brm.GoatEngine.ECS.common.PhysicsComponent;
import com.brm.GoatEngine.Physics.*;
import com.brm.GoatEngine.ScriptingEngine.ScriptComponent;

import java.util.Map;

/**
 * Used to display Colliders
 */
public class ColliderView extends ComponentView {





    // WORK AROUND BEGIN
    // workaround to initialize collider since super must be first instruction
    private static Collider collider;
    private static PhysicsComponent init(PhysicsComponent phys, Collider collider){
        ColliderView.collider = collider;
        return phys;
    }
    // WORK AROUND END

    public ColliderView(PhysicsComponent phys, Collider col, Skin skin) {
        super(init(phys, col),skin);
        collider = col;
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
        Map<String, String> map = collider.toMap();
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

    }
}
