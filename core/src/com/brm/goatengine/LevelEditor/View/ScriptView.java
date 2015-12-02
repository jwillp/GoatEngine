package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.brm.GoatEngine.ScriptingEngine.ScriptComponent;

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


    public ScriptView(ScriptComponent scriptComponent, String scriptName, Skin skin) {
        super(init(scriptComponent, scriptName), skin);

        String name = findScriptSimpleName(scriptName);
        name = name.replace(".groovy", "");
        checkBoxEnable.setText(name + " (Script)");
    }





    @Override
    protected void initContent() {
        addString("Name", findScriptSimpleName(scriptName));
    }





    private String findScriptSimpleName(String fullName){
        Path p = Paths.get(fullName);
        String simpleName = p.getFileName().toString();
        // deCamelCasealize
        simpleName = simpleName.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
        return simpleName;
    }
}
