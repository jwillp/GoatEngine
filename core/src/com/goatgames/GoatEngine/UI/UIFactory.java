package com.goatgames.goatengine.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

/**
 * Class responsible for creating gui elements from Lua definitions
 */
public class UIFactory {

    /**
     * Will create the appropriate type of button depending on the
     * definition
     * @return a constructed Button
     */
    public static Button createButton(LuaTable btnTable, Table parent){
        Button btn = null;
        if(btnTable.get("text") != LuaValue.NIL)
            btn = new TextButton(btnTable.get("text").toString(), parent.getSkin());

        final LuaValue v = btnTable.get("onClick");
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                v.call();
            }
        });

        return btn;
    }

}
