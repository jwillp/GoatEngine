package com.brm.GoatEngine.TmxSupport;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.brm.GoatEngine.GEConfig;
import com.brm.GoatEngine.Utils.Logger;

import java.util.ArrayList;

/**
 * USed to create entities of the current screen by Reading a Tmx File
 */
public class MapConfig {

    private String tmxFile;
    private TiledMap tiledMap;

    /**
     * Ctor
     * @param tmxFile path to the map config file
     */
    public MapConfig(String tmxFile){
        this.tmxFile = tmxFile;
    }

    /**
     * Reads all the map config objects and returns them
     * @return ArrayList or Map Object
     */
    public ArrayList<MapConfigObject> read(){

        try{
            tiledMap = new TmxMapLoader().load(tmxFile);
        }catch (NullPointerException e){
            MapConfigFileNotFoundException exception = new MapConfigFileNotFoundException(this.tmxFile);
            Logger.fatal(exception.getMessage());
            if(GEConfig.DevGeneral.DEV_CTX){
                Logger.logStackTrace(exception);
            }
            throw exception;
        }
        ArrayList<MapConfigObject> objects = new ArrayList<MapConfigObject>();
        float tileSize = tiledMap.getProperties().get("tilewidth", Integer.class);
        MapLayer objectLayer = tiledMap.getLayers().get("objects");
        if(objectLayer == null){
            return objects;
        }
        MapObjects mapObjects = objectLayer.getObjects();

        for(int i=0; i<mapObjects.getCount(); i++){
            RectangleMapObject obj = (RectangleMapObject) mapObjects.get(i); //The object read (rectangle)
            Rectangle rect = obj.getRectangle();    //The rect (width height ...)

            MapConfigObject object = new MapConfigObject();

            object.tag = (String) obj.getProperties().get("tag");
            object.script = (String) obj.getProperties().get("script");
            object.position = new Vector2(rect.getX()/tileSize, rect.getY()/tileSize);
            object.width = rect.getWidth()/tileSize;
            object.height = rect.getHeight()/tileSize;

            objects.add(object);
        }

        return objects;
    }


    class MapConfigFileNotFoundException extends RuntimeException{
        MapConfigFileNotFoundException(final String file){
            super("Map config file not found: " + file);
        }
    }

}
