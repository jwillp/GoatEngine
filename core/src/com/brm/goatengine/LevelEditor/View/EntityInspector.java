package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.brm.GoatEngine.ECS.common.PhysicsComponent;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.brm.GoatEngine.GraphicsRendering.CameraComponent;
import com.brm.GoatEngine.LevelEditor.Components.EditorLabelComponent;
import com.brm.GoatEngine.ScriptingEngine.ScriptComponent;

import java.util.HashMap;

/**
 * Entity Inspector
 */
public class EntityInspector extends Window {

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
        // Trim the id at nth char and replace with dots

        if(!e.getID().equals(entityId)){
            entityId = e.getID();
            String Id = e.getID().substring(0, Math.min(e.getID().length(), 8)).concat(" ...");
            this.lblEntityID.setText(Id);
            String labelText = ((EditorLabelComponent)e.getComponent(EditorLabelComponent.ID)).getLabel();
            this.label.setText(labelText);

            // Components
            componentList.clearChildren();

            HashMap<String, EntityComponent> comps = e.getComponents();
            for(String cId :  comps.keySet()){
                addComponentToList(comps.get(cId));
            }

        }
        // TODO Update componentViews
        this.setVisible(true);
    }




    public void addComponentToList(EntityComponent c){
        ComponentView componentView = null;

        // Special cases
        if(c.getId().equals(PhysicsComponent.ID)){
            componentView = new PhysicsComponentView(c, getSkin());
        }
        else if(c.getId().equals(ScriptComponent.ID)){
            ScriptComponent scriptComponent = (ScriptComponent) c;
            // For each script add a ScriptView
            for(String s : scriptComponent.getScripts()){
                componentList.add(new ScriptView(scriptComponent, s, getSkin())).fill().expandX();
                componentList.row().padBottom(5).padTop(5);
            }
        }
        else if(c.getId().equals(CameraComponent.ID)){
            componentView = new CameraComponentView(c,getSkin());
        }
        else if(c.getId().equals(EditorLabelComponent.ID)){
            // Do nothing it is already handled by one of the field in the header of inspector
            return;
        }else{
            componentView = new GenericComponentView(c, getSkin());
        }











        componentList.add(componentView).fill().expandX();
        componentList.row().padBottom(5).padTop(5);
    }


    public void clear(){
        this.setVisible(false);
    }



}
