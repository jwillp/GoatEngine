package com.goatgames.goatengine.utils;

import com.goatgames.goatengine.utils.math.GameMath;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Timer Tests
 */
public class TimerTest {

    @Test
    public void testIsDone() throws Exception {
        final int delay = Timer.ONE_SECOND;
        Timer timer = new Timer(delay);
        long time = System.currentTimeMillis();
        while (!timer.isDone()){
            assertTrue(System.currentTimeMillis() - time <= delay);
        }
        assertTrue(System.currentTimeMillis() - time >= delay);
    }

    @Test
    public void testGetRunningTime() throws Exception {
        final int delay = 500;
        Timer timer = new Timer(delay);
        long start = System.currentTimeMillis();
        while (!timer.isDone()){
            long runTime = System.currentTimeMillis() - start;
            final long timerRunningTime = timer.getRunningTime();
            System.out.println("test rt: " + runTime);
            System.out.println("timer rt: " + timerRunningTime);
            final boolean test = GameMath.isMoreOrLess(timerRunningTime, runTime, 5);
            System.out.println("test: " + test);
            System.out.println(" ");
            assertTrue(test);
        }
        assertTrue(timer.getRunningTime() >= delay);
    }

    @Test
    public void testGetDeltaTime() throws Exception {

    }

    @Test
    public void testGetRemainingTime() throws Exception {
        final int delay = Timer.ONE_SECOND;
        Timer timer = new Timer(delay);
        long start = System.currentTimeMillis();
        while (!timer.isDone()){
            long end = start + delay;
            long remaining = end - System.currentTimeMillis();
            assertTrue(GameMath.isMoreOrLess(timer.getRemainingTime(), remaining, 5));
        }
        assertTrue(timer.getRemainingTime() == 0);
    }

    @Test
    public void testGetProgression() throws Exception {
        final int delay = Timer.ONE_SECOND;
        Timer timer = new Timer(delay);
        long time = System.currentTimeMillis();
        while (!timer.isDone()){
            assertTrue(timer.getProgression() < 1.0f);
        }
        assertTrue(timer.getProgression() == 1.0f);
    }
}
