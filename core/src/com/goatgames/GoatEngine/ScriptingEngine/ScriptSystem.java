package com.goatgames.goatengine.scriptingengine;

import com.goatgames.gdk.GAssert;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.scriptingengine.common.IEntityScript;
import com.goatgames.goatengine.scriptingengine.common.IScriptLoader;

/**
 * Entity System managing entity scripts as strings and converting to them EntityScript instances
 * using the a ScriptLoader. Adding a new language is therefore simply a matter of creating a script loader and
 * an implementation of IEntityScript
 */
public class ScriptSystem extends EntitySystem {

    IScriptLoader scriptLoader;

    public ScriptSystem(){
        scriptLoader = GoatEngine.scriptingEngine.getLoader(); /* TODO As GEImplSpecs param */
    }

    /**
     * Used to initialise the system
     */
    @Override
    public void init() {
        GAssert.notNull(scriptLoader, "No Script loader found");
    }

    /**
     * Called once per game frame
     *
     * @param dt
     */
    @Override
    public void update(float dt) {
        if(scriptLoader == null) return;
        for(Entity entity: getEntityManager().getEntitiesWithComponent(ScriptComponent.ID)){
            ScriptComponent scriptComponent;
            scriptComponent = (ScriptComponent) entity.getComponent(ScriptComponent.ID);
            if(!entity.hasComponent(EntityScriptComponent.ID)){
                GoatEngine.logger.warn("Entity does not have an EntityScriptComponent, an instance will automatically be added");
                entity.addComponent(new EntityScriptComponent(true), EntityScriptComponent.ID);
            }

            EntityScriptComponent entityScriptComp = (EntityScriptComponent)entity.getComponent(EntityScriptComponent.ID);

            // Add missing script instances
            for(String scriptFile: scriptComponent.getScripts()){
                if(!entityScriptComp.hasScriptWithName(scriptFile)){
                    IEntityScript script = scriptLoader.load(scriptFile);
                    if(GAssert.notNull(script, "script == null")) {
                        entityScriptComp.addScript(script);
                    }
                }
            }

            // Remove unused scripts
            for(String scriptToRemove: scriptComponent.getScriptsToRemove()){
                entityScriptComp.removeScriptByName(scriptToRemove);
                scriptComponent.getScriptsToRemove().remove(scriptToRemove);
            }
            getEntityManager().freeEntityObject(entity);
        }
    }
}

