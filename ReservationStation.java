public abstract class ReservationStation {
	protected String sname;       //name of reservation station
   	protected boolean busy;      //is station holding an operation
	protected Operation operation;  //type of operation
	protected String result;       //used to hold result
    	protected long duration;
	protected boolean resultReady;   //flag indicating result is ready to be written
    	protected boolean resultWritten;  //flag indicating the result has been written
	
	abstract void clear();    //after result is written, clear the reservation station
	
	abstract boolean isReady();//determines whether the operands are available and therefore ready for execution
    
	abstract scheduleInstruction(Operation op);
	
	public boolean isResultReady(){
		return resultready;
	}
    
	public boolean isResultWritten(){
		return resultwritten;
	}
	
	public boolean isBusy(){
		return busy;
	}
	
	public long getDuration(){
		return duration;
	}
	
	public void setDuration(string d){
		duration = d;
	}
    
	public String getName(){
		return sname;
	}
	
	public void setName(String name){
		sname = name;
	}	
}
