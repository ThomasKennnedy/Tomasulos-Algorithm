package edu.odu.cs.cs665.tomasulosalgorithm;

/**
 * This class provides all clock cycle functionality.
 */
public class Clock {
    /**
     * The global clock instance.
     */
    private static Clock clockPtr = null;

    /**
     * Time in CPU cycles.
     */
    private int time;

    /**
     * Allow access to the Clock instance.
     *
     * @return reference to the global clock
     */
    public static Clock getInstance()
    {
        if (clockPtr == null) {
            clockPtr = new Clock();
        }
        return clockPtr;
    }

    /**
     * Construct the CPU clock. Used internally.
     */
    private Clock()
    {
        time = 0;
    }

    /**
     * Get the CPU current cycle number.
     *
     * @return current time
     */
    public int get()
    {
        return time;
    }

    /**
     * Increment the clock by one time step.
     */
    public void increment()
    {
        time++;
    }
}