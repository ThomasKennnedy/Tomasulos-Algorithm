package edu.odu.cs.cs665.tomasulosalgorithm;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Container;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.TitledBorder;
import java.util.Map;

import java.io.*;

/**
 * This class provides all GUI functionality.
 */
public class TomasuloGUI extends JFrame {
     JPanel controlPanel; ///< Container panel for controls
     JPanel statusPanel;  ///< Container panel for status

     JLabel fileLabel; ///< Filename label

     //Tables
     JTable opTable;  ///< Instructions Table
     JTable rsTable;  ///< ALU Reservation Station Table
     JTable mrsTable; ///< Memory Reservations Station Table
     JTable irsTable; ///< Integer Reservation Station Table
     JTable regTable; ///< Register File Table

     //Table Models
     DefaultTableModel opModel;  ///< Instructions Table Model
     DefaultTableModel rsModel;  ///< ALU Reservation Station Table Model
     DefaultTableModel mrsModel; ///< Memory Reservations Station Table Mdoel
     DefaultTableModel irsModel; ///< Integer Reservation Station Table Model
     DefaultTableModel regModel; ///< Register File Table Model

     JButton loadButton; ///< Load button
     JButton stepButton; ///< Step button

     JTextField fileField;
     JTextField clocksField; ///< Clock display field

     //Simulation Object
     Simulation simInstance; ///< Simulation Wrapper Object

     //File Objects
     final JFileChooser fileDialog = new JFileChooser(); ///< File selection dialog
     File instructionsFile; ///< Instructions File object

     /**
      * The GUI Constructor.
      */
     public TomasuloGUI()
     {
          super("Tomsulo's Simulation");
          setLocation(50, 75);
          setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          setMinimumSize(new Dimension(700, 500));

          //setPreferredSize(new Dimension(700, 600));
          Container cp = getContentPane();

          //create the container panels
          controlPanel = new JPanel();
          statusPanel = new JPanel();

          //Create the control elements
          fileLabel       = new JLabel("Input File:");
          fileField       = new JTextField("a.in", 16);
          loadButton      = new JButton("Load");
          stepButton      = new JButton("Step");

          //Add the control elements
          controlPanel.setLayout(new FlowLayout());

          controlPanel.add(fileLabel);
          controlPanel.add(fileField);
          controlPanel.add(loadButton);
          controlPanel.add(stepButton);

          //Disable Selected Control Fields
          fileField.setEnabled(false);
          stepButton.setEnabled(false);

          //Create the status elements
          statusPanel.setLayout(new FlowLayout());
          clocksField =  new JTextField("0", 8);
          clocksField.setEnabled(false);
          //instrLabel =  new JLabel("Instructions: ");

          //Add the status elements
          statusPanel.add(clocksField);
          //statusPanel.add(instrLabel);

          //Create the Instructions Table Model
          opModel = new DefaultTableModel();
          opModel.setColumnIdentifiers(new Object[]{"Instruction", " ", "j", "k", "Issue", "Complete", "Write"});
          //add instruction rows
          for (int i = 1; i <= 7; i++) {
               opModel.addRow(new Object[]{" ", " ", " ", " ", " ", " ", " "});
          }

          //Create The Reservation Station Table Model
          rsModel = new DefaultTableModel();
          rsModel.setColumnIdentifiers(new Object[]{"Time", "Name", "Busy", "Op", "Vj", "Vk", "Qj", "Qk"});
          //Add Reservation Station Table Rows
          rsModel.addRow(new Object[]{"" + 0, "Int1", " ", " ", " ", " ", " ", " "});
          rsModel.addRow(new Object[]{"" + 0, "Add1", " ", " ", " ", " ", " ", " "});
          rsModel.addRow(new Object[]{"" + 0, "Add2", " ", " ", " ", " ", " ", " "});
          rsModel.addRow(new Object[]{"" + 0, "Mult1", " ", " ", " ", " ", " ", " "});
          rsModel.addRow(new Object[]{"" + 0, "Mult2", " ", " ", " ", " ", " ", " "});
          rsModel.addRow(new Object[]{"" + 0, "Div1", " ", " ", " ", " ", " ", " "});
          rsModel.addRow(new Object[]{"" + 0, "Div2", " ", " ", " ", " ", " ", " "});

          //Create The Reservation Station Table Model
          mrsModel = new DefaultTableModel();
          mrsModel.setColumnIdentifiers(new Object[]{"Name", "Busy", "Address" });
          //Add Reservation Station Table Rows
          mrsModel.addRow(new Object[]{"Load1",  " ", " "});
          mrsModel.addRow(new Object[]{"Load2",  " ", " "});
          mrsModel.addRow(new Object[]{"Store1", " ", " "});
          mrsModel.addRow(new Object[]{"Store2", " ", " "});

          //Create the Register Table Model
          regModel =  new DefaultTableModel();
          regModel.addColumn("FP");
          regModel.addColumn("Value");
          regModel.addColumn("Int");
          regModel.addColumn("Value");

          //Ad Initial Register Table Rows
          for (int i = 0; i < 14; i++) {
               regModel.addRow(new Object[]{" ", " " , " ", " "});
          }

          //Create the tables
          opTable  = new JTable(opModel);
          rsTable  = new JTable(rsModel);
          mrsTable = new JTable(mrsModel);
          regTable = new JTable(regModel);

          //Disallow editing of table fields
          opTable.setEnabled(false);
          rsTable.setEnabled(false);
          mrsTable.setEnabled(false);
          regTable.setEnabled(false);

          //Disable the reorganization of columns
          opTable.getTableHeader().setReorderingAllowed(false);
          rsTable.getTableHeader().setReorderingAllowed(false);
          mrsTable.getTableHeader().setReorderingAllowed(false);
          regTable.getTableHeader().setReorderingAllowed(false);

          //Set Instruction Table Column Widths
          //Column 0 -(Operation
          opTable.getColumnModel().getColumn(0).setMinWidth(100);
          opTable.getColumnModel().getColumn(0).setMaxWidth(100);
          opTable.getColumnModel().getColumn(0).setPreferredWidth(100);
          //Columns [1,3]
          for (int i = 1; i <= 3; i++) {
               opTable.getColumnModel().getColumn(i).setMinWidth(60);
               opTable.getColumnModel().getColumn(i).setMaxWidth(60);
               opTable.getColumnModel().getColumn(i).setPreferredWidth(60);
          }
          //Columns [4,6]
          for (int i = 4; i <= 6; i++) {
               opTable.getColumnModel().getColumn(i).setMinWidth(80);
               opTable.getColumnModel().getColumn(i).setMaxWidth(80);
               opTable.getColumnModel().getColumn(i).setPreferredWidth(80);
          }

          //Set Reservation Station Table Column Widths
          //Set the first column -- leave default for the remaining columns
          rsTable.getColumnModel().getColumn(0).setMinWidth(80);
          rsTable.getColumnModel().getColumn(0).setMaxWidth(80);
          rsTable.getColumnModel().getColumn(0).setPreferredWidth(80);

          //Set Register Table Column widths
          //FP Name Column
          regTable.getColumnModel().getColumn(0).setMinWidth(60);
          regTable.getColumnModel().getColumn(0).setMaxWidth(60);
          regTable.getColumnModel().getColumn(0).setPreferredWidth(60);
          //FP Value Column
          regTable.getColumnModel().getColumn(1).setMinWidth(100);
          regTable.getColumnModel().getColumn(1).setMaxWidth(100);
          regTable.getColumnModel().getColumn(1).setPreferredWidth(100);
          //Int Name Column
          regTable.getColumnModel().getColumn(2).setMinWidth(60);
          regTable.getColumnModel().getColumn(2).setMaxWidth(60);
          regTable.getColumnModel().getColumn(2).setPreferredWidth(60);
          //Int Value Column
          regTable.getColumnModel().getColumn(3).setMinWidth(100);
          regTable.getColumnModel().getColumn(3).setMaxWidth(100);
          regTable.getColumnModel().getColumn(3).setPreferredWidth(100);


          //Set Table Viewport size for Table JScrollPanes
          opTable.setPreferredScrollableViewportSize(opTable.getPreferredSize());
          rsTable.setPreferredScrollableViewportSize(rsTable.getPreferredSize());
          mrsTable.setPreferredScrollableViewportSize(mrsTable.getPreferredSize());
          regTable.setPreferredScrollableViewportSize(regTable.getPreferredSize());

          //Create Table JScrollPanes
          JScrollPane opPanel = new JScrollPane(opTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

          JScrollPane rsPanel = new JScrollPane(rsTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

          JScrollPane mrsPanel = new JScrollPane(mrsTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

          JScrollPane regPanel = new JScrollPane(regTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

          //Create Panel Title Borders
          TitledBorder controlBorder = BorderFactory.createTitledBorder("Controls");
          controlPanel.setBorder(controlBorder);

          TitledBorder statusBorder = BorderFactory.createTitledBorder("Clock");
          statusPanel.setBorder(statusBorder);

          TitledBorder opBorder = BorderFactory.createTitledBorder("Instructions");
          opPanel.setBorder(opBorder);

          TitledBorder rsBorder = BorderFactory.createTitledBorder("ALU Stations");
          rsPanel.setBorder(rsBorder);

          TitledBorder mrsBorder = BorderFactory.createTitledBorder("Memory Stations");
          mrsPanel.setBorder(mrsBorder);

          TitledBorder regBorder = BorderFactory.createTitledBorder("Registers");
          regPanel.setBorder(regBorder);

          //set the layout manager and create the constraints helper
          cp.setLayout(new GridBagLayout());
          GridBagConstraints c = new GridBagConstraints();

          //add the controls panel
          c.fill = GridBagConstraints.HORIZONTAL;
          c.ipady     = 0;
          c.gridx     = 0;
          c.gridy     = 0;
          c.gridwidth = 2;
          c.gridheight = 1;
          cp.add(controlPanel, c);

          //add the status panel
          c.fill = GridBagConstraints.HORIZONTAL;
          c.ipady     = 0;
          c.gridx     = 2;
          c.gridy     = 0;
          c.gridwidth = 1;
          c.gridheight = 1;
          cp.add(statusPanel, c);

          //add the operations scroll panel
          c.fill = GridBagConstraints.HORIZONTAL;
          c.gridx     = 0;
          c.gridy     = 1;
          c.gridwidth = 2;
          c.gridheight = 1;
          cp.add(opPanel, c);

          //add the Reservation Station panel
          c.fill = GridBagConstraints.HORIZONTAL;
          c.gridx     = 0;
          c.gridy     = 3;
          c.gridwidth = 3;
          c.gridheight = 1;
          cp.add(rsPanel, c);

          //add the Memory Reservation Station panel
          c.fill = GridBagConstraints.HORIZONTAL;
          c.gridx     = 0;
          c.gridy     = 2;
          c.gridwidth = 2;
          c.gridheight = 1;
          cp.add(mrsPanel, c);

          //add the Registers panel
          c.fill = GridBagConstraints.HORIZONTAL;
          c.gridx     = 2;
          c.gridy     = 1;
          c.gridwidth = 1;
          c.gridheight = 2;
          cp.add(regPanel, c);

          //Set Action Listeners(Event Handlers)
          //File Load Listener
          loadButton.addActionListener(
               new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                         //Open the file selection Dialog
                         int fileResult = fileDialog.showOpenDialog(TomasuloGUI.this);

                         if (fileResult == JFileChooser.APPROVE_OPTION) {
                              instructionsFile = fileDialog.getSelectedFile();
                              fileField.setText(instructionsFile.getName());

                              //construct the simulation
                              simInstance = new Simulation();
                              try {
                                   simInstance.initialize(instructionsFile);
                                   loadButton.setEnabled(false);
                                   stepButton.setEnabled(true);
                              }
                              catch (Exception ex) {
                                   javax.swing.JOptionPane.showMessageDialog(new JFrame(), ex.toString());
                              }
                         }

                         //Add additional instruction rows if needed
                         //this only needs to run once as the number instructions
                         //remains constant
                         OperationList updateList = simInstance.getOperationList();

                         while (opModel.getRowCount() < updateList.getNumberOfOperations()) {
                              opModel.addRow(new Object[]{" ", " ", " ", " ", " ", " ", " "});
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
          stepButton.addActionListener(
               new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                         simInstance.performStep();

                         //update UI
                         //update clock
                         clocksField.setText("" + simInstance.getCurrentCycle());
                         //update tables
                         updateInstructionTable();
                         updateRegisterTable();
                         updateMemTable();
                         updateALUTable();

                         //Check if the simulation has finished
                         if (simInstance.isComplete()) {
                              javax.swing.JOptionPane.showMessageDialog(new JFrame(), "Simulation Complete");
                              stepButton.setEnabled(false);
                         }
                    }
               }
         );

         pack();
     }

     /**
      * Instruction Table Update Helper.
      */
     private void updateInstructionTable()
     {
          OperationList updateList = simInstance.getOperationList();

          //Populate Operations table with the instructions
          for (int i = 0; i < updateList.getNumberOfOperations(); i++) {
               Operation itOp = updateList.getOperation(i + 1);
               //obtain the write time
               int writeTime = itOp.getWriteTime();

               //Opcode
               opModel.setValueAt(itOp.getOpcode(), i, 0);
               //Operands
               opModel.setValueAt(itOp.getOperand(1), i, 1);
               opModel.setValueAt(itOp.getOperand(2), i, 2);

               if (itOp.getNumberOfOperands() > 2) {
                    opModel.setValueAt(itOp.getOperand(3), i, 3);
               }

               opModel.setValueAt(itOp.getExecution(), i, 5);
               opModel.setValueAt((writeTime == -1 ? " " : writeTime), i, 6);

               //Issue Number - display once the instruction has been scheduled
               opModel.setValueAt((itOp.isScheduled() ? itOp.getIssueNum() : " "), i, 4);
          }
     }

     /**
      * Register Table Update Helper.
      */
     private void updateRegisterTable()
     {
          int tempItIndex = 0; // tempory index tracker

          //Get the Integer Registers
          Map<String, String> tempIntReg = simInstance.getRegisterFiles().getIntegerRegisters();
          //Get the FP Registers
          Map<String, String> tempFpReg = simInstance.getRegisterFiles().getFPRegisters();

          //add rows if needed
          while (tempIntReg.size() > regModel.getRowCount()
              || tempFpReg.size() > regModel.getRowCount()) {
               regModel.addRow(new Object[]{" ", " ", " ", " "});
          }

          //Update Integer Table Values
          for (Map.Entry<String, String> intEntry : tempIntReg.entrySet()) {
               regModel.setValueAt(intEntry.getKey(), tempItIndex, 2);
               regModel.setValueAt(intEntry.getValue(), tempItIndex, 3);

               tempItIndex++;
          }

          tempItIndex = 0;
          //Update Integer Table Values
          for (Map.Entry<String, String> fpEntry : tempFpReg.entrySet()) {
               regModel.setValueAt(fpEntry.getKey(), tempItIndex, 0);
               regModel.setValueAt(fpEntry.getValue(), tempItIndex, 1);

               tempItIndex++;
          }
     }

     /**
      * MemStations Table Update Helper
      */
     private void updateMemTable()
     {
          //Get a copy of the memory stations
          MemStation[] tempMs = simInstance.getMemStations();

          //Update the table with current values for the stations
          for (int i = 0; i < tempMs.length; i++) {
               //generate a meaningfull representation of busy
               String busyDesc = (tempMs[i].isBusy() ? "Yes" : "No");

               mrsModel.setValueAt(tempMs[i].getName(), i, 0);
               mrsModel.setValueAt(busyDesc, i, 1);
               mrsModel.setValueAt(tempMs[i].getAddress(), i, 2);
          }
     }

     /**
      * ALUStations Table Update Helper
      */
     private void updateALUTable()
     {
          //Get a copy of the memory stations
          ALUStation[] tempAlu = simInstance.getALUStations();

          //Update the table with current values for the stations
          for (int i = 0; i < tempAlu.length; i++) {
               //generate a meaningfull representation of busy
               String busyDesc = (tempAlu[i].isBusy() ? "Yes" : "No");

               rsModel.setValueAt(((tempAlu[i].isReady() && tempAlu[i].isBusy()) ? tempAlu[i].getDuration() : "0"),
                                     i, 0);

               rsModel.setValueAt(tempAlu[i].getName(), i, 1);
               rsModel.setValueAt(busyDesc, i, 2);
               rsModel.setValueAt((tempAlu[i].isBusy() ? tempAlu[i].getOperation() : " "), i, 3);
               rsModel.setValueAt(tempAlu[i].getVj(), i, 4);
               rsModel.setValueAt(tempAlu[i].getVk(), i, 5);
               rsModel.setValueAt(tempAlu[i].getQj(), i, 6);
               rsModel.setValueAt(tempAlu[i].getQk(), i, 7);
          }
     }

     /**
      * The main function. Construct a new GUI and make it visible
      *
      * @param args no command line arguments or accepted or used
      */
     public static void main(String[] args)
     {
          new TomasuloGUI().setVisible(true);
     }
}
