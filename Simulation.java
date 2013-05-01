import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

public class Simulation{
     private OperationList operations;
     private RegisterFiles registers;
     private ALUStation[] alu_rs;
     private MemStation[] mem_rs;
     
     private HashMap<String, Integer[] > instruction_to_station; ///< Mapping of instructions to Reservation Stations
     
     private Clock clock;

     private boolean is_initialized;
     
     public Simulation(){
          is_initialized = false;
     }
     
     public void initialize(File data_file) throws Exception{
          clock = Clock.getInstance();
     
          OperationFileParser file_parser = new OperationFileParser( data_file );
          
          //Iniatialize containers for instructions and registers
          operations = new OperationList();
          registers = new RegisterFiles();
                    
          file_parser.parseFile( operations );
          
          //Create and intialize Reservation Stations
          alu_rs = new ALUStation[7];
          mem_rs = new MemStation[4];
          
          alu_rs[0] = new ALUStation("Int1");
          alu_rs[1] = new ALUStation("Add1");
          alu_rs[2] = new ALUStation("Add2");
          alu_rs[3] = new ALUStation("Mul1");
          alu_rs[4] = new ALUStation("Mul2");
          alu_rs[5] = new ALUStation("Div1");
          alu_rs[6] = new ALUStation("Div2");
          
          mem_rs[0] = new MemStation("Load1");
          mem_rs[1] = new MemStation("Load2");
          mem_rs[2] = new MemStation("Store1");
          mem_rs[3] = new MemStation("Store2");
          
          //Create Mapping of instructions to the appropiate Reservation Stations
          instruction_to_station = new HashMap<String, Integer[] >();
          //Memory Indices
          instruction_to_station.put( "L.D", new Integer[]{0,1} );
          instruction_to_station.put( "LD", new Integer[]{0,1} );
          instruction_to_station.put( "S.D", new Integer[]{2,3} );
          instruction_to_station.put( "SD", new Integer[]{2,3} );
          
          //ALU Indices
          instruction_to_station.put( "DADDI", new Integer[]{0} );
          instruction_to_station.put( "DADD", new Integer[]{1,2} );
          instruction_to_station.put( "ADDD", new Integer[]{1,2} );
          instruction_to_station.put( "DSUB", new Integer[]{1,2} );
          instruction_to_station.put( "SUBD", new Integer[]{1,2} );
          instruction_to_station.put( "MULD", new Integer[]{3,4} );
          instruction_to_station.put( "MUL.D", new Integer[]{3,4} );
          instruction_to_station.put( "MULTD", new Integer[]{3,4} );
          instruction_to_station.put( "DIVD", new Integer[]{5,6} );
          instruction_to_station.put( "DIV.D", new Integer[]{5,6} );
          
          //System.out.println( instruction_to_station );
          
          //indicate that the simulation has been initialized
          is_initialized = true;
     }
     
     public boolean isComplete(){
          boolean complete = false;          
          complete = !( operations.moreOperationsQueued() );
          
          for( int i = 0; i < mem_rs.length && complete; i++ ){
               complete = !mem_rs[i].isBusy() ;
          }
          
          for( int i = 0; i < alu_rs.length && complete; i++ ){
               complete = !alu_rs[i].isBusy() ;
          }
          
          return complete;          
     }
     
     public void performStep(){
          Operation to_schedule;
          boolean op_scheduled = false;
          
          //increment the clock
          clock.increment();
          
          //Update Reservation Stations
          for( ALUStation it : alu_rs ){
               if( it.isResultReady() ){
                    //broadcast( it.getResult() );
                    it.getResult();
               }

               if( it.isReady() && it.isBusy()){
                    it.performCycle();
               }
               
               if( it.isResultWritten() ){
                    it.clear();
               }    
          }          
          for( MemStation it : mem_rs ){
               //Integer Operations are guranteed to be complete before
               //a memory stations requires a register
               if( it.isResultReady() ){
                    //broadcast( it.getResult() );
                    it.getResult();
               }
               
               if( it.isBusy() ){
                    it.performCycle();
               }
               
               if( it.isResultWritten() ){
                    it.clear();
               }    
          }
          
          //Get an instruction from the head of the list
          //if the list has not been exhausted
          if( operations.moreOperationsQueued() ){
               to_schedule =  operations.getNextOperation();
               
               //Parse the Instruction Comment
               if( to_schedule.hasComment() ){
                    parseComment( to_schedule.getComment() );
               }
               
               //Determine which set of Reservations Station(s) are 
               //appropiate for an instruction
               Integer[] rs_indices = instruction_to_station.get( to_schedule.getOpcode() );
               //Memory Stations
               if( classify( to_schedule.getOpcode() ) ){
                    for( int i = rs_indices[0]; i <= rs_indices[ (rs_indices.length-1) ] && !op_scheduled; i ++){
                    //for( int i = 0; i <= 1 && !op_scheduled; i ++){
                         if( !mem_rs[i].isBusy() ){
                              mem_rs[i].scheduleInstruction( to_schedule, registers, 2 );
                              op_scheduled = true;
                         }
                    }
               }
               //ALU Stations
               else{
                    for( int i = rs_indices[0]; i <= rs_indices[ (rs_indices.length-1) ] && !op_scheduled; i ++){
                         if( !alu_rs[i].isBusy() ){
                              alu_rs[i].scheduleInstruction( to_schedule, registers, 1 );
                              op_scheduled = true;
                         }
                    }  
               }
               
               if( op_scheduled ){
                    operations.increment();
               }         
          }
     }
     
     public int getCurrentCycle(){
          return clock.get();
     }     
     
     public OperationList getOperationList(){
          return operations;
     }

     public RegisterFiles getRegisterFiles(){
          return registers;
     }
     
     public MemStation[] getMemStations(){
          return mem_rs;
     }
     
     public ALUStation[] getALUStations(){
          return alu_rs;
     }
     
     //Utility Functions
     
     ///
     ///Parse the comment and update the Register Files Accordingly
     ///
     private void parseComment(String comment){
          String[] split_1; //split on ","
          String[] split_2; //split on "="
          
          split_1 = comment.split(",");
          
          //Parse each register assignent in comment
          for( String split_1_sub : split_1 ){
               split_2 = split_1_sub.trim().split("=");               
               registers.setRegister( split_2[0].trim(), split_2[1].trim() );
          }
     }
     
     ///
     ///Classify an Instruction as Memory or Other
     ///
     private boolean classify( String opcode ){
          return( opcode.equals("L.D") || opcode.equals("LD") ||
                  opcode.equals("S.D") || opcode.equals("SD") );
         
     }
     
     
     
     
     
     
     
}
