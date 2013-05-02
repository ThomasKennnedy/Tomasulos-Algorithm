///
/// This class provides all MemStation functionality.
///

public class MemStation extends ReservationStation{
    
     private String address;		///< Address to be stored or loaded from memory
     private String[] addr_comp;    ///< Components used in computing the final address

     ///
     /// Calls superclass constructor and initializes address
     ///
     public MemStation(String sname){
          super(sname);
          address = null;
          addr_comp = new String[2];
          addr_comp[0] = "";
          addr_comp[1] = "";
     }

     ///
     /// Function to clear the Reservation Station
     ///
     public void clear(){
          busy = false;
          operation = null;
          address = null;
          resultReady = false;
          resultWritten = false;
          result = "";
          addr_comp[0] = "";
          addr_comp[1] = "";
     }

     ///
     /// Function to determine whether MemStation is Ready for use
     ///
     public boolean isReady(){
        return ( !resultReady  && !isPlaceHolder(addr_comp[0]) &&
                !isPlaceHolder(addr_comp[1]));
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
          //Set the address components
          addr_comp[0] = reg_in.getRegister( op.getOperand(2) );
          addr_comp[1] = reg_in.getRegister(  op.getOperand(3) );
          
          updateAddress();
          
          //set the operation as scheduled
          operation.setScheduled();
     }
     
     ///
     ///Update the address components
     ///
     public void updateAddrComponents(String alias, String value){
     
          if( addr_comp[0].equals(alias) ){
               addr_comp[0] = value;
          }
          else if( addr_comp[1].equals(alias) ){
               addr_comp[1] = value;
          }
          
          updateAddress();     
     }
     
     
     ///
     /// Utility function to update the address
     ///
     private void updateAddress(){
          int temp_int1, temp_int2; //temporary integers to hold the intermediate parsing results
          
          //default result if the parsing fails
          result = "M(" + addr_comp[0]  + "," + addr_comp[1]  + ")";
     
          //attempt to parse the values as integers
          try{
               temp_int1 = Integer.parseInt( addr_comp[0] );
               
               temp_int2 = Integer.parseInt( addr_comp[1] );
              
               result = "M("+( temp_int1 + temp_int2 )+")";
          }
          catch( Exception e ){
          
          }
          address = result;
     }
}
