package com.goatgames.goatengine;

import com.badlogic.gdx.utils.LongArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.goatengine.utils.Timer;

/**
 * Class used for debugging.
 * Used to profile certain repetitive tasks
 * and get the following information:
 *     - the shortest time it took to execute the task
 *     - the longest time it took to execute the task (the peak)
 *     - the average time it takes to execute the given task
 *
 */
public class PerformanceProfiler {

    // Key Task name, value task instance
    ObjectMap<String,Task> tasks = new ObjectMap<>();

    public PerformanceProfiler(){}

    private Timer logTimer = new Timer(Timer.HALF_A_SECOND);


    /**
     * Starts the timing of a task
     */
    public void beginTask(String taskName){
        Task task = tasks.get(taskName, new Task());
        if(!tasks.containsValue(task,true)) tasks.put(taskName,task);
        task.begin();
    }

    /**
     * Ends the timing of a task
     */
    public void endTask(String taskName){
        Task task = tasks.get(taskName);
        long delta = task.end();
        task.recordTime(delta);
    }

    /**
     * Returns the statistics of a task
     */
    public String getStats(String taskName){
        Task  task = tasks.get(taskName);
        return String.format("[PERFO] - %s : Average: %sms | Shortest: %sms | Longuest %sms",
                taskName,
                task.getAverageTime(),
                task.getShortestTime(),
                task.getLongestTime()
        );
    }


    /**
     * Logs the performances of all tracked tasks
     * in the DEBUG log level.
     */
    public void logPerformances(){
        if(logTimer.isDone()){
           dumpPerformances();
           logTimer.reset();
        }
    }

    public void dumpPerformances() {
        for(String taskName: tasks.keys()){
            GoatEngine.logger.debug(getStats(taskName));
        }
    }


    /**
     * Stops trakcing a task
     */
    public void deleteTask(String taskName){
        tasks.remove(taskName);
    }

    class Task  {
        private LongArray recordedTimes = new LongArray();
        private Timer timer = new Timer(Timer.INFINITE);

        long longestTime = 0;
        long shortestTime = Long.MAX_VALUE;

        /**
         * Begins the timer of the task
         */
        public void begin(){
            timer.reset();
        }

        /**
         * Stops the timer and returns the time it took (delta time)
         */
        public long end(){
            return timer.getDeltaTime(); //Convert to ms
        }

        /**
         * Returns the longuest time for the current task
         */
        public long getLongestTime(){
            return longestTime;
        }

        /**
         * Returns the shortest time recorded for the current task
         */
        public long getShortestTime(){
            return shortestTime;
        }


        /**
         * Returns the average time it takes to execute task
         */
        public long getAverageTime(){
            long cumul = 0;
            for(long time : recordedTimes.items){
                cumul += time;
            }
            return cumul/recordedTimes.size;
        }

        /**
         * Records a new time for the current task
         */
        public void recordTime(long time){
            recordedTimes.add(time);
            // Update shortest and longest
            shortestTime = Math.min(time,shortestTime);
            longestTime = Math.max(time,longestTime);
        }
    }
}
