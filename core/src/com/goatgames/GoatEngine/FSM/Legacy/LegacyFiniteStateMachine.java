package com.goatgames.goatengine.fsm.Legacy;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * A Data Driven State Machine
 */
public class LegacyFiniteStateMachine {

    private ArrayList<LegacyMachineState> states = new ArrayList<LegacyMachineState>();
    private LegacyMachineState currentState;

    public LegacyFiniteStateMachine(){

    }

    /**
     * Adds a state to the machine
     * @param s the state to add
     */
    public void addState(LegacyMachineState s) {
        this.states.add(s);
    }

    /**
     * Returns the states
     * @return a list of the states
     */
    public ArrayList<LegacyMachineState> getStates() {
        return states;
    }


    /**
     * Saves the machine to XML
     * @param filePath
     */
    public void saveToXML(String filePath) throws IOException {
        StringWriter writer = new StringWriter();
        XmlWriter xml = new XmlWriter(writer);
        xml.element("TestMachine");
        for(LegacyMachineState state: this.states){
            state.writeXml(xml);
        }
        xml.pop();
        //Write Xml
        FileOutputStream out = new FileOutputStream(filePath);
        out.write(writer.toString().getBytes());

    }

    /**
     * loads the finite state machine from an xml definition
     * @param filePath
     * @throws IOException
     */
    public void loadFromXml(String filePath) throws IOException {
        XmlReader reader = new XmlReader();
        Element root = reader.parse(Gdx.files.internal(filePath));
        Array<Element> states = root.getChildrenByName("State");

        for(Element state : states){
            this.addState(new LegacyMachineState(state));
        }
    }
}
