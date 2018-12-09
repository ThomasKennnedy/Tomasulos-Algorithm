package edu.odu.cs.cs665.tomasulosalgorithm;

import java.util.HashMap;


/**
 * This class provides all MemStation functionality.
 */
public class MemStation extends ReservationStation {

    /**
     * Address to be stored or loaded from memory.
     */
    private String address;

    /**
     * Components used in computing the final address,
     * and the register for stores [3].
     */
    private String[] addrComp;

    /**
     * True indicates that the current operation is a store.
     */
    private boolean isStore;

    /**
     * Calls superclass constructor and initializes address.
     *
     * @param sName desired station name
     */
    public MemStation(String sName)
    {
        super(sName);
        address = null;
        addrComp = new String[3];
        addrComp[0] = "";
        addrComp[1] = "";
        addrComp[2] = "";
        isStore = false;
    }

    /**
     * Clear (reset) the Reservation Station.
     */
    public void clear()
    {
        busy = false;
        operation = null;
        address = null;
        resultReady = false;
        resultWritten = false;
        result = "";
        addrComp[0] = "";
        addrComp[1] = "";
        addrComp[2] = "";
        isStore = false;
    }

    /**
     * Determine whether MemStation is Ready for use.
     *
     * @return true if the station has a result ready for use
     */
    public boolean isReady()
    {
        return !resultReady
            && !isPlaceHolder(addrComp[0])
            && !isPlaceHolder(addrComp[1])
            && (!isStore || (isStore && !isPlaceHolder(addrComp[2])));
            // force store to wait for register value
    }

    /**
     * Retrieve the address.
     *
     * @return address value
     */
    public String getAddress()
    {
         return address;
    }

    /**
     * Sets the address.
     *
     * @param address replacement address value
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * Retrieve whether the currently scheduled operation is a store.
     *
     * @return true if the currently scheduled operation is a store
     */
    public boolean isStore()
    {
         return isStore;
    }

    /**
     * Schedule an instruction.
     *
     * @param op operation to schedule
     * @param regIn RegisterFiles to reference
     * @param cycles duration
     */
    public void scheduleInstruction(Operation op,
                                    RegisterFiles regIn, int cycles)
    {
        this.operation = op;
        this.busy = true;
        this.duration = cycles;

        //Set the address components
        addrComp[0] = regIn.getRegister(op.getOperand(2));
        addrComp[1] = regIn.getRegister(op.getOperand(3));

        // the register being read-store or written-load
        addrComp[2] = regIn.getRegister(op.getOperand(1));

        updateAddress();

        //set the operation as scheduled
        operation.setScheduled();

        //Classify the Instruction as load or store
        setStore();
    }

    /**
     * Update address components.
     *
     * @param alias placeholder value to update
     * @param value updated (newly obtained result) value
     */
    public void updateAddrComponents(String alias, String value)
    {
        if (addrComp[0].equals(alias)) {
            addrComp[0] = value;
        }
        else if (addrComp[1].equals(alias)) {
            addrComp[1] = value;
        }

        //replace the register placeholder - only imporant for stores
        if (addrComp[2].equals(alias)) {
            addrComp[2] = value;
        }

        updateAddress();
    }


    /**
     * Utility function to update the address.
     */
    private void updateAddress()
        throws NumberFormatException
    {
        // temporary integers to hold the intermediate parsing results
        int tempInt1, tempInt2;

        //default result if the parsing fails
        result = "M(" + addrComp[0]  + "," + addrComp[1]  + ")";

        //attempt to parse the values as integers

        tempInt1 = Integer.parseInt(addrComp[0]);

        tempInt2 = Integer.parseInt(addrComp[1]);

        result = "M(" + tempInt1 + tempInt2 + ")";
        

        address = result;
    }

    /**
     * Utility function to set the message as load or store.
     */
    private void setStore()
    {
        isStore = isStore(operation.getOpcode());
    }

    /**
     * Checks if the station has memory access priority.
     *
     * @param memoryBuffer memory buffers to search
     *
     * @return true if this MemStation has priority
     */
    public boolean hasPriority(HashMap<String, Integer> memoryBuffer)
    {
        boolean  toReturn = false;

        if (memoryBuffer.containsKey(result)) {
            if (memoryBuffer.get(result).intValue() >= operation.getIssueNum()) {
                 toReturn = true;
            }
        }
        else {
            if (operation != null) {
                memoryBuffer.put(result,
                                 Integer.valueOf(operation.getIssueNum()));
                toReturn = true;
            }
        }

        return toReturn;
    }

    /**
     * Static Function Classify message as Store or Other.
     *
     * @param opcode message (instruction) to classify
     *
     * @return true if the instruction is a store
     */
    public static boolean isStore(String opcode)
    {
        return opcode.equals("S.D") || opcode.equals("SD");
    }
}
