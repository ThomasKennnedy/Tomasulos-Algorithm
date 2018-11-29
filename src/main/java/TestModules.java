import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.Dimension;
import javax.swing.border.TitledBorder;

import java.io.*;
import java.net.*;

///
/// This class contaisn all moduling testing.
///
public class TestModules{
     
     public static void main( String args[] ){
          OperationList test_list = new OperationList();
          OperationFileParser test_parser = new OperationFileParser( new File( "test-input/a1.in"  ) );
          RegisterFiles test_regs = new RegisterFiles();
          
          //Parse a test input file
          System.out.println("Operation Test");
          try{
               test_parser.parseFile( test_list );
               
               System.out.println( test_list );
          }
          catch(Exception e){
               System.out.println( e );
          }
          
          //Reservation Stations          
          MemStation mem_test =  new MemStation("Load1");
          ALUStation alu_test =  new ALUStation("Add1");
          
          System.out.println("Reservation Station Test");
          System.out.println( mem_test.getName() );
          System.out.println( alu_test.getName() );
          

          
     }


}
