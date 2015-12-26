package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.brm.GoatEngine.ECS.common.TagsComponent;
import com.brm.GoatEngine.ECS.core.EntityComponent;

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
