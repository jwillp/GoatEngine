package com.goatgames.goatengine.scriptingengine;

import com.goatgames.goatengine.eventmanager.GameEvent;

/**
 * Event triggered when we need to reload a script
 */
public class ReloadScriptEvent extends GameEvent {

    private final String scriptName;

    public ReloadScriptEvent(String scriptName){
        this.scriptName = scriptName;
    }

    public String getScriptName() {
        return scriptName;
    }
}
