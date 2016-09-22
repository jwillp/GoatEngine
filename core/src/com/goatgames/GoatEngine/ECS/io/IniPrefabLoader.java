package com.goatgames.goatengine.ecs.io;

import com.goatgames.gdk.GAssert;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;
import com.goatgames.gdk.io.IFileHandle;
import org.ini4j.Ini;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class loading prefab definitions from Ini configuration files
 */
public class IniPrefabLoader implements IPrefabLoader {

    @Override
    public Prefab load(String pathToPrefab) {
        IFileHandle prefabHandle = GoatEngine.fileManager.getFileHandle(pathToPrefab);
        Prefab prefab = new Prefab();

        if (GAssert.that(prefabHandle.exists(), String.format("Prefab: %s does not exist", pathToPrefab))) {
            try {
                Ini ini = new Ini(prefabHandle.getFile());
                Map<String, NormalisedEntityComponent> comps = getComponents(ini);
                prefab.setComponents(comps);
            } catch (IOException e) {
                GoatEngine.logger.error(e.getMessage());
                GoatEngine.logger.error(e);
            }
        }
        /**
         * Note, if there was a problem loading the prefab, an empty prefab will be returned.
         * Therefore the engine should not crash, but unexpected behaviours might occur.
         */
        return prefab;
    }

    /**
     * Returns list of component as Maps as read in the prefab file
     *
     * @param ini ini object
     * @return entity component maps
     */
    private Map<String, NormalisedEntityComponent> getComponents(Ini ini) {
        Map<String, NormalisedEntityComponent> comps = new HashMap<>();

        for (String componentKey : ini.keySet()) {
            NormalisedEntityComponent map = new NormalisedEntityComponent();
            // fetch values for string substitution
            for (String key : ini.get(componentKey).keySet()) {
                map.put(key, ini.fetch(componentKey, key));
            }
            map.put("component_id", componentKey.toUpperCase());
            comps.put(componentKey, map);
        }

        return comps;
    }
}
