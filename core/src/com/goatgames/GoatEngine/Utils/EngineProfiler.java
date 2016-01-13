package com.goatgames.goatengine.utils;

import com.goatgames.goatengine.eventmanager.Event;
import com.goatgames.goatengine.eventmanager.GameEventListener;
import com.goatgames.goatengine.eventmanager.engineevents.EngineEvents;

/**
 * Logs the time of events in game engine
 */
public class EngineProfiler implements GameEventListener {

    // Performance profiling
    private static Timer performanceTimer = new Timer();
    private static long lastLogicTime = 0;
    private static long lastRenderTime = 0;



    public EngineProfiler(){
        performanceTimer.start();
    }


    @Override
    public void onEvent(Event e) {
        if(e instanceof EngineEvents.GameTickBeginEvent){
            performanceTimer.reset();
            lastLogicTime = 0;
            lastRenderTime = 0;
            return;
        }

        if(e instanceof EngineEvents.GameTickEndEvent){
            if(performanceTimer.getDeltaTime() > 16){
                Logger.warn("Tick longer than a frame: " + performanceTimer.getDeltaTime() + "ms" +
                        " logic: " + lastLogicTime + "ms render: " + lastRenderTime + "ms");
            }
            return;
        }



        if(e instanceof EngineEvents.LogicTickEndEvent){
            lastLogicTime = performanceTimer.getDeltaTime();
        }

        if(e instanceof EngineEvents.RenderTickEndEvent){
            lastRenderTime = performanceTimer.getDeltaTime() - lastLogicTime;
        }
    }











}
