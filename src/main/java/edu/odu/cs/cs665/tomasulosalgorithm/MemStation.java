package edu.odu.cs.cs665.tomasulosalgorithm;

import java.util.HashMap;


///
/// This class provides all MemStation functionality.
///
public class MemStation extends ReservationStation {

    private String address;        ///< Address to be stored or loaded from memory
    private String[] addr_comp;   ///< Components used in computing the final address, and the register for stores [3]
    private boolean is_store;     ///< True indicates that the current operation is a store

    ///
    /// Calls superclass constructor and initializes address
    ///
    public MemStation(String sname)
    {
         super(sname);
         address = null;
         addr_comp = new String[3];
         addr_comp[0] = "";
         addr_comp[1] = "";
         addr_comp[2] = "";
         is_store = false;
    }

    ///
    /// Function to clear the Reservation Station
    ///
    public void clear()
    {
         busy = false;
         operation = null;
         address = null;
         resultReady = false;
         resultWritten = false;
         result = "";
         addr_comp[0] = "";
         addr_comp[1] = "";
         addr_comp[2] = "";
         is_store = false;
    }

    ///
    /// Function to determine whether MemStation is Ready for use
    ///
    public boolean isReady()
    {
       return !resultReady
           && !isPlaceHolder(addr_comp[0])
           && !isPlaceHolder(addr_comp[1])
           && (!is_store || (is_store && !isPlaceHolder(addr_comp[2]))); // force store to wait for register value
    }

    ///
    /// Function returns the value of address
    ///
    public String getAddress()
    {
         return address;
    }

    ///
    /// Function sets the value of address
    ///
    public void setAddress(String address)
    {
         this.address = address;
    }

    ///
    /// Return whether the currently scheduled operation is a store
    ///
    public boolean isStore()
    {
         return is_store;
    }

    ///
    /// Function to schedule the instruction
    ///
    public void scheduleInstruction(Operation op,
                                    RegisterFiles reg_in, int cycles)
    {
         this.operation = op;
         this.busy = true;
         this.duration = cycles;

         //Set the address components
         addr_comp[0] = reg_in.getRegister(op.getOperand(2));
         addr_comp[1] = reg_in.getRegister(op.getOperand(3));
         addr_comp[2] = reg_in.getRegister(op.getOperand(1)); // the register being read-store or written-load

         updateAddress();

         //set the operation as scheduled
         operation.setScheduled();

         //Classify the Instruction as load or store
         setStore();
    }

    ///
    ///Update the address components
    ///
    public void updateAddrComponents(String alias, String value)
    {
         if (addr_comp[0].equals(alias)) {
              addr_comp[0] = value;
         }
         else if (addr_comp[1].equals(alias)) {
              addr_comp[1] = value;
         }

         //replace the register placeholder - only imporant for stores
         if (addr_comp[2].equals(alias)) {
              addr_comp[2] = value;
         }

         updateAddress();
    }


    ///
    /// Utility function to update the address
    ///
    private void updateAddress()
    {
         int temp_int1, temp_int2; //temporary integers to hold the intermediate parsing results

         //default result if the parsing fails
         result = "M(" + addr_comp[0]  + "," + addr_comp[1]  + ")";

         //attempt to parse the values as integers
         try {
              temp_int1 = Integer.parseInt(addr_comp[0]);

              temp_int2 = Integer.parseInt(addr_comp[1]);

              result = "M(" + temp_int1 + temp_int2 + ")";
         }
         catch (Exception e) {

         }
         address = result;
    }

    ///
    /// Utility funtion to set the message as load or store
    ///
    private void setStore()
    {
         is_store = isStore(operation.getOpcode());
    }

    ///
    /// Checks if the station has memory access priority
    ///
    public boolean hasPriority(HashMap<String, Integer> memory_buffer)
    {
         boolean  to_return = false;

         if (memory_buffer.containsKey(result)) {
              if (memory_buffer.get(result).intValue() >= operation.getIssueNum()) {
                   to_return = true;
              }
         }
         else {
              if (operation != null) {
                   memory_buffer.put(result,
                                     Integer.valueOf(operation.getIssueNum()));
                   to_return = true;
              }
         }

         return to_return;
    }

    ///
    ///Static Function Classify the message as Store or Otther
    ///
    public static boolean isStore(String opcode)
    {
          return opcode.equals("S.D") || opcode.equals("SD");
    }
}
