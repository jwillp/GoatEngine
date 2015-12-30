package com.goatgames.goatengine.ai.components;


import com.goatgames.goatengine.ai.pathfinding.PathNode;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.utils.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Component to make to Store AI information on an entity
 */
public class AIComponent extends EntityComponent {
    public static final String ID = "AI_COMPONENT"; // TODO CHANGE THIS

    private Timer reactionTime = new Timer(5); //The delay between AI logic updates

    private Hashtable<String, Object> blackboard = new Hashtable<String, Object>();

    private ArrayList<PathNode> currentPath = new ArrayList<PathNode>(); //Current pathfinding path (if any)


    public AIComponent(){
        super(true);
        reactionTime.start();
    }

    public AIComponent(Map<String, String> map) {
        super(map);
    }

    /**
     * Constructs a PODType, to be implemented by subclasses
     *
     * @return
     */
    @Override
    protected Map<String, String> makeMap() {
        return new HashMap<String, String>();
    }

    /**
     * Builds the current object from a pod representation
     *
     * @param pod the pod representation to use
     */
    @Override
    protected void makeFromMap(Map<String, String> pod) {

    }

    @Override
    public String getId() {
        return ID;
    }


    public Timer getReactionTime() {
        return reactionTime;
    }

    public Hashtable<String, Object> getBlackboard() {
        return blackboard;
    }

    public ArrayList<PathNode> getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(ArrayList<PathNode> currentPath) {
        this.currentPath = currentPath;
    }
}