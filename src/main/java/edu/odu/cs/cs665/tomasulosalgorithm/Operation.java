package edu.odu.cs.cs665.tomasulosalgorithm;

///
/// This class provides all Operation functionality.
///
public class Operation {
     private String opcode;           ///< Contains the opcode.
     private String comment;          ///< Stores the comment.
     private String[] operands;       ///< Stores all operands.
     private int exec_start, exec_end;///< start and end of executions respectively
     private int time_write;          ///< Write time
     private int issue;               ///< issue number

     private boolean has_comment; ///< Specifies the existance of a comment
     private boolean scheduled;    ///< Set to true when the instruction has been scheduled


     public Operation()
     {

     }

     ///
     /// Construct an Operation object give 1 opcode, 3 operands and a boolean.
     ///
     public Operation(String opcode, String operand_1, String operand_2,
                      String operand_3, boolean has_comment)
     {
          this.opcode      = opcode;
          this.operands    = new String[]{operand_1, operand_2, operand_3};
          this.comment     = "";
          this.has_comment = has_comment;

          exec_start = exec_end = time_write = -1;
          scheduled = false;
          issue = 0;
     }

     ///
     /// Construct an Operation object give 1 opcode, 2 operands and a boolean.
     ///
     public Operation(String opcode, String operand_1, String operand_2, boolean has_comment)
     {
          //break apart the second operand
          String temp = operand_2; //temporary string
          String operand_3;

          int temp_index;          // temporary index
          temp_index = temp.indexOf('(');

          operand_3 = temp.substring(temp_index + 1, temp.indexOf(')'));
          operand_2 = temp.substring(0, temp_index);

          this.opcode      = opcode;
          this.operands    = new String[]{operand_1, operand_2, operand_3};
          this.comment     = comment;
          this.has_comment = has_comment;

          exec_start = exec_end = time_write = -1;
          scheduled = false;
          issue = 0;
     }

     ///
     /// Construct an Operation object given an existing Operation object.
     ///
     public Operation(Operation to_copy)
     {
          this.opcode   = to_copy.opcode;

          this.operands = new String[ to_copy.operands.length ];
          System.arraycopy(to_copy.operands, 0, this.operands, 0, to_copy.operands.length);

          this.comment  = to_copy.comment;
          this.issue = to_copy.issue;
     }

     ///
     /// Return whether a comment exists
     ///
     public boolean hasComment()
     {
          return has_comment;
     }

     ///
     ///Return the opcode -- exempli gratia LOAD, SD, DADDI.
     ///
     public String getOpcode()
     {
          return opcode;
     }

     ///
     ///Return the comment.
     ///
     public String getComment()
     {
          return comment;
     }

     ///
     ///Get the operand at the specified position. Positions start at "1".
     ///
     public String getOperand(int number)
     {
          return operands[number - 1];
     }

     ///
     ///Return the number of operands
     ///
     public int getNumberOfOperands()
     {
          return operands.length;
     }

     ///
     ///Return a String containing all operands.
     ///
     public String getOperands()
     {
          StringBuilder bld = new StringBuilder();

          for (int i = 0; i < (operands.length - 1); i++) {
               bld.append(operands[i]).append(' ');
          }
          bld.append(operands[operands.length - 1]);

          return bld.toString();
     }

     ///
     /// Get execution start
     ///
     public int getExecStart()
     {
          return exec_start;
     }

     ///
     ///Get execution end
     ///
     public int getExecEnd()
     {
          return exec_end;
     }

     ///
     ///Get the issue number
     ///
     public int getIssueNum()
     {
          return issue;
     }

     ///
     ///Get execution description
     ///
     public String getExecution()
     {
          String to_return = "";

          if (exec_start > 0) {
               to_return += exec_start;

               if (exec_end > -1)
               {
                    to_return += "--" + exec_end;
               }
          }

          return to_return;
     }

     ///
     ///Get the time the result was written
     ///
     public int getWriteTime()
     {
          return time_write;
     }

     ///
     /// Return whether the instruction has been scheduled
     ///
     boolean isScheduled()
     {
          return scheduled;
     }

     ///
     ///Set the Operation opcode.
     ///
     public void setOpcode(String opcode)
     {
          this.opcode = opcode;
     }

     ///
     ///Set a comment
     ///
     public void setComment(String comment)
     {
          this.comment = comment;
     }

     ///
     ///Set the operand at the specified position. Positions start at "1".
     ///Return an error if the position is invalid.
     ///
     public void setOperand(int number, String operand_in)
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

          operands[number - 1] = operand_in;
     }

     ///
     ///Set execution start
     ///
     public void setExecStart(int n)
     {
          exec_start = n;
     }

     ///
     ///Set execution end
     ///
     public void setExecEnd(int n)
     {
          exec_end = n;
     }

     ///
     ///Set the time the result was written
     ///
     public void setWriteTime(int n)
     {
          time_write = n;
     }

     ///
     /// Set the scheduled flag
     ///
     void setScheduled()
     {
          scheduled = true;
     }

     ///
     /// Set the issue number
     ///
     public void setIssueNum(int i)
     {
          issue = i;
     }

     ///
     ///Return the string represeation of the Operation object.
     ///
     public String toString()
     {
          StringBuilder bld = new StringBuilder();
          bld.append("" + opcode);

          for (int i = 0; i < operands.length; i++) {
               bld.append(" " + operands[i]);
          }

          if (has_comment) {
               bld.append(" ; " + comment);
          }

          return bld.toString();
     }
}
