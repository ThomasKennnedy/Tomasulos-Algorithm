///
/// This class provides all MemStation functionality.
///

public class MemStation extends ReservationStation{
    
	private String address;		///<address to be stored or loaded from memory
    
	///
    /// Calls superclass constructor and initializes address
    ///
	public void MemStation(String sname){
        super(sname);
        address = null;
    }
    
	///
    /// Function to clear the reservation Station
    ///
	public void clear(){
        busy = false;
        operation = null;
		address = null;
        resultready = false;
        resultwritten = false;
    }
    
	///
    /// Function to determine whether MemStation is Ready for use
    ///
	public boolean isReady(){
        return (busy == true && address==null &&
                resultready == false);
    }
	
	///
    /// Function returns the value of address
    ///
	public String getAddress(){
		return address;
	}
	
	///
    /// Function sets the value of address
    ///
	public void setAddress(String address){
		address = address;
	}
	
	///
    /// Function to schedule the instruction
    ///
	public scheduleInstruction(Operation op){
		this.busy= true;
        // rest schedule code goes here.
	}
}