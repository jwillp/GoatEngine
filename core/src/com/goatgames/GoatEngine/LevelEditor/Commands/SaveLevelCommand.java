package com.goatgames.goatengine.leveleditor.commands;

import com.badlogic.gdx.Gdx;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.leveleditor.LevelEditor;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter;
import com.kotcrab.vis.ui.util.dialog.OptionDialogListener;

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
                level = String.format("data/levels/%s.gel", level);

                // If File exist show confirmation of overwrite
                if(Gdx.files.internal(level).exists()){
                   /* DialogUtils.OptionDialog showOptionDialog (editor.getView().getStage(),
                            "Warning Overwrite", "",
                            OptionDialogType type, final OptionDialogListener listener)*/
                    final String finalLevel = level;
                    DialogUtils.showOptionDialog(editor.getView().getStage(),
                            "Warning Overwrite",
                            String.format("The level file %s already exists and will be OVERWRITTEN \n" +
                                    " Do you want to proceed ?", level),
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
        GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager().saveLevel(file);
        DialogUtils.showOKDialog(
                editor.getView().getStage(),
                "Success",
                String.format("The level has been saved at  %s", file));
    }
}
