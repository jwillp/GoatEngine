package com.goatgames.goatengine.leveleditor.view;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.goatgames.goatengine.scriptingengine.groovy.ScriptComponent;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Used to display a script (not a script component but a simple script)
 * in the future it could use reflexion on scripts to add fields
 * (fields with annotation EditorDisplayable for ex)
 */
public class ScriptView extends ComponentView {

    // WORK AROUND BEGIN
    // workaround since super must be first instruction
    private static String scriptName;
    private static ScriptComponent init(ScriptComponent scriptComponent, String scriptName){
        ScriptView.scriptName = scriptName;
        return scriptComponent;
    }
    // WORK AROUND END

    int scriptIndex;


    public ScriptView(ScriptComponent scriptComponent, String scriptName, Skin skin) {
        super(init(scriptComponent, scriptName), skin);

        String name = findScriptSimpleName(scriptName);
        name = name.replace(".groovy", "");
        checkBoxEnable.setText(name + " (Script)");

        // Find script ID
        final int scriptCount = scriptComponent.getScripts().size();
        for(int i=0; i<scriptCount; i++)
        if(scriptComponent.getScripts().get(i).equals(scriptName)){
            scriptIndex = i;break;
        }
    }


    @Override
    protected void initContent() {
        addStringField("Name", findScriptSimpleName(scriptName));
    }

    @Override
    protected void onApply(){
        ScriptComponent scriptComponent = (ScriptComponent) component;
        scriptComponent.getScripts().set(scriptIndex, stringFields.get("Name").getText());

    }


    private String findScriptSimpleName(String fullName){
        Path p = Paths.get(fullName);
        String simpleName = p.getFileName().toString();
        // deCamelCasealize
        simpleName = simpleName.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
        return simpleName;
    }
}
