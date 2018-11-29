///
/// This class provides all clock cycle functionality.
///
public class Clock {
    int time;                     ///< time in cycles
    static Clock clockPtr = null; ///< the clock instance
    
    ///
    ///class is a singleton so constructor is private.
    ///
    private Clock()
    {
        time = 0;
    }
    
    ///
    ///returns singleton instance
    ///
    static Clock getInstance()
    {
        if (clockPtr == null) {
            clockPtr = new Clock();
        }
        return clockPtr;
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