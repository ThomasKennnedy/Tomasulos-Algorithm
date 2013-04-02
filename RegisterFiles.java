import java.util.Hashtable;
import java.util.Enumeration;

///
///This class provides all register functionality for
///integer and floating point register files.
///

public class RegisterFiles{
     public static final int NUM_INT_REGISTERS = 32;  ///< Total number of integer registers.
     public static final int NUM_FP_REGISTERS  = 32;  ///< Total number of floating point registers.

     private Hashtable<String, String> registers_int; ///< Consists of all integer registers.
     private Hashtable<String, String> registers_fp;  ///< Consists of all floating point registers.
     
     ///
     ///Constructs the register files; initializes all floating point and integer registers to "0";
     ///
     public RegisterFiles(){
          registers_int = new Hashtable<String, String>();
          registers_fp  = new Hashtable<String, String>();
          
          //Initialize R0
               registers_int.put( "R0", "0" );
          
          //Initialize the Integer Registers R1 to Rn
          for( int i = 0; i <= NUM_INT_REGISTERS; i++ ){
               registers_int.put( ( "R" + ( ( 2*i ) + 1 ) ), "0" );
          }
          
          //Initialize the FP Registers f0 to Fn
          for( int i = 0; i < NUM_INT_REGISTERS; i++ ){
               registers_int.put( ("F" + ( ( 2*i ) + 1 ) ), "0" );
          }
     }
     
     ///
     ///Generates a copy of an existing RegisterFiles object.
     ///
     public RegisterFiles( RegisterFiles to_copy ){
          registers_int = new Hashtable<String, String>();
          registers_fp  = new Hashtable<String, String>();

          //Copy Integer and FP Register Files
          this.registers_int = new Hashtable<String, String>( to_copy.registers_int ) ;
          this.registers_fp  = new Hashtable<String, String>( to_copy.registers_fp ) ;
     }
     
     ///
     /// Returns a Hashtable that conatins a copy of the current integer register file.
     ///
     public Hashtable<String, String> getIntegerRegisters(){
          return new Hashtable<String, String>( registers_int );
     }

     ///
     /// Returns a Hashtable that conatins a copy of the current floating point register file.
     ///
     public Hashtable<String, String> getFPRegisters(){
          return new Hashtable<String, String>( registers_fp );
     }
     
     ///
     ///Returns the current value of the specified register. An "Invalid register ID" exception is thrown if 
     ///the specified register does not exist. 
     ///
     public String getRegister( String r_id ) throws Exception{
          String to_return;
          
          if( r_id.charAt(0) == 'R' ){
               to_return = registers_int.get( r_id );
          }
          else if( r_id.charAt(0) == 'F' ){
               to_return = registers_fp.get( r_id );
          }
          else{
               throw new Exception(){
                    public String toString(){
                         return "Invalid Register ID.";
                    }
               };
          }
          
          return to_return;
     }
     
     ///
     ///Sets a new value for the specified register. An "Invalid register ID" exception is thrown if 
     ///the specified register does not exist. 
     ///
     public void setRegister( String r_id, String val_in ) throws Exception{
          if( r_id.charAt(0) == 'R' && registers_int.containsKey( r_id ) ){
               registers_int.put( r_id, val_in );
          }
          else if( r_id.charAt(0) == 'F' && registers_int.containsKey( r_id )){
               registers_fp.put( r_id, val_in );
          }
          else{
               throw new Exception(){
                    public String toString(){
                         return "Invalid Register ID.";
                    }
               };
          }
     }
     
     ///
     ///Generates the string representation of the RegisterFiles Object.
     ///
     public String toString(){
          return "Integer Registers: " + NUM_INT_REGISTERS + "\n" +
                 "    Values: " + registers_int.toString() + "\n" +
                 "FP Registers:      " + NUM_FP_REGISTERS + "\n" +
                 "    Values: " + registers_fp.toString() + "\n" ;
     } 
}
