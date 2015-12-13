package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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


    protected void initContent2() {
        // For each field try to generate a UI field
        Field[] fields = component.getClass().getDeclaredFields();
        for(int i=0; i<fields.length; i++){
            Field field = fields[i];
            field.setAccessible(true);

            // Special cases
            if(field.getName().equals("ID")){
                continue;
            }
            /*if(!field.getType().isPrimitive()){
                continue;
            }*/;

            try {
                if(field.getType().equals(boolean.class)){
                    addBooleanField(field.getName(), field.getBoolean(component));
                }else{
                    addStringReadOnly(field.getName(), field.get(component).toString());
                }
            } catch (IllegalAccessException e) {
                Logger.error(e.getMessage());
                Logger.logStackTrace(e);
            }
        }
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
                addStringField(key, value);
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

        component.fromMap(map);

    }


}
