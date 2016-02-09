package com.goatgames.goatengine.leveleditor.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.goatengine.physics.PhysicsComponent;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.leveleditor.components.EditorLabelComponent;
import com.goatgames.goatengine.physics.Collider;
import com.goatgames.goatengine.scriptingengine.ScriptComponent;

import java.util.HashMap;

/**
 * Entity Inspector
 */
public class EntityInspector extends Window {

    private boolean dirty = false;

    Table root;
    Label lblEntityID;
    TextButton btnAddComponent;
    TextField label;
    Table componentList;

    String entityId;

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
        lblEntityID = new Label("N/A", this.getSkin());
        root.add("Entity ID").padRight(10);
        root.add(lblEntityID);
        root.row().padBottom(10);

        label = new TextField("Entity label", this.getSkin());
        root.add("Label").padRight(10).padBottom(5);
        root.add(label);
        root.row().padBottom(10);

        btnAddComponent = new TextButton("+", this.getSkin());


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

        Color color = getColor();
        color.a = 0.5f;
        this.setColor(color);
    }


    public void inspectEntity(Entity e){
        // if selected entity is not the same as the last inspected, update
        if(!e.getID().equals(entityId) || this.dirty){
            entityId = e.getID();
            // Trim the id at nth char and replace with dots
            String Id = e.getID().substring(0, Math.min(e.getID().length(), 8)).concat(" ...");
            this.lblEntityID.setText(Id);
            String labelText = ((EditorLabelComponent)e.getComponent(EditorLabelComponent.ID)).getLabel();
            this.label.setText(labelText);

            // Components
            componentList.clearChildren();

            ObjectMap<String, EntityComponent> comps = e.getComponents();
            for(String cId :  comps.keys()){
                addComponentToList(e, comps.get(cId));
            }
            this.dirty = false;
        }
        // TODO Update componentViews
        this.setVisible(true);
    }

    public void setDirty(boolean isDirty){
        this.dirty = isDirty;
    }


    public void addComponentToList(Entity e, EntityComponent c){
        ComponentView componentView = null;

        // Special cases
        if(c.getId().equals(PhysicsComponent.ID)){
            componentView = new PhysicsComponentView(c, getSkin());
            // For each collider add a ColliderView
            PhysicsComponent phys = (PhysicsComponent) c;
            for(Collider col: phys.getColliders()){
                componentList.add(new ColliderView(phys, col, e, getSkin())).fill().expandX();
                componentList.row().padBottom(5).padTop(5);
            }
        }
        else if(c.getId().equals(ScriptComponent.ID)){
            ScriptComponent scriptComponent = (ScriptComponent) c;
            // For each script add a ScriptView
            for(String s : scriptComponent.getScripts()){
                componentList.add(new ScriptView(scriptComponent, s, getSkin())).fill().expandX();
                componentList.row().padBottom(5).padTop(5);
            }
        }
        else if(c.getId().equals(EditorLabelComponent.ID)){
            // Do nothing it is already handled by one of the field in the header of inspector
            return;
        }
        /*else if(c.getId().equals(TagsComponent.ID)){
            componentView = new TagsComponentView(c,getSkin());
        }*/
        else{
            componentView = new GenericComponentView(c, getSkin());
        }


        componentList.add(componentView).fill().expandX();
        componentList.row().padBottom(5).padTop(5);
    }


    public void clear(){
        this.setVisible(false);
    }



}
