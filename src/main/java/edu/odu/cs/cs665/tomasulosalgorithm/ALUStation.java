package edu.odu.cs.cs665.tomasulosalgorithm;

/**
 * This class provides all ALUStation functionality.
 */
public class ALUStation extends ReservationStation {

    private String vj;          ///<value of operand
    private String vk;          ///<value of operand
    private String qj;          ///<name of reservation station producing Vj
    private String qk;          ///<name of reservation station producing Vk
    private String A;           ///<used to hold immediate field or off address

    /**
     * Calls superclass constructor and initializes vj,vk,qj and qk
     */
    public ALUStation(String sname)
    {
        super(sname);
        this.vj = null;
        this.vk = null;
        this.A = null;
        this.qj = null;
        this.qk = null;
    }

    /**
     * Function to clear the Reservation Station
     */
    public void clear()
    {
        this.busy = false;
        this.operation = null;
        this.vj = null;
        this.vk = null;
        this.A = null;
        this.qj = null;
        this.qk = null;
        super.resultReady = false;
        super.resultWritten = false;
    }

    /**
     * Determine whether operands are available and therefore ready
     * for execution.
     */
    public boolean isReady()
    {
        return busy
            && qj == null && qk == null
            && !resultReady;
    }

    /**
     * Function to schedule the instruction
     */
    public void scheduleInstruction(Operation op,
                                    RegisterFiles reg_in, int cycles)
    {
        this.operation = op;
        this.busy = true;
        this.duration = cycles;

        result = "R(" + op.getOperand(2) + "," + op.getOperand(3) + ")";

        if (isPlaceHolder(reg_in.getRegister(operation.getOperand(2)))) {
             qj = reg_in.getRegister(operation.getOperand(2));
        }
        else {
             vj = reg_in.getRegister(operation.getOperand(2));
        }

        if (isPlaceHolder(reg_in.getRegister(operation.getOperand(3)))) {
             qk = reg_in.getRegister(operation.getOperand(3));
        }
        else {
             vk = reg_in.getRegister(operation.getOperand(3));
        }

        //set the operation as scheduled
        operation.setScheduled();
    }

    /**
     * Function returns the value vj
     */
    public String getVj()
    {
        if (vj == null) {
            return "";
        }

        return vj;

        //return (vj == null ? "" : vj);
    }

    /**
     * Function returns the value vk
     */
    public String getVk()
    {
        if (vk == null) {
            return "";
        }

        return vk;

        //return (vk == null ? "" : vk);
    }

    /**
     * Function returns the value qj
     */
    public String getQj()
    {
        if (qj == null) {
            return "";
        }

        return qj;

        //return (qj == null ? "" : qj);
    }

    /**
     * Function returns the value qk
     */
    public String getQk()
    {
        if (qk == null) {
            return "";
        }

        return qk;

        //return (qk == null ? "" : qk);
    }

    /**
     * Function returns the value A
     */
    public String getA()
    {
        return A;
    }

    /**
     * Function to set the value of qj
     */
    public void setQj(String sname)
    {
        qj = sname;
    }

    /**
     * Function to set the value of qk
     */
    public void setQk(String sname)
    {
        qk = sname;
    }

    /**
     * Function to set the value of vj
     */
    public void setVj(String i)
    {
        vj = i;
    }

    /**
     * Function to set the value of vk
     */
    public void setVk(String i)
    {
        vk = i;
    }

    /**
     * Function to set the value of A
     */
    public void setA(String i)
    {
        A = i;
    }

    /**
     * Finalize the result - perform integer parsing if necessary
     */
    void finalizeResult()
    {
        boolean is_int1, is_int2; //boolean flags that hold true if the operands are integers
        int temp_int1, temp_int2; //temporary integers to hold the intermediate parsing results

        //default result if the parsing fails
        result = "R(" + vj + "," + vk + ")";

        //attempt to parse the values as integers
        try {
            //parse vj
            if (vj.startsWith("#")) {
                vj = vj.substring(1);
            }

            temp_int1 = Integer.parseInt(vj);
            is_int1 = true;

            //parse vk
            if (vk.startsWith("#")) {
                vk = vk.substring(1);
            }

            temp_int2 = Integer.parseInt(vk);
            is_int2 = true;

            result = "" + temp_int1 + temp_int2;
        }
        catch (Exception e) {

        }
    }

    /**
     * Return an operation type descriptor
     */
    public String getOperation()
    {
        if (operation == null) {
            return " ";
        }

        return operation.getOpcode();

        //return (operation != null ? operation.getOpcode() : " ");
    }

}
