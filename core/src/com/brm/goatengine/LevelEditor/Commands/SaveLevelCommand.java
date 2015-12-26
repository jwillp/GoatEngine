package com.brm.GoatEngine.LevelEditor.Commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.LevelEditor.LevelEditor;
import com.brm.GoatEngine.Utils.Logger;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter;
import com.kotcrab.vis.ui.util.dialog.OptionDialogListener;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

/**
 * Saves the editor to a command
 */
public class SaveLevelCommand extends EditorCommand {


    private final LevelEditor editor;

    public SaveLevelCommand(LevelEditor editor){
        this.editor = editor;
    }


    /**
     * Executes the logic of the command
     */
    @Override
    public void exec() {

        DialogUtils.showInputDialog(editor.getView().getStage(), "Enter level file name", "level name: ", new InputDialogAdapter() {
            @Override
            public void finished (String input) {
                String level = input.replace(".gel", "");
                level = "data/levels/" + level + ".gel";

                // If File exist show confirmation of overwrite
                if(Gdx.files.internal(level).exists()){
                   /* DialogUtils.OptionDialog showOptionDialog (editor.getView().getStage(),
                            "Warning Overwrite", "",
                            OptionDialogType type, final OptionDialogListener listener)*/
                    final String finalLevel = level;
                    DialogUtils.showOptionDialog(editor.getView().getStage(),
                            "Warning Overwrite",
                            "The level file " + level +" already exists and will be OVERWRITTEN \n " +
                                    "Do you want to proceed ?",
                            DialogUtils.OptionDialogType.YES_NO,
                            new OptionDialogListener() {
                                @Override
                                public void yes() {
                                    saveFile(finalLevel);
                                }

                                @Override
                                public void no() {abort();}

                                @Override
                                public void cancel() {abort();}
                            });
                }else {
                    saveFile(level);
                }
            }
        });
    }


    private void abort(){

    }
    private void saveFile(String file){
        Gdx.files.local(file).writeString("", false);
        GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager().saveIni(file);
        DialogUtils.showOKDialog(
                editor.getView().getStage(),
                "Success",
                "The level has been saved at  " + file);
    }
}
