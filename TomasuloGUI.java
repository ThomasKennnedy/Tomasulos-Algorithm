import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.Dimension;
import javax.swing.border.TitledBorder;

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
	     setMinimumSize( new Dimension( 700, 500 ) );

          //setPreferredSize( new Dimension( 700, 600 ) );
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
          
          //Create the Instructions Table Model
          //Instructions Table Model
          op_model = new DefaultTableModel();
          op_model.setColumnIdentifiers( new Object[]{"Instruction"," ", "j", "k", "Issue", "Complete", "Write"} );                  
          //add instruction rows
          for( int i = 1; i <= 7; i++){
               op_model.addRow( new Object[]{("op"+i), "F"+(i*2), "F"+(i*4), "F"+(i*8), " ", " ", " "} );
          }

          //Create The Reservation Station Table Model
          rs_model = new DefaultTableModel();
          rs_model.setColumnIdentifiers( new Object[]{"Time", "Name","Busy", "Op", "Vj", "Vk", "Qj", "Qk"} );
          //Add Reservation Station Table Rows
          rs_model.addRow( new Object[]{""+0, "ADD1", " ", " ", " ", " ", " ", " "});
          rs_model.addRow( new Object[]{""+0, "ADD2", " ", " ", " ", " ", " ", " "});
          rs_model.addRow( new Object[]{""+0, "MULT1", " ", " ", " ", " ", " ", " "});
          rs_model.addRow( new Object[]{""+0, "MULT2", " ", " ", " ", " ", " ", " "});
          rs_model.addRow( new Object[]{""+0, "DIV1", " ", " ", " ", " ", " ", " "});
          rs_model.addRow( new Object[]{""+0, "DIV2", " ", " ", " ", " ", " ", " "});

          //Create the Register Table Model
          reg_model =  new DefaultTableModel();
          reg_model.addColumn("Clock");
          
          //Add Register Columns
          for( int i = 0; i < 32; i+=2){
               reg_model.addColumn( ("F"+i) );
          }
          
          //Add register table rows
          reg_model.addRow( new Object[]{""+0, " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "});
          
          //Create the tables
          op_table  = new JTable(op_model);
          rs_table  = new JTable(rs_model);
          reg_table = new JTable(reg_model);

          //Disallow editing of table fields
          op_table.setEnabled( false );
          rs_table.setEnabled( false );
          reg_table.setEnabled( false );
          
          //Disable the reorganization of columns
          op_table.getTableHeader().setReorderingAllowed(false);
          rs_table.getTableHeader().setReorderingAllowed(false);
          reg_table.getTableHeader().setReorderingAllowed(false);
          
          //Set Instruction Table Column Widths
          //Column 0 -(Operation
          op_table.getColumnModel().getColumn(0).setMinWidth(100);
          op_table.getColumnModel().getColumn(0).setMaxWidth(100);
          op_table.getColumnModel().getColumn(0).setPreferredWidth(100);
          //Columns [1,3]
          for( int i = 1; i <= 3; i++){  
               op_table.getColumnModel().getColumn(i).setMinWidth(60);
               op_table.getColumnModel().getColumn(i).setMaxWidth(60);
               op_table.getColumnModel().getColumn(i).setPreferredWidth(60);
          }
          //Columns [4,6]
          for( int i = 4; i <= 6; i++){  
               op_table.getColumnModel().getColumn(i).setMinWidth(80);
               op_table.getColumnModel().getColumn(i).setMaxWidth(80);
               op_table.getColumnModel().getColumn(i).setPreferredWidth(80);
          }
          
          //Set Reservation Station Table Column Widths
          //Set the first columnt -- leave default for the remaining columns
          rs_table.getColumnModel().getColumn(0).setMinWidth(80);
          rs_table.getColumnModel().getColumn(0).setMaxWidth(80);
          rs_table.getColumnModel().getColumn(0).setPreferredWidth(80);
       
          
          
          //Set Register Table Column widths
          reg_table.getColumnModel().getColumn(0).setMinWidth(60);
          reg_table.getColumnModel().getColumn(0).setMaxWidth(60);
          reg_table.getColumnModel().getColumn(0).setPreferredWidth(60);
          
          for( int i = 1; i <= 16; i++){  
               reg_table.getColumnModel().getColumn(i).setMinWidth(60);
               reg_table.getColumnModel().getColumn(i).setMaxWidth(60);
               reg_table.getColumnModel().getColumn(i).setPreferredWidth(60);
          }
          
          //Set Table Viewport size for Table JScrollPanes
          op_table.setPreferredScrollableViewportSize( op_table.getPreferredSize() );
          rs_table.setPreferredScrollableViewportSize( rs_table.getPreferredSize() );
          reg_table.setPreferredScrollableViewportSize( reg_table.getPreferredSize() );
          
          //Create Table JScrollPanes
          JScrollPane op_panel= new JScrollPane(op_table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                                                
          JScrollPane rs_panel = new JScrollPane(rs_table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                                                 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                                                 
          JScrollPane reg_panel = new JScrollPane(reg_table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                                                  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);   
          
          //Create Panel Title Borders
          TitledBorder control_border = BorderFactory.createTitledBorder( "Controls" );
          control_panel.setBorder( control_border );
          
          TitledBorder op_border = BorderFactory.createTitledBorder( "Instructions" );
          op_panel.setBorder( op_border );
          
          TitledBorder rs_border = BorderFactory.createTitledBorder( "ALU Stations" );
          rs_panel.setBorder( rs_border );
          
          TitledBorder reg_border = BorderFactory.createTitledBorder( "Registers" );
          reg_panel.setBorder( reg_border );
          
   
          
          
          
          
          
          
          //Add the tables to the content panel
          JPanel bottom_panel = new JPanel();
          bottom_panel.setLayout( new BorderLayout() );
          bottom_panel.add( rs_panel, BorderLayout.NORTH );
          bottom_panel.add( reg_panel, BorderLayout.SOUTH );
          
          //set the layout manager and create the constraints helper
          cp.setLayout( new GridBagLayout() );
          GridBagConstraints c = new GridBagConstraints();
          
          //add the controls panel
          c.fill = GridBagConstraints.HORIZONTAL;
          c.ipady     = 0;
          c.gridx     = 0;
          c.gridy     = 0;
          c.gridwidth = 3;  
          c.gridheight= 1;
	     cp.add( control_panel, c);
          
          //add the operations scroll panel
          c.fill = GridBagConstraints.HORIZONTAL;
          c.gridx     = 0;
          c.gridy     = 1;
          c.gridwidth = 1;
          c.gridheight= 2;          
	     cp.add( op_panel, c);          
          
          //add the Reservation Station panel
          c.fill = GridBagConstraints.HORIZONTAL;
          c.gridx     = 0;
          c.gridy     = 3;
          c.gridwidth = 3;
          c.gridheight= 1; 
          cp.add( rs_panel, c);
          
          //add the registers panel
          c.fill = GridBagConstraints.HORIZONTAL;
          c.gridx     = 0;
          c.gridy     = 4;
          c.gridwidth = 3;
          c.gridheight= 1; 
          cp.add( reg_panel, c);
          
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
                         
                                   //add instruction rows
                         for( int i = 1; i <= 7; i++){
                              op_model.addRow( new Object[]{("op"+i), "F"+(i*2), "F"+(i*4), "F"+(i*8), " ", " ", " "} );
                         }
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
