package com.goatgames.goatengine.graphicsrendering.camera;

/**
 * returns a strategy according to a setting's input
 */
public class CameraStrategyResolver {

    public  static CameraStrategy getStrategy(String data){

        if(data.startsWith("FixedHeight")){
            int nbUnits = Integer.parseInt(data.replace("FixedHeight(","").replace(")", ""));
            return new FixedHeightStrategy(nbUnits);
        }

        if(data.startsWith("FixedWidth")){
            int nbUnits = Integer.parseInt(data.replace("FixedWidth(","").replace(")", ""));
            return new FixedWidthStrategy(nbUnits);
        }

        if(data.startsWith("Fixed(")){
            String widthHeight = data.replace("Fixed(","").replace(")","");
            String[] split = widthHeight.split(",");
            return new FixedStrategy(Integer.parseInt(split[0]),Integer.parseInt(split[1]));
        }

        return null; // TODO Exception?
    }
}
