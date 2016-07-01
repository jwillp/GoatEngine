package com.goatgames.goatengine.ui.macros;

import com.goatgames.goatengine.ui.UIVariable;

import static org.junit.Assert.assertTrue;

public class MinTest {

    @org.junit.Test
    public void executeShouldReturnLowestPositive() throws Exception {
        Macro.Min m = new Macro.Min();

        // Positive + positive
        UIVariable v1 = new UIVariable(2);
        UIVariable v2 = new UIVariable(3);
        UIVariable result = m.execute(new UIVariable[]{v1, v2});

        assertTrue(2 == result.getInt());
    }

    @org.junit.Test
    public void executeShouldReturnLowestNegativeNumber() throws Exception {
        Macro.Min m = new Macro.Min();

        // Negative + negative
        assertTrue(-3 == m.execute(new UIVariable[]{
                new UIVariable(-2), new UIVariable(-3)
        }).getInt());
    }

    @org.junit.Test
    public void executeShouldReturnNegativeOverNegativeNumber() throws Exception{
        Macro.Min m = new Macro.Min();

        // Positive, negative
        assertTrue(-3 == m.execute(new UIVariable[]{
                new UIVariable(2), new UIVariable(-3)
        }).getInt());
    }
}
