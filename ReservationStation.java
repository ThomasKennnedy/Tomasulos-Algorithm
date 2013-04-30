///
/// This class provides all Reservation Station functionality.
///

public abstract class ReservationStation {
	protected String sname;				///< name of reservation station
    protected boolean busy;				///< flag indicating whether station holding an operation
    protected Operation operation;  	///< type of operation
	protected String result;			///< used to hold result
    protected long duration;			///< holds the durations of the instruction
	protected boolean resultReady;		///< flag indicating result is ready to be written
    protected boolean resultWritten;	///< flag indicating the result has been written
	
	///
    /// Abstract function to clear the reservation station
    ///
	abstract void clear();
	
	///
    /// Abstract Function to determine whether the operands are available and therefore ready for execution
    ///
	abstract boolean isReady();
    
	///
    /// Abstract Function to schedule the instruction
    ///
	abstract scheduleInstruction(Operation op);
	
	///
    /// Construct an Reservationstation object and initialize sname,busy and operation
    ///
	public void ReservationStation(String sname){
		sname = sname;
        busy = false;
        operation = null;
		resultReady = false;
        resultWritten = false;
	}
	
	///
    /// Function return the value of resultready
    ///
	public boolean isResultReady(){
		return resultready;
	}
    
	///
    /// Function returns the value of resultwritten
    ///
	public boolean isResultWritten(){
		return resultwritten;
	}
	
	///
    /// Function return the value of busy
    ///
	public boolean isBusy(){
		return busy;
	}
	
	///
    /// Function returns the value of duration
    ///
	public long getDuration(){
		return duration;
	}
	
	///
    /// Function sets the value of duration
    ///
	public void setDuration(string d){
		duration = d;
	}
    
	///
    /// Function returns the value of name of reservation station
    ///
	public String getName(){
		return sname;
	}
	
	///
    /// Function sets the name of the reservation station
    ///
	public void setName(String name){
		sname = name;
	}	
}