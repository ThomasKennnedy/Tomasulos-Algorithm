package edu.odu.cs.cs665.tomasulosalgorithm;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.*;

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
          curr_op = 1;
     }

     ///
     ///Generate a new OperationList from the specified OperationList.
     ///
     public OperationList( OperationList to_copy ){
          this.op_list = new ArrayList<Operation>( to_copy.op_list );
          this.curr_op = to_copy.curr_op;
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
     public Operation getOperation( int num ) {
          return op_list.get( num - 1 );
     }
     
     ///
     ///Get the next Operation to be scheduled.
     ///
     public Operation getNextOperation(){
          return op_list.get( curr_op - 1 );
     }
     
     ///
     ///Get the last Operation in the list.
     ///
     public Operation getLastOperation(){
          return op_list.get( op_list.size() - 1 );
     }     
     
     ///
     ///Returns true when there is at least one Operation that hs not been scheduled. 
     ///Returns false if there is not an Operation to schedule.
     ///     
     public boolean moreOperationsQueued(){
          return curr_op <= op_list.size();
     }
     
     ///
     ///Add an operation to the OperationList.
     ///
     public void addOperation( Operation to_add ){
          op_list.add( to_add );
     }
     
     ///
     ///Return the number of operations in the list.
     ///
     public int getNumberOfOperations(){
          return op_list.size();
     }
     
     ///
     ///Set the iterator to the next Operation in the List
     ///
     public void increment(){
          curr_op++;
     }
     
     ///
     ///Generates the string representation of the Operation Object.
     ///     
     public String toString(){
          String to_return = "# Operations: " + op_list.size();
          
          for( Operation to_print : op_list ){
               to_return += "\n" + to_print;
          }
          
          return to_return;
     }
}
