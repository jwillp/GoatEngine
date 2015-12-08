package com.brm.GoatEngine.ECS;

import com.badlogic.gdx.Gdx;
import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.brm.GoatEngine.ECS.core.EntityManager;
import com.brm.GoatEngine.Utils.Logger;
import com.brm.GoatEngine.Utils.PODType;
import org.ini4j.Ini;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Used to serialize entities in Ini format
 */
public class ECSIniSerializer {

    private String iniPath;
    private EntityManager entityManager;
    private Ini ini;
    private HashSet<String> entityIds = new HashSet<String>();

    public ECSIniSerializer(String iniPath, EntityManager entityManager){
        this.iniPath = iniPath;
        this.entityManager = entityManager;

        try {
            ini = new Ini(Gdx.files.internal(iniPath).file());
        } catch (IOException e) {
            Logger.error(e.getMessage());
            Logger.logStackTrace(e);
            e.printStackTrace();
        }
    }






    public void load(){


    }



    public void save(){
        entityIds.clear();
        entityIds.addAll(entityManager.getEntityIds());
        writeEntityIndex();
        for(String id: entityIds){
            ini.putComment("#", "Entity BEGIN");
            HashMap<String, EntityComponent> components = entityManager.getComponentsForEntity(id);
            for(EntityComponent component: components.values()){
                writeComponent(id, component);
            }
            ini.putComment("#", "Entity END");
        }
        try {
            ini.store();
        } catch (IOException e) {
            Logger.error(e.getMessage());
            Logger.logStackTrace(e);
            e.printStackTrace();
        }
    }


    /**
     * Writes the entity index to file.
     * The index is used to make a list of all
     * the entities described in the file.
     * This is used to retrieve the info about
     * each entity in an easy way
     */
    public void writeEntityIndex(){

        Object[] array = entityIds.toArray();

        for(int i=0; i<array.length; i++){
            String id = (String) array[i];
            ini.put("entity_index",String.valueOf(i),id);
        }
    }


    /**
     * Writes a component to INI
     * @param entityId
     * @param component
     */
    public void writeComponent(String entityId, EntityComponent component) {
        String secName = entityId + "/" + component.getId().toLowerCase();
        EntityComponent.EntityComponentPOD pod = component.toPODType();
        Field[] fields = pod.getClass().getFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String fieldName = field.getName();
            if (field.isAnnotationPresent(PODType.SerializeName.class)) {
                fieldName = field.getAnnotation(PODType.SerializeName.class).value();
            }
            try {
                ini.put(secName, fieldName, field.get(pod).toString());
            } catch (IllegalAccessException e) {
                Logger.error(e.getMessage());
                Logger.logStackTrace(e);
                e.printStackTrace();
            }
        }
    }





}
