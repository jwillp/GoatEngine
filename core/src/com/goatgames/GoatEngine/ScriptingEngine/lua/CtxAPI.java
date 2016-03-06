package com.goatgames.goatengine.scriptingengine.lua;

import com.badlogic.gdx.utils.Array;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.PrefabFactory;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.graphicsrendering.camera.CameraComponent;
import com.goatgames.goatengine.screenmanager.GameScreen;
import com.goatgames.goatengine.utils.GAssert;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

/**
 * CtxAPI API
 */
public class CtxAPI extends TwoArgFunction {

    private final LuaValue luaEntity;
    private final String scriptName;

    public CtxAPI(Entity entity, String scriptName) {
        this.luaEntity = CoerceJavaToLua.coerce(entity);
        this.scriptName = scriptName;

        GAssert.notNull(luaEntity, "Entity was null cannot be added to CtxAPI");
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


        library.set("create", new CreateEntityFromPrefabAPI());
        library.set("destroy", new DestroyEntityAPI());

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


    /**
     * Create an entity from a prefab
     */
    private class CreateEntityFromPrefabAPI extends OneArgFunction{

        @Override
        public LuaValue call(LuaValue arg) {
            String prefab = arg.toString();
            final String DATA_DIR = GoatEngine.config.getString("data_directory");
            if(!prefab.startsWith(DATA_DIR + "prefabs")){
                prefab = DATA_DIR + "prefabs/" + prefab;
            }
            return CoerceJavaToLua.coerce(new PrefabFactory().createEntity(prefab));
        }
    }

    private class CreateEntityAPI extends ZeroArgFunction{

        @Override
        public LuaValue call() {
            GameScreen currentGameScreen = GoatEngine.gameScreenManager.getCurrentScreen();
            return CoerceJavaToLua.coerce(currentGameScreen.getEntityManager().createEntity());
        }
    }


    private class DestroyEntityAPI extends OneArgFunction{

        @Override
        public LuaValue call(LuaValue arg) {
            GameScreen currentGameScreen = GoatEngine.gameScreenManager.getCurrentScreen();
            currentGameScreen.getEntityManager().deleteEntity(arg.toString());
            return null;
        }
    }

}
