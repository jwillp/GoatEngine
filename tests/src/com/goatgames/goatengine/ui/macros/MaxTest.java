package com.goatgames.goatengine.ui.macros;


import com.goatgames.goatengine.ui.UIVariable;

import static org.junit.Assert.assertTrue;

public class MaxTest {

    @org.junit.Test
    public void executeShouldReturnHighestPositive() throws Exception {
        Macro.Max m = new Macro.Max();

        // Positive + positive
        UIVariable v1 = new UIVariable(2);
        UIVariable v2 = new UIVariable(3);
        UIVariable result = m.execute(new UIVariable[]{v1, v2});

        assertTrue(3 == result.getInt());
    }

    @org.junit.Test
    public void executeShouldReturnHighestNegativeNumber() throws Exception {
        Macro.Max m = new Macro.Max();

        // Negative + negative
        assertTrue(-2 == m.execute(new UIVariable[]{
                new UIVariable(-2), new UIVariable(-3)
        }).getInt());
    }

    @org.junit.Test
    public void executeShouldReturnPositiveOverNegativeNumber() throws Exception{
        Macro.Max m = new Macro.Max();

        // Positive, negative
        assertTrue(2 == m.execute(new UIVariable[]{
                new UIVariable(2), new UIVariable(-3)
        }).getInt());
    }
}
