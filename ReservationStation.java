public class ReservationStation{
    public String sname;       //name of reservation station
    private boolean busy;      //is station holding an operation
    private Operation operation;  //type of operation
    private long vj;           //value of operand
    private long vk;           //value of operand
    private String qj;         //name of reservation station producing Vj
    private String qk;         //name of reservation station producing Vk
    private long A;            //used to hold immediate field or off address
    public long result;       //used to hold result 
    

    private boolean resultReady;   //flag indicating result is ready to be written
    private boolean resultWritten;  //flag indicating the result has been written

    public ReservationStation(String sname){
        this.sname = sname;
        busy = false;
        operation = null;
        Vj = Vk = A = 0;
        Qj = Qk = null;
        resultReady = false;
        resultWritten = false;
    }

    //after result is written, clear the reservation station
    public void clear(){
        busy = false;
        operation = null;
        Vj = Vk = A = 0;
        Qj = Qk = null;
        resultready = false;
        resultwritten = false;
    }

    //determines whether the operands are available and therefore ready
    //for execution
    public boolean isReady(){
        return (busy == true && qj == null && qk == null && 
                resultready == false);
    }

	// getters methods
	public Integer getVj(){
		return vj;
	}

	public Integer getVk(){
		return vk;
	}

	public String getQj(){
		return qj;
	}

	public String getQk(){
		return qk;
	}

	public Integer getA(){
                return A;
        }

	public boolean isBusy(){
		return busy;
	}
	
	public boolean isResultReady(){
		return resultready;
	}

	public boolean isResultWriiten(){
		return resultwritten;
	}

	//setter methods
	public void setQj(String sname){
		qj=sname;
	}

	public void setQk(String sname){
		qk=sname;
	}

	public void setVj(long i){
		vj=i;
	}

	public void setVk(long i){
		vk=i;
	}
	
	public scheduleInstruction(Operation op){
		this.busy= true;
	
	// rest schedule code goes here.
	}
	
}
