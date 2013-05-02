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
	public void scheduleInstruction(Operation op, RegisterFiles reg_in, int cycles){
          this.operation = op;
          this.busy= true;
          this.duration = cycles;          
          
          result = "R(" + op.getOperand(2) + "," + op.getOperand(3) + ")";
          
          if( isPlaceHolder(reg_in.getRegister(operation.getOperand(2))) ){
               qj = reg_in.getRegister( operation.getOperand(2) );
          }
          else{
               vj = reg_in.getRegister( operation.getOperand(2) );
          }
          
          if( isPlaceHolder(reg_in.getRegister(operation.getOperand(3))) ) {
               qk = reg_in.getRegister( operation.getOperand(3) );
          }
          else{
               vk = reg_in.getRegister( operation.getOperand(3) );
          }
          
          //set the operation as scheduled
          operation.setScheduled();
	}
	
	///
    /// Function returns the value vj
    ///
	public String getVj(){
		return ( vj==null ? "" : vj );
	}
    
	///
    /// Function returns the value vk
    ///
	public String getVk(){
		return ( vk==null ? "" : vk );
	}
    
	///
    /// Function returns the value qj
    ///
	public String getQj(){
		return ( qj==null ? "" : qj );
	}
    
	///
    /// Function returns the value qk
    ///
	public String getQk(){
		return ( qk==null ? "" : qk );
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
          
          if( vk != null ){
               result = "R(" + vj + "," + vk + ")";
          }
          else{
               result = "R(" + vj + "," + operation.getOperand(3) + ")";
          }
	}
	
	///
    /// Function to set the value of vk
    ///
	public void setVk(String i){
		vk=i;
          
          if( vj != null ){
               result = "R(" + vj + "," + vk + ")";
          }
          else{
               result = "R(" + operation.getOperand(2) + "," + vk + ")";
          }
	}
	
	///
    /// Function to set the value of A
    ///
	public void setA(String i){
		A=i;
	}

     ///
     /// Return an operation type descriptor
     ///
     public String getOperation(){
          return (operation !=null ? operation.getOpcode(): " ");
     }
     
     ///
     /// Utility funtion toc check if the Register Value is an alias
     ///
     private boolean isPlaceHolder( String to_check ){
          return ( to_check.equals("Add1") || to_check.equals("Add2") || 
                   to_check.equals("Mul1") || to_check.equals("Mul1") ||
                   to_check.equals("Div1") || to_check.equals("Div2") ||
                   to_check.equals("Load1")|| to_check.equals("Load2")||
                   to_check.equals("Int1") );
                 
     }
     
	
    
}
