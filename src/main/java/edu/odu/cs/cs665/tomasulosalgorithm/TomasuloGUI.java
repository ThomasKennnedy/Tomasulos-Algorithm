package edu.odu.cs.cs665.tomasulosalgorithm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.TitledBorder;
import java.util.Map;

import java.io.*;

///
/// This class provides all GUI functionality.
///
public class TomasuloGUI extends JFrame {
     JPanel control_panel; ///< Container panel for controls
     JPanel status_panel;  ///< Container panel for status

     JLabel file_label; ///< Filename label

     //Tables
     JTable op_table;  ///< Instructions Table
     JTable rs_table;  ///< ALU Reservation Station Table
     JTable mrs_table; ///< Memory Reservations Station Table
     JTable irs_table; ///< Integer Reservation Station Table
     JTable reg_table; ///< Register File Table

     //Table Models
     DefaultTableModel op_model;  ///< Instructions Table Model
     DefaultTableModel rs_model;  ///< ALU Reservation Station Table Model
     DefaultTableModel mrs_model; ///< Memory Reservations Station Table Mdoel
     DefaultTableModel irs_model; ///< Integer Reservation Station Table Model
     DefaultTableModel reg_model; ///< Register File Table Model

     JButton load_button; ///< Load button
     JButton step_button; ///< Step button

     JTextField file_field;
     JTextField clocks_field; ///< Clock display field

     //Simulation Object
     Simulation sim_instance; ///< Simulation Wrapper Object

     //File Objects
     final JFileChooser file_dialog = new JFileChooser(); ///< File selection dialog
     File instructions_file; ///< Instructions File object

     ///
     /// The GUI Constructor
     ///
     public TomasuloGUI(){
          super("Tomsulo's Simulation");
          setLocation(50,75);
	     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     setMinimumSize(new Dimension( 700, 500 ));

          //setPreferredSize( new Dimension( 700, 600 ) );
	     Container cp = getContentPane();
	
          //create the container panels
	     control_panel = new JPanel();
	     status_panel = new JPanel();

          //Create the control elements
          file_label       = new JLabel("Input File:");
          file_field       = new JTextField("a.in", 16);
          load_button      = new JButton("Load");
          step_button      = new JButton("Step");

          //Add the control elements
          control_panel.setLayout(new FlowLayout());

          control_panel.add(file_label);
          control_panel.add(file_field);
          control_panel.add(load_button);
          control_panel.add(step_button);

          //Disable Selected Control Fields
          file_field.setEnabled(false);
          step_button.setEnabled(false);

          //Create the status elements
          status_panel.setLayout(new FlowLayout());
          clocks_field =  new JTextField("0", 8);
          clocks_field.setEnabled(false);
          //instr_label =  new JLabel("Instructions: ");

          //Add the status elements
          status_panel.add(clocks_field);
          //status_panel.add(instr_label);

          //Create the Instructions Table Model
          op_model = new DefaultTableModel();
          op_model.setColumnIdentifiers(new Object[]{"Instruction"," ", "j", "k", "Issue", "Complete", "Write"});
          //add instruction rows
          for (int i = 1; i <= 7; i++){
               op_model.addRow(new Object[]{ " ", " ", " ", " ", " ", " ", " "});
          }

          //Create The Reservation Station Table Model
          rs_model = new DefaultTableModel();
          rs_model.setColumnIdentifiers( new Object[]{"Time", "Name","Busy", "Op", "Vj", "Vk", "Qj", "Qk"});
          //Add Reservation Station Table Rows
          rs_model.addRow(new Object[]{""+0, "Int1", " ", " ", " ", " ", " ", " "});
          rs_model.addRow(new Object[]{""+0, "Add1", " ", " ", " ", " ", " ", " "});
          rs_model.addRow(new Object[]{""+0, "Add2", " ", " ", " ", " ", " ", " "});
          rs_model.addRow(new Object[]{""+0, "Mult1", " ", " ", " ", " ", " ", " "});
          rs_model.addRow(new Object[]{""+0, "Mult2", " ", " ", " ", " ", " ", " "});
          rs_model.addRow(new Object[]{""+0, "Div1", " ", " ", " ", " ", " ", " "});
          rs_model.addRow(new Object[]{""+0, "Div2", " ", " ", " ", " ", " ", " "});


          //Create The Reservation Station Table Model
          mrs_model = new DefaultTableModel();
          mrs_model.setColumnIdentifiers( new Object[]{"Name","Busy", "Address" });
          //Add Reservation Station Table Rows
          mrs_model.addRow(new Object[]{"Load1",  " ", " "});
          mrs_model.addRow(new Object[]{"Load2",  " ", " "});
          mrs_model.addRow(new Object[]{"Store1", " ", " "});
          mrs_model.addRow(new Object[]{"Store2", " ", " "});

          //Create the Register Table Model
          reg_model =  new DefaultTableModel();
          reg_model.addColumn("FP");
          reg_model.addColumn("Value");
          reg_model.addColumn("Int");
          reg_model.addColumn("Value");

          //Ad Initial Register Table Rows
          for (int i = 0; i < 14; i++ ){
               reg_model.addRow(new Object[]{ " ", " " , " ", " " });
          }

          //Create the tables
          op_table  = new JTable(op_model);
          rs_table  = new JTable(rs_model);
          mrs_table = new JTable(mrs_model);
          reg_table = new JTable(reg_model);


          //Disallow editing of table fields
          op_table.setEnabled( false );
          rs_table.setEnabled( false );
          mrs_table.setEnabled( false );
          reg_table.setEnabled( false );

          //Disable the reorganization of columns
          op_table.getTableHeader().setReorderingAllowed(false);
          rs_table.getTableHeader().setReorderingAllowed(false);
          mrs_table.getTableHeader().setReorderingAllowed(false);
          reg_table.getTableHeader().setReorderingAllowed(false);

          //Set Instruction Table Column Widths
          //Column 0 -(Operation
          op_table.getColumnModel().getColumn(0).setMinWidth(100);
          op_table.getColumnModel().getColumn(0).setMaxWidth(100);
          op_table.getColumnModel().getColumn(0).setPreferredWidth(100);
          //Columns [1,3]
          for (int i = 1; i <= 3; i++){
               op_table.getColumnModel().getColumn(i).setMinWidth(60);
               op_table.getColumnModel().getColumn(i).setMaxWidth(60);
               op_table.getColumnModel().getColumn(i).setPreferredWidth(60);
          }
          //Columns [4,6]
          for (int i = 4; i <= 6; i++){
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
          //FP Name Column
          reg_table.getColumnModel().getColumn(0).setMinWidth(60);
          reg_table.getColumnModel().getColumn(0).setMaxWidth(60);
          reg_table.getColumnModel().getColumn(0).setPreferredWidth(60);
          //FP Value Column
          reg_table.getColumnModel().getColumn(1).setMinWidth(100);
          reg_table.getColumnModel().getColumn(1).setMaxWidth(100);
          reg_table.getColumnModel().getColumn(1).setPreferredWidth(100);
          //Int Name Column
          reg_table.getColumnModel().getColumn(2).setMinWidth(60);
          reg_table.getColumnModel().getColumn(2).setMaxWidth(60);
          reg_table.getColumnModel().getColumn(2).setPreferredWidth(60);
          //Int Value Column
          reg_table.getColumnModel().getColumn(3).setMinWidth(100);
          reg_table.getColumnModel().getColumn(3).setMaxWidth(100);
          reg_table.getColumnModel().getColumn(3).setPreferredWidth(100);


          //Set Table Viewport size for Table JScrollPanes
          op_table.setPreferredScrollableViewportSize( op_table.getPreferredSize() );
          rs_table.setPreferredScrollableViewportSize( rs_table.getPreferredSize() );
          mrs_table.setPreferredScrollableViewportSize( mrs_table.getPreferredSize() );
          reg_table.setPreferredScrollableViewportSize( reg_table.getPreferredSize() );

          //Create Table JScrollPanes
          JScrollPane op_panel= new JScrollPane(op_table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

          JScrollPane rs_panel = new JScrollPane(rs_table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

          JScrollPane mrs_panel = new JScrollPane(mrs_table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

          JScrollPane reg_panel = new JScrollPane(reg_table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

          //Create Panel Title Borders
          TitledBorder control_border = BorderFactory.createTitledBorder( "Controls" );
          control_panel.setBorder( control_border );

          TitledBorder status_border = BorderFactory.createTitledBorder( "Clock" );
          status_panel.setBorder( status_border );

          TitledBorder op_border = BorderFactory.createTitledBorder( "Instructions" );
          op_panel.setBorder( op_border );

          TitledBorder rs_border = BorderFactory.createTitledBorder( "ALU Stations" );
          rs_panel.setBorder( rs_border );

          TitledBorder mrs_border = BorderFactory.createTitledBorder( "Memory Stations" );
          mrs_panel.setBorder( mrs_border );

          TitledBorder reg_border = BorderFactory.createTitledBorder( "Registers" );
          reg_panel.setBorder( reg_border );



          //set the layout manager and create the constraints helper
          cp.setLayout( new GridBagLayout() );
          GridBagConstraints c = new GridBagConstraints();

          //add the controls panel
          c.fill = GridBagConstraints.HORIZONTAL;
          c.ipady     = 0;
          c.gridx     = 0;
          c.gridy     = 0;
          c.gridwidth = 2;
          c.gridheight= 1;
	     cp.add( control_panel, c);
	
          //add the status panel
          c.fill = GridBagConstraints.HORIZONTAL;
          c.ipady     = 0;
          c.gridx     = 2;
          c.gridy     = 0;
          c.gridwidth = 1;
          c.gridheight= 1;
	     cp.add( status_panel, c);

          //add the operations scroll panel
          c.fill = GridBagConstraints.HORIZONTAL;
          c.gridx     = 0;
          c.gridy     = 1;
          c.gridwidth = 2;
          c.gridheight= 1;
	     cp.add( op_panel, c);

          //add the Reservation Station panel
          c.fill = GridBagConstraints.HORIZONTAL;
          c.gridx     = 0;
          c.gridy     = 3;
          c.gridwidth = 3;
          c.gridheight= 1;
          cp.add( rs_panel, c);

          //add the Memory Reservation Station panel
          c.fill = GridBagConstraints.HORIZONTAL;
          c.gridx     = 0;
          c.gridy     = 2;
          c.gridwidth = 2;
          c.gridheight= 1;
          cp.add( mrs_panel, c);


          //add the Registers panel
          c.fill = GridBagConstraints.HORIZONTAL;
          c.gridx     = 2;
          c.gridy     = 1;
          c.gridwidth = 1;
          c.gridheight= 2;
          cp.add( reg_panel, c);

          //Set Action Listeners( Event Handlers )
          //File Load Listener
          load_button.addActionListener(
               new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                         //Open the file selection Dialog
                         int file_result = file_dialog.showOpenDialog( TomasuloGUI.this );

                         if( file_result == JFileChooser.APPROVE_OPTION ){
                              instructions_file = file_dialog.getSelectedFile();
                              file_field.setText( instructions_file.getName() );



                              //construct the simulation
                              sim_instance = new Simulation();
                              try{
                                   sim_instance.initialize( instructions_file );
                                   load_button.setEnabled( false );
                                   step_button.setEnabled( true );
                              }
                              catch(Exception ex){
                                   javax.swing.JOptionPane.showMessageDialog( new JFrame(), ex.toString() );
                              }
                         }

                         //Add additional instruction rows if needed
                         //this only needs to run once as the number instructions
                         //remains constant
                         OperationList update_list = sim_instance.getOperationList();

                         while( op_model.getRowCount() < update_list.getNumberOfOperations() ) {
                              op_model.addRow( new Object[]{ " ", " ", " ", " ", " ", " ", " "} );
                         }



                         //Populate the tables
                         updateInstructionTable();
                         updateRegisterTable();
                         updateMemTable();
                         updateALUTable();


                    }
               }
          );
          //Step Listener
          step_button.addActionListener(
               new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                         sim_instance.performStep();

                         //update UI
                         //update clock
                         clocks_field.setText( ""+sim_instance.getCurrentCycle() );
                         //update tables
                         updateInstructionTable();
                         updateRegisterTable();
                         updateMemTable();
                         updateALUTable();

                         //Check if the simulation has finished
                         if( sim_instance.isComplete() ){
                              javax.swing.JOptionPane.showMessageDialog( new JFrame(), "Simulation Complete" );
                              step_button.setEnabled( false );
                         }


                    }
               }
          );

          pack();
     }

     ///
     /// Instruction Table Update Helper
     ///
     private void updateInstructionTable()
     {
          OperationList update_list = sim_instance.getOperationList();

          //Populate Operations table with the instructions
          for (int i = 0; i < update_list.getNumberOfOperations(); i++){
               Operation it_op = update_list.getOperation( i+1 );
               //obtain the write time
               int write_time = it_op.getWriteTime();

               //Opcode
               op_model.setValueAt( it_op.getOpcode(), i, 0);
               //Operands
               op_model.setValueAt( it_op.getOperand(1), i, 1);
               op_model.setValueAt( it_op.getOperand(2), i, 2);

               if( it_op.getNumberOfOperands() > 2 ){
                    op_model.setValueAt( it_op.getOperand(3), i, 3);
               }

               op_model.setValueAt( it_op.getExecution(), i, 5);
               op_model.setValueAt( (write_time == -1 ? " " : write_time ), i, 6);

               //Issue Number - display once the instruction has been scheduled
               op_model.setValueAt( (it_op.isScheduled() ? it_op.getIssueNum() : " " ), i, 4);
          }
     }

     ///
     /// Register Table Update Helper
     ///
     private void updateRegisterTable()
     {
          int temp_it_index = 0; // tempory index tracker

          //Get the Integer Registers
          Map<String, String> temp_int_reg = sim_instance.getRegisterFiles().getIntegerRegisters();
          //Get the FP Registers
          Map<String, String> temp_fp_reg = sim_instance.getRegisterFiles().getFPRegisters();

          //add rows if needed
          while (temp_int_reg.size() > reg_model.getRowCount()
              || temp_fp_reg.size() > reg_model.getRowCount()) {
               reg_model.addRow(new Object[]{ " ", " ", " ", " "});
          }

          //Update Integer Table Values
          for (Map.Entry<String, String> int_entry : temp_int_reg.entrySet()) {
               reg_model.setValueAt(int_entry.getKey(), temp_it_index, 2);
               reg_model.setValueAt(int_entry.getValue(), temp_it_index, 3);

               temp_it_index++;
          }

          temp_it_index = 0;
          //Update Integer Table Values
          for (Map.Entry<String, String> fp_entry : temp_fp_reg.entrySet()) {
               reg_model.setValueAt(fp_entry.getKey(), temp_it_index, 0);
               reg_model.setValueAt(fp_entry.getValue(), temp_it_index, 1);

               temp_it_index++;
          }
     }

     ///
     /// MemStations Table Update Helper
     ///
     private void updateMemTable()
     {
          //Get a copy of the memory stations
          MemStation[] temp_ms = sim_instance.getMemStations();

          //Update the table with current values for the stations
          for (int i = 0; i < temp_ms.length; i++ ){
               //generate a meaningfull representation of busy
               String busy_desc = (temp_ms[i].isBusy() ? "Yes" : "No" );

               mrs_model.setValueAt(temp_ms[i].getName(), i, 0);
               mrs_model.setValueAt(busy_desc, i, 1);
               mrs_model.setValueAt(temp_ms[i].getAddress(), i, 2);
          }
     }

     ///
     /// ALUStations Table Update Helper
     ///
     private void updateALUTable()
     {
          //Get a copy of the memory stations
          ALUStation[] temp_alu = sim_instance.getALUStations();

          //Update the table with current values for the stations
          for (int i =0; i < temp_alu.length; i++ ){
               //generate a meaningfull representation of busy
               String busy_desc = (temp_alu[i].isBusy() ? "Yes" : "No" );

               rs_model.setValueAt(((temp_alu[i].isReady() && temp_alu[i].isBusy()) ? temp_alu[i].getDuration() : "0") ,
                                     i, 0 );

               rs_model.setValueAt(temp_alu[i].getName(), i, 1);
               rs_model.setValueAt(busy_desc, i, 2);
               rs_model.setValueAt((temp_alu[i].isBusy() ? temp_alu[i].getOperation() : " "), i, 3);
               rs_model.setValueAt(temp_alu[i].getVj(), i, 4);
               rs_model.setValueAt(temp_alu[i].getVk(), i, 5);
               rs_model.setValueAt(temp_alu[i].getQj(), i, 6);
               rs_model.setValueAt(temp_alu[i].getQk(), i, 7);
          }
     }

     ///
     /// The main function
     ///
     public static void main(String[] args)
     {
          new TomasuloGUI().setVisible(true);
     }
}
