package com.goatgames.goatengine.scriptingengine.nativescripts;

import com.goatgames.goatengine.scriptingengine.common.IEntityScript;

/**
 * Abstract Native Script class to be implemented by Native Scripts (Java)
 */
public abstract class NativeEntityScript implements IEntityScript {

    /**
     * Indicates whether or not the script is considered initialised or not
     */
    private boolean initialised;

    @Override
    public boolean isInitialised() {
        return initialised;
    }

    public void setInitialised(boolean initialised) {
        this.initialised = initialised;
    }

    @Override
    public String getName(){
        return this.getClass().getSimpleName();
    }
}
