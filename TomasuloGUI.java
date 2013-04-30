import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.Dimension;

import java.io.*;
import java.net.*;

public class TomasuloGUI extends JFrame {
     JPanel control_panel;
     JPanel content_panel;
     
     JLabel file_label;
     
     //Tables
     JTable op_table;
     JTable rs_table;
     JTable reg_table;
     
     //Table Models
     DefaultTableModel op_model;
     DefaultTableModel rs_model;
     DefaultTableModel reg_model;     
     
     JButton file_button; 
     JButton start_button;
     JButton step_button;
     JButton all_steps_button;
     
     JTextField file_field;

     public TomasuloGUI(){
          super("Tomsulo's Simulation");
          setLocation(50,75);
	     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     setMinimumSize( new Dimension( 800, 600 ) );
	     Container cp = getContentPane();
	     
          //create the container panels
	     control_panel = new JPanel();
          content_panel = new JPanel();
          
          //Create the control Elements
          file_label       = new JLabel("Input File:");
          file_field       = new JTextField("a.in", 8);
          file_button      = new JButton("Browse");
          start_button     = new JButton("Start"); 
          step_button      = new JButton("Step");
          all_steps_button = new JButton("Run All");
          
          control_panel.setLayout( new FlowLayout() );
          
          control_panel.add( file_label );
          control_panel.add( file_field );
          control_panel.add( file_button );          
          control_panel.add( start_button );   

          file_field.setEnabled( false );
          
          content_panel = new JPanel();
          
          //Create the table Models
          op_model = new DefaultTableModel();
          op_model.setColumnIdentifiers( new Object[]{"Instruction"," ", "j", "k", "Issue", "Complete", "Write"} );
          
          rs_model = new DefaultTableModel();
          rs_model.setColumnIdentifiers( new Object[]{"Time", "Name","Busy", "Op", "Vj", "Vk", "Qj", "Qk"} );
          
          reg_model =  new DefaultTableModel();
          reg_model.addColumn("Clock");
          
          for( int i = 0; i < 32; i+=2){
               reg_model.addColumn( ("F"+i) );
          }
          
          
          for( int i = 1; i <= 7; i++){
               op_model.addRow( new Object[]{("op"+i), "F"+(i*2), "F"+(i*4), "F"+(i*8), " ", " ", " "} );
          }
          
          for( int i = 1; i <= 5; i++){
               rs_model.addRow( new Object[]{""+0, "Name"+i, " ", " ", " ", " ", " ", " "});
          }
          

          reg_model.addRow( new Object[]{""+0, " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "});
          
          //Create the tables
          op_table  = new JTable(op_model);
          rs_table  = new JTable(rs_model);
          reg_table = new JTable(reg_model);   
          
          op_table.setPreferredScrollableViewportSize( op_table.getPreferredSize() );
          rs_table.setPreferredScrollableViewportSize( rs_table.getPreferredSize() );
          reg_table.setPreferredScrollableViewportSize( reg_table.getPreferredSize() );
          
          JScrollPane op_panel= new JScrollPane(op_table);
          JScrollPane rs_panel = new JScrollPane(rs_table);
          JScrollPane reg_panel = new JScrollPane(reg_table);
          
          //Add the tables to the content panel
          //content_panel.add(  );         
          
          JPanel bottom_panel = new JPanel();
          bottom_panel.setLayout( new BorderLayout() );
          bottom_panel.add( rs_panel, BorderLayout.NORTH );
          bottom_panel.add( reg_panel, BorderLayout.SOUTH );
          
          cp.setLayout( new BorderLayout() );
	     cp.add( control_panel, BorderLayout.NORTH);
	     cp.add( op_panel, BorderLayout.WEST);
          cp.add( bottom_panel, BorderLayout.SOUTH);
          
          //Set Action Listeners( Event Handlers )
          start_button.addActionListener(
               new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                         //remove the old elements
                         control_panel.remove( file_button );
                         control_panel.remove( start_button );
                         control_panel.invalidate();
                         //add the new controls
                         control_panel.add(step_button);
                         control_panel.add(all_steps_button);                        
                         control_panel.validate();
                         
                         //refresh the panel
                         control_panel.repaint();
                    }
               }
          );
     
          pack();
     }
     
     
        
     public static void main(String args[])
     {
          new TomasuloGUI().setVisible(true);
     }




}
