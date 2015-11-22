package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.brm.GoatEngine.ECS.core.Entity;

/**
 * Entity Inspector
 */
public class EntityInspector extends Window {

    Table root;
    Label entityId;
    TextButton btnAddComponent;
    TextField label;
    Table componentList;

    public EntityInspector(Skin skin) {
        super("Entity Inspector", skin);

        initRootLayout();

    }


    private void initRootLayout(){
        top().left();
        root = new Table(this.getSkin());
        root.setDebug(this.getDebug());
        add(root).fill().expandX().padTop(5);

        root.top().center();
        root.defaults().top().left().expandX();
        entityId = new Label("845-587", this.getSkin());
        root.add("Entity ID").padRight(10);
        root.add(entityId);
        root.row().padBottom(10);

        label = new TextField("Entity label", this.getSkin());
        root.add("Label").padRight(10).padBottom(5);
        root.add(label);
        root.row().padBottom(10);

        btnAddComponent = new TextButton(" + ", this.getSkin());
        root.add("Component ").padRight(10).padBottom(5);
        root.add(btnAddComponent);
        root.row().padBottom(10);

        // Component List
        root.add("Components").colspan(2).padTop(5);
        root.row();
        componentList = new Table(getSkin());
        componentList.left().top();
        componentList.setDebug(this.getDebug());
        componentList.padRight(20);

        for(int i = 0; i < 20; i++){
            String value = Integer.toString(i);
            //componentList.add("PhysicsComponent " + value);
            componentList.add(new ComponentView(value, this.getSkin())).fill().expandX();
            componentList.row().padBottom(5).padTop(5);
        }
        //componentList.setFillParent(true);
        ScrollPane scrollPane = new ScrollPane(componentList, getSkin());
        scrollPane.setFadeScrollBars(false);
        root.add(scrollPane).colspan(2).expandY().fill();
    }


    public void inspectEntity(Entity e){

    }



}
