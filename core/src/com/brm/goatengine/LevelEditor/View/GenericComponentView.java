package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.brm.GoatEngine.Utils.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * A Component View using reflexion to display fields
 */
public class GenericComponentView extends ComponentView {

    public GenericComponentView(EntityComponent c, Skin skin) {
        super(c, skin);
    }

    @Override
    protected void initContent() {
        Map<String, String> map = component.toMap();
        for(String key: map.keySet()){
            if(key.equals("component_id") || key.equals("enabled")){
                continue;
            }
            String value = map.get(key);
            if(value.equals("true") || value.equals("false")){
                addBooleanField(key, Boolean.parseBoolean(value));
            }else{
                TextField field = addStringField(key, value);
                /*field.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        onApply();
                    }
                });*/ // Auto Update
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

        // TODO array
        try{
            component.fromMap(map);
        }catch (RuntimeException e){
            new WarningDialog(e.getCause() + " " + e.getMessage(), getSkin()).show(this.getStage());
            e.printStackTrace();
        }

    }


}
