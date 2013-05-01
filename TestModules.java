import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.Dimension;
import javax.swing.border.TitledBorder;

import java.io.*;
import java.net.*;

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
          
          System.out.println("Register Files Test");
          System.out.println( test_regs );
          
          
     }


}
