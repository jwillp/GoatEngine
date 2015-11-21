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
        root.row();

        label = new TextField("Entity label", this.getSkin());
        root.add("Label").padRight(10);
        root.add(label);
        root.row();

        btnAddComponent = new TextButton(" + ", this.getSkin());
        root.add("Component ").padRight(10);
        root.add(btnAddComponent);
        root.row();

        // Component List
        root.add("Components").colspan(2).padTop(5);
        root.row();
        componentList = new Table();
        componentList.left();
        componentList.setDebug(true);
        componentList.padRight(20);
        for(int i = 0; i < 10; i++){
            String value = Integer.toString(i);
            //componentList.addActor(new Label("PhysicsComponent " + value, this.getSkin()));
            componentList.add(new ComponentView(value, this.getSkin())).fill().expandX();
            componentList.row().padBottom(10);
        }
        //componentList.setFillParent(true);
        root.add(new ScrollPane(componentList, getSkin())).colspan(2).expandY().fill();
    }


    public void inspectEntity(Entity e){

    }



}
