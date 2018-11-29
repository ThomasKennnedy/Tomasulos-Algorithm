///
/// This class provides all ALUStation functionality.
///

public class ALUStation extends ReservationStation{
    
    private String vj;          ///<value of operand
    private String vk;          ///<value of operand
    private String qj;          ///<name of reservation station producing Vj
    private String qk;          ///<name of reservation station producing Vk
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
    /// Function to clear the Reservation Station
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
        return (busy && qj == null && qk == null && !resultReady );
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

     ///
     /// Finalize the result - perform integer parsing if necessary
     ///
     void finalizeResult(){
          boolean is_int1, is_int2; //boolean flags that hold true if the operands are integers
          int temp_int1, temp_int2; //temporary integers to hold the intermediate parsing results
          
          //default result if the parsing fails
          result = "R(" + vj + "," + vk + ")";
     
          //attempt to parse the values as integers
          try{
               //parse vj
               if( vj.startsWith("#") ){
                    vj = vj.substring(1);
               }
               
               temp_int1 = Integer.parseInt( vj );
               is_int1 = true;
               
               //parse vk
               if( vk.startsWith("#") ){
                    vk = vk.substring(1);
               }
               
               temp_int2 = Integer.parseInt( vk );
               is_int2 = true;
               
               result = ""+( temp_int1 + temp_int2 );
          }
          catch( Exception e ){
          
          }
     }

     ///
     /// Return an operation type descriptor
     ///
     public String getOperation(){
          return (operation !=null ? operation.getOpcode(): " ");
     }
    
}
