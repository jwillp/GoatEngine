package com.goatgames.goatengine.ui;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

public class GUTLInterpreterTest {

    @Test
    public void testSingleMacros() throws Exception {
        GUTLInterpreter interpreter = new GUTLInterpreter(new UIContext());

        assertTrue(Objects.equals("3", interpreter.evaluate("@max(2,3)")));
        assertTrue(Objects.equals("2", interpreter.evaluate("@max(2,1)")));


        // This should trigger an syntax error
        assertFalse(Objects.equals("45", interpreter.evaluate("@max(@max(0,1),@max(2,45)")));
    }

    @Test
    public void testExpressionWithBlanks() throws Exception {
        GUTLInterpreter interpreter = new GUTLInterpreter(new UIContext());
        assertTrue(Objects.equals("200", interpreter.evaluate("@max(0,    200)  ")));  // evaluate with spaces

        // Blank separated atoms should not work (expected comma)
        //assertFalse(Objects.equals("200", interpreter.evaluate("@max(0,    2 00)  ")));  // evaluate with spaces
    }


    @Test
    public void testNestedMacros() throws Exception {
        GUTLInterpreter interpreter = new GUTLInterpreter(new UIContext());

        assertTrue(Objects.equals("54", interpreter.evaluate("@max(@min(0,2), @abs(-54))")));
        assertTrue(Objects.equals("47", interpreter.evaluate("@max(@max(0,1),@max(2,47))")));
    }



}
