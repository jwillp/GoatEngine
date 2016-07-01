package com.goatgames.goatengine.ui;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

public class GUTLInterpreterTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testEvaluate() throws Exception {
        GUTLInterpreter interpreter = new GUTLInterpreter(new UIContext());


        //assertTrue(Objects.equals("3", interpreter.evaluate("@max(2,3)")));
       // assertTrue(Objects.equals("2", interpreter.evaluate("@max(2,1)")));

        assertTrue(Objects.equals("47", interpreter.evaluate("@max(@max(0,1),@max(2,47))")));

        // This should trigger an syntax error
        //assertFalse(Objects.equals("45", interpreter.evaluate("@max(@max(0,1),@max(2,45)")));


    }
}
