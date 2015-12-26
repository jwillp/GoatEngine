package com.goatgames.goatengine.leveleditor.View;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.goatgames.goatengine.ecs.common.TagsComponent;
import com.goatgames.goatengine.ecs.core.EntityComponent;

import java.util.ArrayList;

/**
 * Tags Component View
 */
public class TagsComponentView extends ComponentView {


    public TagsComponentView(EntityComponent component, Skin skin) {
        super(component, skin);

    }

    @Override
    protected void initContent() {
        TagsComponent tagsComponent = (TagsComponent)component;
        ArrayView arrayView = new ArrayView(getSkin(), "tags", new ArrayList<String>(tagsComponent.getTags()));
        contentTable.add(arrayView).left().fillX().expandX();

    }

    @Override
    protected void onApply() {

    }
}
