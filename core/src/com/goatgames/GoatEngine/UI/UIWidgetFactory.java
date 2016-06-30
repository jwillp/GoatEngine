package com.goatgames.goatengine.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * Factory used to create Widgets using definitions
 */
public interface UIWidgetFactory {
    /**
     * Creates a widget from a definition
     */
    Widget createWidget(UIWidgetDefinition def);
}
