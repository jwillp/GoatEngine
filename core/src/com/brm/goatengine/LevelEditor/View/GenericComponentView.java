package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.brm.GoatEngine.Utils.Logger;

import java.lang.reflect.Field;

/**
 * A Component View using reflexion to display fields
 */
public class GenericComponentView extends ComponentView {

    public GenericComponentView(EntityComponent c, Skin skin) {
        super(c, skin);
    }

    @Override
    protected void initContent() {
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
                    addStringField(field.getName(), field.get(component).toString());
                }
            } catch (IllegalAccessException e) {
                Logger.error(e.getMessage());
                Logger.logStackTrace(e);
            }
        }
    }

    @Override
    protected void onApply() {


    }


}
