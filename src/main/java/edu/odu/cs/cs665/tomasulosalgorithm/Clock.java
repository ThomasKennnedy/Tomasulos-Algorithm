package edu.odu.cs.cs665.tomasulosalgorithm;

/**
 * This class provides all clock cycle functionality.
 */
public class Clock {
    /**
     * The clock instance.
     */
    private static Clock clockPtr = null;

    /**
     * Time in cycles.
     */
    private int time;

    ///
    ///returns singleton instance
    ///
    public static Clock getInstance()
    {
        if (clockPtr == null) {
            clockPtr = new Clock();
        }
        return clockPtr;
    }

    ///
    ///class is a singleton so constructor is private.
    ///
    private Clock()
    {
        time = 0;
    }

    ///
    ///returns current time in cycles
    ///
    int get()
    {
        return time;
    }

    ///
    ///increment clock
    ///
    void increment()
    {
        time++;
    }
}