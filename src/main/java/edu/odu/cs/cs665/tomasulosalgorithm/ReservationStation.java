package edu.odu.cs.cs665.tomasulosalgorithm;

/**
 * This class provides all Reservation Station functionality.
 */
public abstract class ReservationStation {
    protected String sname;              ///< name of reservation station
    protected boolean busy;              ///< flag indicating whether station holding an operation
    protected Operation operation;       ///< type of operation
    protected String result;             ///< used to hold result
    protected long duration;             ///< holds the durations of the instruction
    protected boolean resultReady;       ///< flag indicating result is ready to be written
    protected boolean resultWritten;     ///< flag indicating the result has been written

    /**
     * Construct an Reservationstation object and initialize sname,
     * busy and operation.
     */
    public ReservationStation(String sname)
    {
        this.sname = sname;
        busy = false;
        operation = null;
        resultReady = false;
        resultWritten = false;
        result = "";
    }

    /**
     * Abstract function to clear the reservation station.
     */
    abstract void clear();

    /**
     * Abstract Function to determine whether the Station is Ready for use.
     */
    abstract boolean isReady();

    /**
     * Abstract Function to schedule the instruction.
     */
    abstract void scheduleInstruction(Operation op,
                                      RegisterFiles reg_in, int cycles);

    /**
     * Return the result.
     */
    String getResult()
    {
        if (operation.getWriteTime() == -1) {
             operation.setWriteTime(Clock.getInstance().get());
        }

        resultWritten = true;

        return result;
    }


    /**
     * Function return the value of resultready.
     */
    public boolean isResultReady()
    {
         return resultReady;
    }

    /**
     * Function returns the value of resultwritten.
     */
    public boolean isResultWritten()
    {
        return resultWritten;
    }

    /**
     * Function return the value of busy.
     */
    public boolean isBusy()
    {
        return busy;
    }

    /**
     * Function returns the value of duration.
     */
    public long getDuration()
    {
        return duration;
    }

    /**
     * Function sets the value of duration.
     */
    public void setDuration(long d)
    {
        duration = d;
    }

    /**
     * Function returns the value of name of reservation station.
     */
    public String getName()
    {
        return sname;
    }

    /**
     * Function sets the name of the reservation station.
     */
    public void setName(String name)
    {
         sname = name;
    }

    /**
     * Decrement duration.
     */
    public void performCycle()
    {
        //set the execution start of the Operation
        if (operation.getExecStart() == -1) {
             operation.setExecStart(Clock.getInstance().get());
        }

        duration--;
        resultReady = (duration == 0);

        if (resultReady) {
             operation.setExecEnd(Clock.getInstance().get());
        }
    }

    /**
     * Utility function to check if the Register Value is an alias.
     */
    protected boolean isPlaceHolder(String to_check)
    {
        return to_check.equals("Add1") || to_check.equals("Add2")
            || to_check.equals("Mul1") || to_check.equals("Mul1")
            || to_check.equals("Div1") || to_check.equals("Div2")
            || to_check.equals("Load1") || to_check.equals("Load2")
            || to_check.equals("Int1");
    }
}
