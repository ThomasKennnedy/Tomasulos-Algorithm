package edu.odu.cs.cs665.tomasulosalgorithm;

import java.io.File;
import java.io.FileReader;

import java.util.HashMap;

/**
 * This class contains all simulation logic.
 */
public class Simulation {
    private OperationList operations;                          ///< List of instructions
    private RegisterFiles registers;                           ///< Register Files
    private ALUStation[] aluRs;                               ///< ALU and Integer Reservation Stations
    private MemStation[] memRs;                               ///< Memory Reservation Stations

    private HashMap<String, Integer[]> instructionToStation; ///< Mapping of instructions to Reservation Stations
    private HashMap<String, Integer> instructionToTime;      ///< Mapping of instructions to Execution Time
    private HashMap<String, String> aliasToRegister;         ///< Mapping of placeholder to Register

    private HashMap<String, Integer> memoryBuffer;            ///< Mapping of operation issue numbers to ,emory locations

    private Clock clock;                                       ///< Clock Cycle Object
    private boolean isInitialized;                            ///< Whether the Simulation Instance is initialized

    /**
     * Simulation Constructor
     */
    public Simulation()
    {
        isInitialized = false;
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

        FileReader dataFileReader = new FileReader(dataFile);
        OperationFileParser fileParser = new OperationFileParser(dataFileReader);

        //Iniatialize containers for instructions and registers
        operations = new OperationList();
        registers = new RegisterFiles();

        fileParser.parseFile(operations);

        //Create and intialize Reservation Stations
        aluRs = new ALUStation[7];
        memRs = new MemStation[4];

        aluRs[0] = new ALUStation("Int1");
        aluRs[1] = new ALUStation("Add1");
        aluRs[2] = new ALUStation("Add2");
        aluRs[3] = new ALUStation("Mul1");
        aluRs[4] = new ALUStation("Mul2");
        aluRs[5] = new ALUStation("Div1");
        aluRs[6] = new ALUStation("Div2");

        memRs[0] = new MemStation("Load1");
        memRs[1] = new MemStation("Load2");
        memRs[2] = new MemStation("Store1");
        memRs[3] = new MemStation("Store2");

        // Create Mapping of instructions to the
        // appropiate Reservation Stations
        instructionToStation = new HashMap<String, Integer[]>();

        //Memory Indices
        instructionToStation.put("L.D", new Integer[]{0, 1});
        instructionToStation.put("LD", new Integer[]{0, 1});
        instructionToStation.put("S.D", new Integer[]{2, 3});
        instructionToStation.put("SD", new Integer[]{2, 3});

        //ALU Indices
        instructionToStation.put("DADDI", new Integer[]{0});
        instructionToStation.put("DADDUI", new Integer[]{0});
        instructionToStation.put("DADD", new Integer[]{1, 2});
        instructionToStation.put("ADDD", new Integer[]{1, 2});
        instructionToStation.put("ADD.D", new Integer[]{1, 2});
        instructionToStation.put("DSUB", new Integer[]{1, 2});
        instructionToStation.put("SUBD", new Integer[]{1, 2});
        instructionToStation.put("SUB.D", new Integer[]{1, 2});
        instructionToStation.put("MULD", new Integer[]{3, 4});
        instructionToStation.put("MUL.D", new Integer[]{3, 4});
        instructionToStation.put("MULTD", new Integer[]{3, 4});
        instructionToStation.put("MULT.D", new Integer[]{3, 4});
        instructionToStation.put("DIVD", new Integer[]{5, 6});
        instructionToStation.put("DIV.D", new Integer[]{5, 6});

        //Create a mapping of instructions to execution time
        instructionToTime = new HashMap<String, Integer>();
        //Memory Instructions
        instructionToTime.put("L.D", Integer.valueOf(2));
        instructionToTime.put("LD", Integer.valueOf(2));
        instructionToTime.put("S.D", Integer.valueOf(2));
        instructionToTime.put("SD", Integer.valueOf(2));

        //ALU Instructions
        instructionToTime.put("DADDI", Integer.valueOf(1));
        instructionToTime.put("DADDUI", Integer.valueOf(1));
        instructionToTime.put("DADD", Integer.valueOf(4));
        instructionToTime.put("ADDD", Integer.valueOf(4));
        instructionToTime.put("ADD.D", Integer.valueOf(4));
        instructionToTime.put("DSUB", Integer.valueOf(4));
        instructionToTime.put("SUBD", Integer.valueOf(4));
        instructionToTime.put("SUB.D", Integer.valueOf(4));
        instructionToTime.put("MULD", Integer.valueOf(7));
        instructionToTime.put("MUL.D", Integer.valueOf(7));
        instructionToTime.put("MULTD", Integer.valueOf(7));
        instructionToTime.put("MULT.D", Integer.valueOf(7));
        instructionToTime.put("DIVD", Integer.valueOf(25));
        instructionToTime.put("DIV.D", Integer.valueOf(25));

        //Mapping of aliases to Registers
        aliasToRegister = new HashMap<String, String>();

        //Initalize the Memory Buffer
        memoryBuffer = new HashMap<String, Integer>();

        //indicate that the simulation has been initialized
        isInitialized = true;
    }

    /**
     * Returns true when the simulation has finished.
     */
    public boolean isComplete()
    {
        boolean complete = false;
        complete = !(operations.moreOperationsQueued());

        for (int i = 0; i < memRs.length && complete; i++) {
            complete = !memRs[i].isBusy();
        }

        for (int i = 0; i < aluRs.length && complete; i++) {
            complete = !aluRs[i].isBusy();
        }

        return complete;
    }

    /**
     * Perform one time step.
     */
    public void performStep()
    {
        Operation toSchedule;
        boolean opScheduled = false;

        //increment the clock
        clock.increment();

        //Broadcast Reservation Station Results
        for (ALUStation it : aluRs) {
            if (it.isResultReady()) {
                it.finalizeResult();
                broadcast(it.getName(), it.getResult());
            }
            if (it.isResultWritten()) {
                it.clear();
            }
        }
        for (MemStation it : memRs) {
            if (it.isResultReady()) {
                broadcast(it.getName(), it.getResult());
            }
            if (it.isResultWritten()) {
                //System.out.prtinln
                it.clear();
            }
        }

        //Update Reservation Stations -- perform cycle
        for (ALUStation it : aluRs) {
            if (it.isReady() && it.isBusy()) {
                it.performCycle();
            }
        }
        for (MemStation it : memRs) {
            if (it.isBusy() && it.hasPriority(memoryBuffer) && it.isReady()) {
                it.performCycle();
            }
        }

        //Get an instruction from the head of the list
        //if the list has not been exhausted
        if (operations.moreOperationsQueued()) {
            toSchedule = operations.getNextOperation();

            //Parse the Instruction Comment
            if (toSchedule.hasComment()) {
                 parseComment(toSchedule.getComment());
            }

            //Determine which set of Reservations Station(s) are
            //appropiate for an instruction
            Integer[] rsIndices = instructionToStation.get(toSchedule.getOpcode());
            //Memory Stations
            if (classify(toSchedule.getOpcode())) {
                for (int i = rsIndices[0]; i <= rsIndices[rsIndices.length - 1] && !opScheduled; i++) {
                    if (!memRs[i].isBusy()) {
                        memRs[i].scheduleInstruction(toSchedule, registers, 2);
                        opScheduled = true;

                        //Set the placeholder in the Register Files
                        //if the instruction is not a store
                        if (!MemStation.isStore(toSchedule.getOpcode())) {
                            registers.setRegister(toSchedule.getOperand(1), memRs[i].getName());
                            aliasToRegister.put(memRs[i].getName(), toSchedule.getOperand(1));

                        }
                        memRs[i].hasPriority(memoryBuffer);
                    }
                }
            }
            //ALU Stations
            else {
                for (int i = rsIndices[0]; i <= rsIndices[rsIndices.length - 1] && !opScheduled; i++) {
                    if (!aluRs[i].isBusy()) {
                        aluRs[i].scheduleInstruction(toSchedule, registers,
                                                      instructionToTime.get(toSchedule.getOpcode()));
                        opScheduled = true;

                        registers.setRegister(toSchedule.getOperand(1), aluRs[i].getName());
                        aliasToRegister.put(aluRs[i].getName(), toSchedule.getOperand(1));
                    }
                }
            }

            if (opScheduled) {
                operations.increment();
            }
        }
    }

    /**
     * Return the current clock cycle.
     */
    public int getCurrentCycle()
    {
        return clock.get();
    }

    /**
     * Return the Instruction List.
     */
    public OperationList getOperationList()
    {
        return operations;
    }

    /**
     * Return the Regieter Files.
     */
    public RegisterFiles getRegisterFiles()
    {
        return registers;
    }

    /**
     * Return the Memory Reservation Stations.
     */
    public MemStation[] getMemStations()
    {
        return memRs;
    }

    /**
     * Return the ALU Reservation Stations.
     */
    public ALUStation[] getALUStations()
    {
        return aluRs;
    }

    //Utility Functions

    /**
     * Parse the comment and update the Register Files Accordingly.
     */
    private void parseComment(String comment)
    {
        String[] split1; //split on ","
        String[] split2; //split on "="

        split1 = comment.split(",");

        //Parse each register assignent in comment
        for (String split1Sub : split1) {
            split2 = split1Sub.trim().split("=");
            registers.setRegister(split2[0].trim(), split2[1].trim());
        }
    }

    /**
     * Classify an Instruction as Memory or Other.
     */
    private boolean classify(String opcode)
    {
        return opcode.equals("L.D") || opcode.equals("LD")
            || opcode.equals("S.D") || opcode.equals("SD");

    }

    /**
     * Broadcast the Result to all Reservation Stations
     * Update the Register File.
     */
    private void broadcast(String alias, String result) {
        String register; // the register to update

        //remove the mapping from the memory buffer
        if (memoryBuffer.containsKey(result)) {
            memoryBuffer.remove(result);
        }

        //broadcast to all Reservation Stations
        for (ALUStation it : aluRs) {
            if (it.getQj().equals(alias)) {
                it.setVj(result);
                it.setQj(null);
            }
            if (it.getQk().equals(alias)) {
                it.setVk(result);
                it.setQk(null);
            }
        }

        for (MemStation it : memRs) {
            if (it.isBusy()) {
                it.updateAddrComponents(alias, result);
            }
        }

         //Update the Registers
        if (aliasToRegister.containsKey(alias)) {
            // get the register that is mapped to the alias
            register = aliasToRegister.get(alias);

            // overwrite the current register valu if the station name matched
            // the alias
            if (alias.equals(registers.getRegister(register))) {
                registers.setRegister(register, result);
            }
            aliasToRegister.remove(alias);
        }
    }
}
