package com.brm.GoatEngine.LevelEditor.Commands;

import com.brm.GoatEngine.GoatEngine;

/**
 * Toggle Play and pause states of the Game Screen Manager
 */
public class TogglePlayPauseCommand extends EditorCommand {

    /**
     * Executes the logic of the command
     */
    @Override
    public void exec() {
        if(GoatEngine.gameScreenManager.isRunning()){
            GoatEngine.gameScreenManager.pause();
        }else{
            GoatEngine.gameScreenManager.resume();
        }
    }
}
