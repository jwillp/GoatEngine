package com.brm.GoatEngine.AI.Components;


import com.badlogic.gdx.utils.XmlReader;
import com.brm.GoatEngine.AI.Pathfinding.PathNode;
import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.brm.GoatEngine.Utils.Timer;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Component to make to Store AI information on an entity
 */
public class AIComponent extends EntityComponent {
    public static final String ID = "KUBOTZ_AI_COMPONENT"; // TODO CHANGE THIS

    private Timer reactionTime = new Timer(5); //The delay between AI logic updates

    private Hashtable<String, Object> blackboard = new Hashtable<String, Object>();

    private ArrayList<PathNode> currentPath = new ArrayList<PathNode>(); //Current pathfinding path (if any)


    public AIComponent(){
        reactionTime.start();
    }

    /**
     * Desiralizes a component
     *
     * @param componentData the data as an XML element
     */
    public void deserialize(XmlReader.Element componentData) {

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