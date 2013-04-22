public class MemStation extends ReservationStation{

	private String address; //address to be stored or loaded from memory

	public String getAddress(){
                return address;
        }
	
	public void setAddress(String address){
		address = address;
	}

	public void clear(){
        busy = false;
        operation = null;
		address = null;
        resultready = false;
        resultwritten = false;
    }

	public boolean isReady(){
        return (busy == true && address==null && 
                resultready == false);
    }
	
	public void MemStation(String sname){
        sname = sname;
        busy = false;
        operation = null;
        address = null;
        resultReady = false;
        resultWritten = false;
    }
	
	public scheduleInstruction(Operation op){
		this.busy= true;
	// rest schedule code goes here.
	}
}