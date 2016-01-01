package com.goatgames.goatengine.leveleditor.commands;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.graphicsrendering.CameraComponent;
import com.goatgames.goatengine.leveleditor.components.EditorLabelComponent;
import com.goatgames.goatengine.leveleditor.LevelEditor;
import com.goatgames.goatengine.utils.Logger;

/**
 * Command "trying" to select an entity at a given position
 */
public class SelectEntityAtPositionCommand extends EditorCommand {

    // Mouse position on screen
    private final int screenX;
    private final int screenY;
    private LevelEditor editor;

    public SelectEntityAtPositionCommand(int screenX, int screenY, LevelEditor editor){

        this.screenX = screenX;
        this.screenY = screenY;
        this.editor = editor;
    }


    /**
     * Executes the logic of the command
     */
    @Override
    public void exec() {
        String entityId = findEntity();
        if(entityId != null) {
            //Make sure it is registered
            Entity entity = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager().getEntityObject(entityId);
            selectEntity(entity);
        }else{
            editor.getView().getStage().setScrollFocus(null);
            editor.setSelectedEntity(null);
        }
    }

    /**
     * Selects the entity in the editor && inspect
     */
    private void selectEntity(Entity entity){
        Logger.info("Level Editor: entity selected :" + entity.getID());
        editor.setSelectedEntity(entity);

        // By default entities don't have an editor label component
        // so we'll check if we need to add one before any inspection can occur
        if(!entity.hasComponent(EditorLabelComponent.ID)){
            entity.addComponent(new EditorLabelComponent(""), EditorLabelComponent.ID);
        }
    }

    /**
     * Tries to find an entity at the position of the mouse
     */
    private String findEntity(){
        EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
        CameraComponent cam = (CameraComponent) manager.getComponents(CameraComponent.ID).get(0);
        final Vector3 pos = new Vector3();
        // Translate the mouse coordinates to world coordinates
        cam.getCamera().unproject(pos.set(screenX, screenY, 0));

        // Ask the world which bodies are within the given
        // Bounding box around the mouse pointer
        World world = GoatEngine.gameScreenManager.getCurrentScreen().getPhysicsSystem().getWorld();
        final Body[] hitBody = {null};
        float mousePointerSize =  0.0001f;
        world.QueryAABB(new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                if(fixture != null) {
                    hitBody[0] = fixture.getBody();
                    return true;
                }
                return false;
            }
        }, pos.x - mousePointerSize, pos.y - mousePointerSize, pos.x + mousePointerSize, pos.y + mousePointerSize);
        // If we hit something
        String entityId = null;
        if (hitBody[0] != null) {
            entityId = (String) hitBody[0].getUserData();
        }
        return entityId;
    }






}
