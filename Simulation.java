
public class Simulation{
     private OperationList operations;
     private RegisterFiles registers;
     private ALUStations alu_rs[8];
     private MemStations mem_rs[4];
     
     private Clock clock;

     private bool is_initialized;
     
     public Simulation(){
          is_initialized = false;
     }
     
     public initialize(String filename){
          clock = Clock.getInstance();
     
          OperationFileParser file_parser = new OperationFileParser( filename );
          
          operations = new OperationList();
          registers = new RegisterFiles();
                    
          file_parser.parseFile( operations, registers );

          alu_rs[0] = new ALUStation("ADD1")
          alu_rs[1] = new ALUStation("ADD2")
          alu_rs[2] = new ALUStation("SUB1")
          alu_rs[3] = new ALUStation("MUL2")
          alu_rs[4] = new ALUStation("MUL1")
          alu_rs[5] = new ALUStation("ADD2")
          alu_rs[6] = new ALUStation("ADD1")
          alu_rs[7] = new ALUStation("ADD2")
          alu_rs[7] = new ALUStation("ADD2")
          
          mem_rs[0] = new MemStation("LOAD1");
          mem_rs[1] = new MemStation("LOAD2");
          mem_rs[2] = new MemStation("STORE1");
          mem_rs[3] = new MemStation("STORE2");
          
          is_initialized = true;
     }
     
     public bool isComplete(){
          bool complete = false;          
          complete = !( operations.moreOperationsQueued() );
          
          for( int i = 0; i < mem_rs.size && complete; i++ ){
               complete = mem_rs[i].isReady() ;
          }
          
          for( int i = 0; i < alu_rs.size && complete; i++ ){
               complete = alu_rs[i].isReady() ;
          }
          
          return complete;          
     }
     
     public void performStep(){
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
          }          
     }
     
     public Clock getCurrentCycle(){
          return clock.get();
     }     
     
     public OperationList getOperationList(){
          return operations;
     }

     public registerFiles getRegisterFiles(){
          return registers;
     }
     
     public MemStations getMemStations(){
          return mem_rs;
     }
     
     public ALUStations getALUStations(){
          return alu_rs;
     }
}
