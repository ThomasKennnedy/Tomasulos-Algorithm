package edu.odu.cs.cs665.tomasulosalgorithm;

/**
 * This class provides all ALUStation functionality.
 */
public class ALUStation extends ReservationStation {

    /**
     * Value of operand (first).
     */
    private String vj;

    /**
     * Value of operand (Second).
     */
    private String vk;

    /**
     * Name of reservation station producing Vj.
     */
    private String qj;

    /**
     * Name of reservation station producing Vk.
     */
    private String qk;

    /**
     * Used to hold immediate field or off address.
     */
    private String A;

    /**
     * Calls superclass constructor and initializes vj, vk, qj and qk.
     *
     * @param sName name to use for this ALU Station
     */
    public ALUStation(String sName)
    {
        super(sName);
        this.vj = null;
        this.vk = null;
        this.A = null;
        this.qj = null;
        this.qk = null;
    }

    /**
     * Clear the Reservation Station.
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
     *
     * @return true if this station is ready for execution
     */
    public boolean isReady()
    {
        return busy
            && qj == null && qk == null
            && !resultReady;
    }

    /**
     * Function to schedule the instruction.
     *
     * @param op operand to schedule
     * @param regIn register files to reference
     * @param cycles duration in cycles
     */
    public void scheduleInstruction(Operation op,
                                    RegisterFiles regIn, int cycles)
    {
        this.operation = op;
        this.busy = true;
        this.duration = cycles;

        result = "R(" + op.getOperand(2) + "," + op.getOperand(3) + ")";

        if (isPlaceHolder(regIn.getRegister(operation.getOperand(2)))) {
             qj = regIn.getRegister(operation.getOperand(2));
        }
        else {
             vj = regIn.getRegister(operation.getOperand(2));
        }

        if (isPlaceHolder(regIn.getRegister(operation.getOperand(3)))) {
             qk = regIn.getRegister(operation.getOperand(3));
        }
        else {
             vk = regIn.getRegister(operation.getOperand(3));
        }

        //set the operation as scheduled
        operation.setScheduled();
    }

    /**
     * Retrieve the value vj.
     *
     * @return vj as a String
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
     * Retrieve the value vk.
     *
     * @return vk as a String
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
     * Retrieve the value qj.
     *
     * @return qj as a String
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
     * Retrieve the value qk.
     *
     * @return qk as a String
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
     * Retrieve the value A.
     *
     * @return A as a String
     */
    public String getA()
    {
        return A;
    }

    /**
     * Update the value of qj.
     *
     * @param sName new qj
     */
    public void setQj(String sName)
    {
        qj = sName;
    }

    /**
     * Update the value of qk.
     *
     * @param sName new qk
     */
    public void setQk(String sName)
    {
        qk = sName;
    }

    /**
     * Update the value of vj.
     *
     * @param i new vj
     */
    public void setVj(String i)
    {
        vj = i;
    }

    /**
     * Update the value of vk.
     *
     * @param i new vk
     */
    public void setVk(String i)
    {
        vk = i;
    }

    /**
     * Update the value of A.
     *
     * @param i new A
     */
    public void setA(String i)
    {
        A = i;
    }

    /**
     * Finalize the result--perform integer parsing if necessary.
     */
    void finalizeResult()
    {
        // is_intx - Flags hold true if the operands are integers
        boolean isInt1, isInt2;

        int vjAsInt;
        int vkAsInt;

        //default result if the parsing fails
        result = "R(" + vj + "," + vk + ")";

        //attempt to parse the values as integers
        try {
            //parse vj
            if (vj.startsWith("#")) {
                vj = vj.substring(1);
            }

            vjAsInt = Integer.parseInt(vj);
            isInt1 = true;

            //parse vk
            if (vk.startsWith("#")) {
                vk = vk.substring(1);
            }

            vkAsInt = Integer.parseInt(vk);
            isInt2 = true;

            result = "" + vjAsInt + vkAsInt;
        }
        catch (Exception e) {

        }
    }

    /**
     * Generate an operation type descriptor.
     *
     * @return operation type descriptor
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
