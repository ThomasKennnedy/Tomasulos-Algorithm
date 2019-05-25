package edu.odu.cs.cs665.tomasulosalgorithm;

/**
 * This class provides all Operation functionality.
 */
public class Operation {
    private String opcode;     ///< Contains the opcode.
    private String comment;    ///< Stores the comment.
    private String[] operands; ///< Stores all operands.
    private int execStart;     ///< Start of execution
    private int execEnd;       ///< End of execution
    private int timeWrite;     ///< Write time
    private int issue;         ///< issue number

    private boolean commentExists; ///< Specifies the existance of a comment
    private boolean scheduled;    ///< Set to true when the instruction has been scheduled

    public Operation()
    {

    }

    /**
     * Construct an Operation object give 1 opcode, 3 operands and a boolean.
     */
    public Operation(String opcode, String operand1, String operand2,
                     String operand3, boolean commentExists)
    {
        this.opcode      = opcode;
        this.operands    = new String[]{operand1, operand2, operand3};
        this.comment     = "";
        this.commentExists = commentExists;

        execStart = -1;
        execEnd   = -1;
        timeWrite = -1;
        scheduled = false;
        issue = 0;
    }

    /**
     * Construct an Operation object give 1 opcode, 2 operands and a boolean.
     */
    public Operation(String opcode,
                     String operand1, String operand2, boolean commentExists)
    {
        //break apart the second operand
        String temp = operand2; //temporary string
        String operand3;

        int tempIndex;          // temporary index
        tempIndex = temp.indexOf('(');

        operand3 = temp.substring(tempIndex + 1, temp.indexOf(')'));
        operand2 = temp.substring(0, tempIndex);

        this.opcode      = opcode;
        this.operands    = new String[]{operand1, operand2, operand3};
        this.comment     = "";
        this.commentExists = commentExists;

        execStart = -1;
        execEnd   = -1;
        timeWrite = -1;
        scheduled = false;
        issue = 0;
    }

    /**
     * Construct an Operation object given an existing Operation object.
     */
    public Operation(Operation toCopy)
    {
        this.opcode   = toCopy.opcode;

        this.operands = new String[toCopy.operands.length];
        System.arraycopy(toCopy.operands, 0,
                         this.operands, 0, toCopy.operands.length);

        this.comment  = toCopy.comment;
        this.issue = toCopy.issue;
    }

    /**
     * Return whether a comment exists.
     */
    public boolean hasComment()
    {
        return commentExists;
    }

    /**
     *Return the opcode -- exempli gratia LOAD, SD, DADDI.
     */
    public String getOpcode()
    {
        return opcode;
    }

    /**
     *Return the comment.
     */
    public String getComment()
    {
        return comment;
    }

    /**
     * Get the operand at the specified position. Positions start at "1".
     */
    public String getOperand(int number)
    {
        return operands[number - 1];
    }

    /**
     * Return the number of operands.
     */
    public int getNumberOfOperands()
    {
        return operands.length;
    }

    /**
     * Return a String containing all operands.
     */
    public String getOperands()
    {
        StringBuilder bld = new StringBuilder();

        for (int i = 0; i < (operands.length - 1); i++) {
            bld.append(operands[i]).append(' ');
        }
        bld.append(operands[operands.length - 1]);

        return bld.toString();
    }

    /**
     * Get execution start.
     */
    public int getExecStart()
    {
        return execStart;
    }

    /**
     * Get execution end.
     */
    public int getExecEnd()
    {
        return execEnd;
    }

    /**
     * Get the issue number.
     */
    public int getIssueNum()
    {
        return issue;
    }

    /**
     * Get execution description.
     */
    public String getExecution()
    {
        String toReturn = "";

        if (execStart > 0) {
            toReturn += execStart;

            if (execEnd > -1)
            {
                toReturn += "--" + execEnd;
            }
        }

        return toReturn;
    }

    /**
     * Get the time the result was written.
     */
    public int getWriteTime()
    {
        return timeWrite;
    }

    /**
     * Return whether the instruction has been scheduled.
     */
    boolean isScheduled()
    {
        return scheduled;
    }

    /**
     * Set the Operation opcode.
     */
    public void setOpcode(String opcode)
    {
        this.opcode = opcode;
    }

    /**
     * Set a comment.
     */
    public void setComment(String comment)
    {
        this.comment = comment;
    }

    /**
     *Set the operand at the specified position. Positions start at "1".
    ///Return an error if the position is invalid.
     */
    public void setOperand(int number, String operandIn)
         throws Exception
    {
        if (number < 1 || number > operands.length) {
            throw new Exception() {
                public String toString()
                {
                    return "Error: Manipulation of Invalid Operand";
                }
            };
        }

        operands[number - 1] = operandIn;
    }

    /**
     * Set execution start.
     */
    public void setExecStart(int n)
    {
        execStart = n;
    }

    /**
     * Set execution end.
     */
    public void setExecEnd(int n)
    {
        execEnd = n;
    }

    /**
     * Set the time the result was written.
     */
    public void setWriteTime(int n)
    {
        timeWrite = n;
    }

    /**
     * Set the scheduled flag.
     */
    public void setScheduled()
    {
        scheduled = true;
    }

    /**
     * Set the issue number.
     */
    public void setIssueNum(int i)
    {
        issue = i;
    }

    /**
     * Return the string represeation of the Operation object.
     */
    public String toString()
    {
        StringBuilder bld = new StringBuilder();
        bld.append("" + opcode);

        for (int i = 0; i < operands.length; i++) {
            bld.append(" " + operands[i]);
        }

        if (commentExists) {
            bld.append(" ; " + comment);
        }

        return bld.toString();
    }
}
