///
/// This class provides all ALUStation functionality.
///

public class ALUStation extends ReservationStation{
    
    private String vj;          ///<value of operand
    private String vk;          ///<value of operand
    private String qj;          ///<name of reservation station producing Vj
    private String qk;         	///<name of reservation station producing Vk
    private String A;           ///<used to hold immediate field or off address
    
	///
    /// Calls superclass constructor and initializes vj,vk,qj and qk
    ///
    public ALUStation(String sname){
        super(sname);
        vj = vk = A = null;
        qj = qk = null;
    }
    
    ///
    /// Function to clear the reservation Station
    ///
    public void clear(){
        busy = false;
        operation = null;
        vj = vk = A = null;
        qj = qk = null;
        resultReady = false;
        resultWritten = false;
    }
    
	///
    /// Function to determine whether operands are available and therefore ready for execution
    ///
    public boolean isReady(){
        return (busy == true && qj == null && qk == null &&
                resultReady == false);
    }
	
	///
    /// Function to schedule the instruction
    ///
	public void scheduleInstruction(Operation op){
		this.busy= true;
        
        // rest schedule code goes here.
	}
	
	///
    /// Function returns the value vj
    ///
	public String getVj(){
		return vj;
	}
    
	///
    /// Function returns the value vk
    ///
	public String getVk(){
		return vk;
	}
    
	///
    /// Function returns the value qj
    ///
	public String getQj(){
		return qj;
	}
    
	///
    /// Function returns the value qk
    ///
	public String getQk(){
		return qk;
	}
    
	///
    /// Function returns the value A
    ///
	public String getA(){
        return A;
    }
    
	///
    /// Function to set the value of qj
    ///
	public void setQj(String sname){
		qj=sname;
	}
    
	///
    /// Function to set the value of qk
    ///
	public void setQk(String sname){
		qk=sname;
	}
    
	///
    /// Function to set the value of vj
    ///
	public void setVj(String i){
		vj=i;
	}
	
	///
    /// Function to set the value of vk
    ///
	public void setVk(String i){
		vk=i;
	}
	
	///
    /// Function to set the value of A
    ///
	public void setA(String i){
		A=i;
	}

	
    
}
