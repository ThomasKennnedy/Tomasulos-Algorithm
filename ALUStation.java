public class ALUStation extends ReservationStation{
    
    private String vj;           //value of operand
    private String vk;           //value of operand
    private String qj;         //name of reservation station producing Vj
    private String qk;         //name of reservation station producing Vk
    private String A;            //used to hold immediate field or off address
    

    public void ALUStation(String sname){
        sname = sname;
        busy = false;
        operation = null;
        vj = vk = A = null;
        qj = qk = null;
        resultReady = false;
        resultWritten = false;
    }

    //after result is written, clear the reservation station
    public void clear(){
        busy = false;
        operation = null;
        vj = vk = A = null;
        qj = qk = null;
        resultready = false;
        resultwritten = false;
    }

    //determines whether the operands are available and therefore ready
    //for execution
    public boolean isReady(){
        return (busy == true && qj == null && qk == null && 
                resultready == false);
    }
	
	public scheduleInstruction(Operation op){
		this.busy= true;

	// rest schedule code goes here.
	}

	// getters methods
	public String getVj(){
		return vj;
	}

	public String getVk(){
		return vk;
	}

	public String getQj(){
		return qj;
	}

	public String getQk(){
		return qk;
	}

	public String getA(){
                return A;
    }

	//setter methods
	public void setQj(String sname){
		qj=sname;
	}

	public void setQk(String sname){
		qk=sname;
	}

	public void setVj(String i){
		vj=i;
	}

	public void setVk(String i){
		vk=i;
	}

	

}