package com.goatgames.goatengine.scriptingengine;

import com.goatgames.goatengine.eventmanager.Event;

/**
 * Event triggered when we need to reload a script
 */
public class ReloadScriptEvent extends Event {

    private final String scriptName;

    public ReloadScriptEvent(String scriptName){
        this.scriptName = scriptName;
    }

    public String getScriptName() {
        return scriptName;
    }
}
