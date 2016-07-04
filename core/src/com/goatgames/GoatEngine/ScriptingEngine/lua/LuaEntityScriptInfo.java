package com.goatgames.goatengine.scriptingengine.lua;

/**
 * Represents info about a script (last edit date, path etc.)
 */
public class LuaEntityScriptInfo {

    private long lastModified = 0;
    private boolean valid;

    LuaEntityScriptInfo(long lastModified) {
        this.lastModified = lastModified;
        this.valid = true;
    }

    /**
     * Returns the last time the script was modified
     * @return the last modified time
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * Sets the last time the script was modified
     * @param lastModified time at which the script was last modified
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Sets the validity of the script
     * @param valid
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Indicates whether the script has errors or not
     * @return true if valid, otherwise false
     */
    public boolean isValid() {
        return valid;
    }
}
