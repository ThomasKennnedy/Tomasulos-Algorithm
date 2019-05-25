package edu.odu.cs.cs665.tomasulosalgorithm;

import java.util.LinkedHashMap;

/**
 * This class provides all register functionality for
 * integer and floating point register files.
 */
public class RegisterFiles {
    /**
     * Total number of integer registers-- not including R0.
     */
    public static final int NUM_INT_REGISTERS = 32;

    /**
     * Total number of floating point registers.
     */
    public static final int NUM_FP_REGISTERS  = 32;

    /**
     * All integer registers.
     */
    private LinkedHashMap<String, String> registersInt;

    /**
     * All floating point registers.
     */
    private LinkedHashMap<String, String> registersFp;

    /**
     * Constructs the register files; initializes all floating point and
     * integer registers to "0".
     */
    public RegisterFiles()
    {
        registersInt = new LinkedHashMap<String, String>();
        registersFp  = new LinkedHashMap<String, String>();

        //Initialize R0
        registersInt.put("R0", "0");

        //Initialize the Integer Registers R1 to Rn
        for (int i = 0; i < NUM_INT_REGISTERS; i++) {
            String tempReg =  "R" + (i + 1);
            registersInt.put(tempReg, "");
        }

        //Initialize the FP Registers f0 to Fn
        for (int i = 0; i < NUM_FP_REGISTERS; i++) {
            String tempReg =  "F" + (2 * i);
            registersFp.put(tempReg, "");
        }
    }

    /**
     * Generates a copy of an existing RegisterFiles object.
     */
    public RegisterFiles(RegisterFiles toCopy)
    {
        registersInt = new LinkedHashMap<String, String>();
        registersFp  = new LinkedHashMap<String, String>();

        //Copy Integer and FP Register Files
        this.registersInt = new LinkedHashMap<>(toCopy.registersInt);
        this.registersFp  = new LinkedHashMap<>(toCopy.registersFp);
    }

    /**
     * Returns a LinkedHashMap that conatins a copy of the current integer
     * register file.
     */
    public LinkedHashMap<String, String> getIntegerRegisters()
    {
        return new LinkedHashMap<String, String>(registersInt);
    }

    /**
     * Returns a LinkedHashMap that conatins a copy of the current floating
     * point register file.
     */
    public LinkedHashMap<String, String> getFPRegisters()
    {
        return new LinkedHashMap<String, String>(registersFp);
    }

    /**
     * Returns the current value of the specified register. An "Invalid
     * register ID" exception is thrown if the specified register does not
     * exist.
     */
    public String getRegister(String rId)
         /*throws Exception*/
    {
        String toReturn = "";

        if (rId.charAt(0) == 'R') {
            toReturn = registersInt.get(rId);
        }
        else if (rId.charAt(0) == 'F') {
            toReturn = registersFp.get(rId);
        }
        /*else{
            throw new Exception(){
                public String toString(){
                    return "Invalid Register ID.";
                }
            };
        }*/

        if (toReturn.equals("")) {
            toReturn = rId;
        }

        return toReturn;
    }

    /**
     * Sets a new value for the specified register. An "Invalid register ID"
     * exception is thrown if the specified register does not exist.
     */
    public void setRegister(String rId, String valIn)
        /*throws Exception*/
    {
        if (rId.charAt(0) == 'R' && registersInt.containsKey(rId)) {
            registersInt.put(rId, valIn);
        }
        else if (rId.charAt(0) == 'F' && registersFp.containsKey(rId)) {
            registersFp.put(rId, valIn);
        }
        /*
        else{
            throw new Exception(){
                public String toString() {
                    return "Invalid Register ID.";
                }
            };
        }*/

        //System.out.println( rId + " " + valIn);
    }

    /**
     * Generates the string representation of the RegisterFiles Object.
     */
    public String toString()
    {
        return "Integer Registers: " + NUM_INT_REGISTERS + "\n"
             + "    Values: \n" + registersInt.toString() + "\n"
             + "FP Registers:      " + NUM_FP_REGISTERS + "\n"
             + "    Values: \n" + registersFp.toString() + "\n";
    }
}
