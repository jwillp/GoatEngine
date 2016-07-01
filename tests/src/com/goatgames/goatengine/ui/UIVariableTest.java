package com.goatgames.goatengine.ui;

import org.junit.Test;

import static org.junit.Assert.*;

public class UIVariableTest {

    @Test
    public void testFindType() throws Exception {
        UIVariable var;

        var = new UIVariable(true);
        assertTrue(var.findType() == UIVariable.VariableType.BOOLEAN);

        var = new UIVariable("String");
        assertTrue(var.findType() == UIVariable.VariableType.STRING);

        var = new UIVariable(10);
        assertTrue(var.findType() == UIVariable.VariableType.INT);

    }

    @Test
    public void testSetValue() throws Exception {


        // Set Value of non constant
        UIVariable var = new UIVariable(10);
        assertTrue(var.getInt() == 10);

        var.setValue("22");
        assertTrue(var.getInt() == 22);

        // Set Value of Constant
        UIVariable constant = new UIVariable(23, true);
        assertTrue(constant.getInt() == 23);

        constant.setValue("56"); // Cannot set value of constant
        assertFalse(constant.getInt() == 56);

    }
}
