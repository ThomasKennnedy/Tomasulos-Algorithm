///
/// This class provides all MemStation functionality.
///

public class MemStation extends ReservationStation{
    
     private String address;		///<address to be stored or loaded from memory

     ///
     /// Calls superclass constructor and initializes address
     ///
     public MemStation(String sname){
          super(sname);
          address = null;
     }

     ///
     /// Function to clear the reservation Station
     ///
     public void clear(){
          busy = false;
          operation = null;
          address = "";
          resultReady = false;
          resultWritten = false;
     }

     ///
     /// Function to determine whether MemStation is Ready for use
     ///
     public boolean isReady(){
        return (busy == true && address==null &&
                resultReady == false);
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
     public void scheduleInstruction(Operation op, RegisterFiles reg_in, int cycles){
          this.operation = op;
          this.busy= true;
          this.duration = cycles;
          //Create the address descriptor
          this.address =  reg_in.getRegister( op.getOperand(2) )+ "+" + reg_in.getRegister(  op.getOperand(3) );
          
          //set the operation as scheduled
          operation.setScheduled();
     }
}
