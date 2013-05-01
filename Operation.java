
///
/// This class provides all Operation functionality.
///

public class Operation{
     private String opcode;       ///< Contains the opcode.
     private String comment;      ///< Stores the comment.
     private String[] operands;   ///< Stores all operands.
     
     private boolean has_comment; ///< Specifies the existance of a comment
     
     //public Operation(){}
     
     ///
     /// Construct an Operation object give 1 opcode, 3 operands and a boolean.
     ///
     public Operation( String opcode, String operand_1, String operand_2, String operand_3, boolean has_comment){
          this.opcode      = opcode;
          this.operands    = new String[]{ operand_1, operand_2, operand_3 };
          this.comment     = "";
          this.has_comment = has_comment;
     }     

     ///
     /// Construct an Operation object give 1 opcode, 2 operands and a boolean.
     ///     
     public Operation( String opcode, String operand_1, String operand_2, boolean has_comment ){
          this.opcode      = opcode;
          this.operands    = new String[]{ operand_1, operand_2};
          this.comment     = comment;
          this.has_comment = has_comment;
     }
          
     ///
     /// Construct an Operation object given an existing Operation object.
     ///     
     public Operation( Operation to_copy ){
          this.opcode   = to_copy.opcode;
          
          this.operands = new String[ to_copy.operands.length ];
          System.arraycopy( to_copy.operands, 0, this.operands, 0, to_copy.operands.length );
          
          this.comment  = to_copy.comment;          
     }
     
     ///
     /// Return whether a comment exists
     ///
     public boolean hasComment(){
          return has_comment;
     }
     
     ///
     ///Return the opcode -- exempli gratia LOAD, SD, DADDI. 
     ///
     public String getOpcode(){
          return opcode;
     }
     
     ///
     ///Return the comment.
     ///
     public String getComment(){
          return comment;
     }
     
     ///
     ///Get the operand at the specified position. Positions start at "1".
     ///Return an error if the position is invalid.
     ///
     public String getOperand( int number ) throws Exception{          
          if( number < 1 || number > operands.length ){
               throw new Exception(){
                    public String toString(){
                         return "Error: Retrieval of Invalid Operand";
                    }
               };
          }         
          
          return operands[ number - 1 ];
     }
     
     ///
     ///Return the number of operands
     ///
     public int getNumberOperands(){
          return operands.length;
     }
     
     ///
     ///Return a String containing all operands.
     ///
     public String getOperands(){
          String to_return = "";;
          
          for( int i =0; i < (operands.length - 1); i++){
               to_return += operands[i] + " ";
          }
          to_return += operands[operands.length - 1];
          
          return to_return;
     }     
     
     ///
     ///Set the Operation opcode.
     ///
     public void setOpcode( String opcode){
          this.opcode = opcode;
     }
     
     ///
     ///Set a comment
     ///
     public void setComment(String comment){
          this.comment = comment;
     }
     
     ///
     ///Set the operand at the specified position. Positions start at "1".
     ///Return an error if the position is invalid.
     ///
     public void setOperand( int number, String operand_in ) throws Exception{          
          if( number < 1 || number > operands.length ){
               throw new Exception(){
                    public String toString(){
                         return "Error: Manipulation of Invalid Operand";
                    }
               };
          }         
          
          operands[ number - 1 ] = operand_in;
     }   
     
     ///
     ///Return the string represeation of the Operation object.
     ///
     public String toString(){
          String to_return = opcode;
          
          for( int i =0; i < operands.length; i++){
               to_return += " " + operands[i];
          }          
          
          if( has_comment ){
               to_return += " ; " + comment;
          }
          
          return to_return;
     }
}
