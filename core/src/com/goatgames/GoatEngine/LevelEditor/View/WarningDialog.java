package com.goatgames.goatengine.leveleditor.view;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Warning with a Ok button
 */
public class WarningDialog extends Dialog {
    public WarningDialog(String text, Skin skin) {
        super("Goat Engine", skin);
        this.text(text);
        this.button("Ok").key(Input.Keys.ENTER,true);
    }
}
