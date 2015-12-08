package com.brm.GoatEngine.LevelEditor.Commands;

import com.brm.GoatEngine.GoatEngine;

/**
 * Saves the editor to a command
 */
public class SaveLevelCommand extends EditorCommand {
    /**
     * Executes the logic of the command
     */
    @Override
    public void exec() {
        GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager().saveIni("H:/tmp/out.ini");
    }
}
