package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.brm.GoatEngine.LevelEditor.Components.EditorLabelComponent;

import java.util.HashMap;

/**
 * Entity Inspector
 */
public class EntityInspector extends Window {

    Table root;
    Label entityId;
    ImageButton btnAddComponent;
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
        entityId = new Label("N/A", this.getSkin());
        root.add("Entity ID").padRight(10);
        root.add(entityId);
        root.row().padBottom(10);

        label = new TextField("Entity label", this.getSkin());
        root.add("Label").padRight(10).padBottom(5);
        root.add(label);
        root.row().padBottom(10);

        btnAddComponent = new ImageButton(this.getSkin(), "plus");


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

        ScrollPane scrollPane = new ScrollPane(componentList, getSkin());
        scrollPane.setFadeScrollBars(false);
        root.add(scrollPane).colspan(2).expandY().fill();
    }


    public void inspectEntity(Entity e){
        // Trim the id at nth char and replace with dots
        String Id = e.getID().substring(0, Math.min(e.getID().length(), 8)).concat(" ...");
        this.entityId.setText(Id);
        String labelText = ((EditorLabelComponent)e.getComponent(EditorLabelComponent.ID)).getLabel();
        this.label.setText(labelText);


        // Components
        componentList.clearChildren();

        HashMap<String, EntityComponent> comps = e.getComponents();
        for(String cId :  comps.keySet()){
            addComponentToList(comps.get(cId));
        }

        this.setVisible(true);
    }


    public void addComponentToList(EntityComponent c){
        componentList.add(new ComponentView(c, this.getSkin())).fill().expandX();
        componentList.row().padBottom(5).padTop(5);
    }


    public void clear(){
        this.setVisible(false);
    }



}
