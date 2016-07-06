package com.goatgames.goatengine.desktop;

import com.goatgames.goatengine.DefaultGEImplSpecs;
import com.goatgames.goatengine.desktop.input.DesktopInputManager;
import com.goatgames.goatengine.input.InputManager;

/**
 * Desktop default specs
 */
public class DesktopGEImplSpecs extends DefaultGEImplSpecs {

    @Override
    public InputManager getInputManager() {
        return new DesktopInputManager();
    }
}
