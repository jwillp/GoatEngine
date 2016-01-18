package com.goatgames.goatengine.scriptingengine.lua;

import com.badlogic.gdx.utils.Array;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.graphicsrendering.CameraComponent;
import com.goatgames.goatengine.screenmanager.GameScreen;
import com.goatgames.goatengine.utils.GAssert;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

/**
 * Ctx API
 */
public class Ctx extends TwoArgFunction {

    private final LuaValue luaEntity;
    private final String scriptName;

    public Ctx(Entity entity, String scriptName) {
        this.luaEntity = CoerceJavaToLua.coerce(entity);
        this.scriptName = scriptName;

        GAssert.notNull(luaEntity, "Entity was null cannot be added to Ctx");
    }

    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("entity", luaEntity);
        library.set("scriptName", scriptName);

        GameScreen currentGameScreen = GoatEngine.gameScreenManager.getCurrentScreen();
        library.set("gameScreen",
                CoerceJavaToLua.coerce(currentGameScreen));

        library.set("entityManager",
                CoerceJavaToLua.coerce(currentGameScreen.getEntityManager()));

        library.set("physicsWorld", CoerceJavaToLua.coerce(currentGameScreen.getPhysicsSystem().getWorld()));


        library.set("camera", new CameraAPI());




        env.set("ctx", library);
        env.get("package").get("loaded").set("ctx", library);
        return library;
    }

    private class CameraAPI extends OneArgFunction{
        @Override
        public LuaValue call(LuaValue arg) {
            Array<EntityComponent> comps = GoatEngine.gameScreenManager.getCurrentScreen()
                    .getEntityManager().getComponents(CameraComponent.ID);
            CameraComponent camComp = (CameraComponent) comps.get(0);
            GAssert.notNull(camComp, "Camera Component was null, cannot create Lua Library Ctx");
            return CoerceJavaToLua.coerce(camComp.getCamera());
        }
    }
}
