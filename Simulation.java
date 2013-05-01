import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.*;

public class Simulation{
     private OperationList operations;
     private RegisterFiles registers;
     private ALUStation[] alu_rs;
     private MemStation[] mem_rs;
     
     private Clock clock;

     private boolean is_initialized;
     
     public Simulation(){
          is_initialized = false;
     }
     
     public void initialize(File data_file) throws Exception{
          clock = Clock.getInstance();
     
          OperationFileParser file_parser = new OperationFileParser( data_file );
          
          operations = new OperationList();
          registers = new RegisterFiles();
                    
          file_parser.parseFile( operations );
          
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
     
     public void performStep(){/*
          Operation to_schedule;
          bool op_scheduled = false;
          
          //Update Reservation Stations
          for( MemStation it : mem_rs ){
               if( !it.isWaiting() ){
                    it.performCycle();
               }
               
               if( it.isResultReady() ){
                    broadcast( it.getResult() );
                    it.clear();
               }    
          }          
          for( MemStation it : mem_rs ){
               if( !it.isWaiting() ){
                    it.performCycle();
               }
               
               if( it.isResultReady() ){
                    broadcast( it.getResult() );
                    it.clear();
               }    
          }
          
          //Operation Scheduling
          to_schedule =  operations.getNextOperation();
          
          
          for( int i = 0; i < rs_list.size && op_scheduled; i++ ){
               if( rs_list[i].isSupportedInstruction(to_schedule.getOpCode()) && rs_list[i].isReady()){
                    rs_list[i].scheduleInstruction( to_schedule )
                    op_scheduled = true;
               }
          }
          
          if( op_scheduled ){
               operations.increment();
          }        */  
          operations.increment();
          clock.increment();
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
}
