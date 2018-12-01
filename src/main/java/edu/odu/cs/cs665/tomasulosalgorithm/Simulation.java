package edu.odu.cs.cs665.tomasulosalgorithm;

import java.io.IOException;
import java.io.File;

import java.util.HashMap;

/**
 * This class contains all simulation logic.
 */
public class Simulation {
     private OperationList operations;                          ///< List of instructions
     private RegisterFiles registers;                           ///< Register Files
     private ALUStation[] alu_rs;                               ///< ALU and Integer Reservation Stations
     private MemStation[] mem_rs;                               ///< Memory Reservation Stations

     private HashMap<String, Integer[]> instruction_to_station; ///< Mapping of instructions to Reservation Stations
     private HashMap<String, Integer> instruction_to_time;      ///< Mapping of instructions to Execution Time
     private HashMap<String, String> alias_to_register;         ///< Mapping of placeholder to Register

     private HashMap<String, Integer> memory_buffer;            ///< Mapping of operation issue numbers to ,emory locations

     private Clock clock;                                       ///< Clock Cycle Object
     private boolean is_initialized;                            ///< Whether the Simulation Instance is initialized

     /**
      * Simulation Constructor
      */
     public Simulation()
     {
          is_initialized = false;
     }

     /**
      * Initialize the simulation.
      *
      * @param dataFile input file containing the set of all instructions to
      *     simulate
      */
     public void initialize(File dataFile)
          throws Exception
     {
          clock = Clock.getInstance();

          OperationFileParser file_parser = new OperationFileParser(dataFile);

          //Iniatialize containers for instructions and registers
          operations = new OperationList();
          registers = new RegisterFiles();

          file_parser.parseFile(operations);

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

          // Create Mapping of instructions to the
          // appropiate Reservation Stations
          instruction_to_station = new HashMap<String, Integer[]>();

          //Memory Indices
          instruction_to_station.put("L.D", new Integer[]{0, 1});
          instruction_to_station.put("LD", new Integer[]{0, 1});
          instruction_to_station.put("S.D", new Integer[]{2, 3});
          instruction_to_station.put("SD", new Integer[]{2, 3});

          //ALU Indices
          instruction_to_station.put("DADDI", new Integer[]{0});
          instruction_to_station.put("DADDUI", new Integer[]{0});
          instruction_to_station.put("DADD", new Integer[]{1, 2});
          instruction_to_station.put("ADDD", new Integer[]{1, 2});
          instruction_to_station.put("ADD.D", new Integer[]{1, 2});
          instruction_to_station.put("DSUB", new Integer[]{1, 2});
          instruction_to_station.put("SUBD", new Integer[]{1, 2});
          instruction_to_station.put("SUB.D", new Integer[]{1, 2});
          instruction_to_station.put("MULD", new Integer[]{3, 4});
          instruction_to_station.put("MUL.D", new Integer[]{3, 4});
          instruction_to_station.put("MULTD", new Integer[]{3, 4});
          instruction_to_station.put("MULT.D", new Integer[]{3, 4});
          instruction_to_station.put("DIVD", new Integer[]{5, 6});
          instruction_to_station.put("DIV.D", new Integer[]{5, 6});

          //Create a mapping of instructions to execution time
          instruction_to_time = new HashMap<String, Integer>();
          //Memory Instructions
          instruction_to_time.put("L.D", Integer.valueOf(2));
          instruction_to_time.put("LD", Integer.valueOf(2));
          instruction_to_time.put("S.D", Integer.valueOf(2));
          instruction_to_time.put("SD", Integer.valueOf(2));

          //ALU Instructions
          instruction_to_time.put("DADDI", Integer.valueOf(1));
          instruction_to_time.put("DADDUI", Integer.valueOf(1));
          instruction_to_time.put("DADD", Integer.valueOf(4));
          instruction_to_time.put("ADDD", Integer.valueOf(4));
          instruction_to_time.put("ADD.D", Integer.valueOf(4));
          instruction_to_time.put("DSUB", Integer.valueOf(4));
          instruction_to_time.put("SUBD", Integer.valueOf(4));
          instruction_to_time.put("SUB.D", Integer.valueOf(4));
          instruction_to_time.put("MULD", Integer.valueOf(7));
          instruction_to_time.put("MUL.D", Integer.valueOf(7));
          instruction_to_time.put("MULTD", Integer.valueOf(7));
          instruction_to_time.put("MULT.D", Integer.valueOf(7));
          instruction_to_time.put("DIVD", Integer.valueOf(25));
          instruction_to_time.put("DIV.D", Integer.valueOf(25));

          //Mapping of aliases to Registers
          alias_to_register = new HashMap<String, String>();

          //Initalize the Memory Buffer
          memory_buffer = new HashMap<String, Integer>();

          //indicate that the simulation has been initialized
          is_initialized = true;
     }

     /**
      * Returns true when the simulation has finished
      */
     public boolean isComplete()
     {
          boolean complete = false;
          complete = !(operations.moreOperationsQueued());

          for (int i = 0; i < mem_rs.length && complete; i++) {
               complete = !mem_rs[i].isBusy();
          }

          for (int i = 0; i < alu_rs.length && complete; i++) {
               complete = !alu_rs[i].isBusy();
          }

          return complete;
     }

     /**
      * Perform one time step.
      */
     public void performStep()
     {
          Operation to_schedule;
          boolean op_scheduled = false;

          //increment the clock
          clock.increment();

          //Broadcast Reservation Station Results
          for (ALUStation it : alu_rs) {
               if (it.isResultReady()) {
                    it.finalizeResult();
                    broadcast(it.getName(), it.getResult());
               }
               if (it.isResultWritten()) {
                    it.clear();
               }
          }
          for (MemStation it : mem_rs) {
               if (it.isResultReady()) {
                    broadcast(it.getName(), it.getResult());
               }
               if (it.isResultWritten()) {
                    //System.out.prtinln
                    it.clear();
               }
          }

          //Update Reservation Stations -- perform cycle
          for (ALUStation it : alu_rs) {
               if (it.isReady() && it.isBusy()) {
                    it.performCycle();
               }
          }
          for (MemStation it : mem_rs) {
               if (it.isBusy() && it.hasPriority(memory_buffer) && it.isReady()) {
                    it.performCycle();
               }

          }

          //Get an instruction from the head of the list
          //if the list has not been exhausted
          if (operations.moreOperationsQueued()) {
               to_schedule = operations.getNextOperation();

               //Parse the Instruction Comment
               if (to_schedule.hasComment()) {
                    parseComment(to_schedule.getComment());
               }

               //Determine which set of Reservations Station(s) are
               //appropiate for an instruction
               Integer[] rs_indices = instruction_to_station.get(to_schedule.getOpcode());
               //Memory Stations
               if (classify(to_schedule.getOpcode())) {
                    for (int i = rs_indices[0]; i <= rs_indices[rs_indices.length - 1] && !op_scheduled; i++) {
                         if (!mem_rs[i].isBusy()) {
                              mem_rs[i].scheduleInstruction(to_schedule, registers, 2);
                              op_scheduled = true;

                              //Set the placeholder in the Register Files
                              //if the instruction is not a store
                              if (!MemStation.isStore(to_schedule.getOpcode())) {
                                   registers.setRegister(to_schedule.getOperand(1), mem_rs[i].getName());
                                   alias_to_register.put(mem_rs[i].getName(), to_schedule.getOperand(1));

                              }
                              mem_rs[i].hasPriority(memory_buffer);
                         }
                    }
               }
               //ALU Stations
               else {
                    for (int i = rs_indices[0]; i <= rs_indices[rs_indices.length - 1] && !op_scheduled; i++) {
                         if (!alu_rs[i].isBusy()) {
                              alu_rs[i].scheduleInstruction(to_schedule, registers,
                                                             instruction_to_time.get(to_schedule.getOpcode()));
                              op_scheduled = true;

                              registers.setRegister(to_schedule.getOperand(1), alu_rs[i].getName());
                              alias_to_register.put(alu_rs[i].getName(), to_schedule.getOperand(1));
                         }
                    }
               }

               if (op_scheduled) {
                    operations.increment();
               }
          }
     }

     /**
      * Return the current clock cycle
      */
     public int getCurrentCycle()
     {
          return clock.get();
     }

     /**
      * Return the Instruction List
      */
     public OperationList getOperationList()
     {
          return operations;
     }

     /**
      *Return the Regieter Files
      */
     public RegisterFiles getRegisterFiles()
     {
          return registers;
     }

     /**
      * Return the Memory Reservation Stations
      */
     public MemStation[] getMemStations()
     {
          return mem_rs;
     }

     /**
      * Return the ALU Reservation Stations
      */
     public ALUStation[] getALUStations()
     {
          return alu_rs;
     }

     //Utility Functions

     /**
      * Parse the comment and update the Register Files Accordingly
      */
     private void parseComment(String comment)
     {
          String[] split_1; //split on ","
          String[] split_2; //split on "="

          split_1 = comment.split(",");

          //Parse each register assignent in comment
          for (String split_1_sub : split_1) {
               split_2 = split_1_sub.trim().split("=");
               registers.setRegister(split_2[0].trim(), split_2[1].trim());
          }
     }

     /**
      * Classify an Instruction as Memory or Other
      */
     private boolean classify(String opcode)
     {
          return opcode.equals("L.D") || opcode.equals("LD")
              || opcode.equals("S.D") || opcode.equals("SD");

     }

     /**
      * Broadcast the Result to all Reservation Stations
      * Update the Register File
      */
     private void broadcast(String alias, String result) {
          String register; // the register to update

          //remove the mapping from the memory buffer
          if (memory_buffer.containsKey(result)) {
               memory_buffer.remove(result);
          }

          //broadcast to all Reservation Stations
          for (ALUStation it : alu_rs) {
               if (it.getQj().equals(alias)) {
                    it.setVj(result);
                    it.setQj(null);
               }
               if (it.getQk().equals(alias)) {
                    it.setVk(result);
                    it.setQk(null);
               }
          }

          for (MemStation it : mem_rs) {
               if (it.isBusy()) {
                    it.updateAddrComponents(alias, result);
               }
          }

          //Update the Registers
          if (alias_to_register.containsKey(alias)) {

               //ger the register that is mapped to the alias
               register = alias_to_register.get(alias);

               //overwrite the current register valu if the station name matched the alias
               if (alias.equals(registers.getRegister(register))) {
                    registers.setRegister(register, result);
               }
               alias_to_register.remove(alias);
          }
     }
}
