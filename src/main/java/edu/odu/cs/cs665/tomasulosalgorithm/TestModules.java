package edu.odu.cs.cs665.tomasulosalgorithm;

import java.io.File;

/**
 * This class contaisn all moduling testing.
 */
public class TestModules {

     public static void main(String[] args)
     {
          OperationList testList = new OperationList();
          OperationFileParser testParser = new OperationFileParser(new File("test-input/a1.in"));
          RegisterFiles testRegs = new RegisterFiles();

          //Parse a test input file
          System.out.println("Operation Test");
          try {
               testParser.parseFile(testList);

               System.out.println(testList);
          }
          catch (Exception e) {
               System.out.println(e);
          }

          // Reservation Stations
          MemStation memTest =  new MemStation("Load1");
          ALUStation aluTest =  new ALUStation("Add1");

          System.out.println("Reservation Station Test");
          System.out.println(memTest.getName());
          System.out.println(aluTest.getName());
     }
}
