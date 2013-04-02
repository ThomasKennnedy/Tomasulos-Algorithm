import java.util.ArrayList;

///
/// This class provides all operation queueing and storage funtionality.
///

public class OperationList {
     private ArrayList<Operation> op_list; ///< The list op Operations   
     private int curr_op;                  ///< The current Operation
     
     ///
     ///Initialize the op_list.
     ///
     public OperationList(){
          op_list = new ArrayList<Operation>();
     }

     ///
     ///Generate a new OperationList from the specified OperationList.
     ///
     public OperationList( OperationList to_copy ){
          this.op_list = new ArrayList<Operation>( to_copy.op_list );
     }
     
     ///
     ///Return an ArrayList<Operation> that contains a copy of all Operations.
     ///
     public ArrayList<Operation> getOperationList(){
          return new ArrayList<Operation>( op_list );     
     }
     
     ///
     ///Get an Operation given a position number. The first position is "1". 
     ///
     public Operation getOperation( int num ) throws Exception{
          if( num > op_list.size() ){
               throw new Exception(){
                    public String toString(){
                         return "OperationList: Invalid Operation Number";  
                    }
               };
          }
          return op_list.get( num - 1 );
     }
     
     ///
     ///Get the next Operation to be scheduled.
     ///
     public Operation getNextOperation() throws Exception{
          if( curr_op > op_list.size() ){
               throw new Exception(){
                    public String toString(){
                         return "OperationList: Operations Exhausted";  
                    }
               };
          } 
          curr_op++;
          
          return op_list.get( curr_op - 1 );
     }
     
     ///
     ///Returns true when there is at least one Operation that hs not been scheduled. 
     ///Returns false if there is not an Operation to schedule.
     ///     
     public boolean moreOperationsQueued(){
          return curr_op <= op_list.size();
     }
     
     ///
     ///Add an operation to the OperationList
     ///
     public void addOperation( Operation to_add ){
          op_list.add( to_add );
     }
     
     ///
     ///Generates the string representation of the Operation Object.
     ///     
     public String toString(){
          return "# Operations: " + op_list.size();
     }
}
