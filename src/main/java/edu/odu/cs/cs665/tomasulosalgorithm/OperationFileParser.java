package edu.odu.cs.cs665.tomasulosalgorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.File;

//import java.io.*;

/**
 * This class provides all file parsing funtionality. Given an input file, one OperationList and one RegisterFiles
 * will be generated.
 */
public class OperationFileParser {
    private File data_file; ///< The input file

    /**
     * Set the input file to the default value of "a.in".
     */
    public OperationFileParser()
    {
        this.data_file = new File("a.in");
    }

    /**
     * Set the input file to the specified filename.
     */
    public OperationFileParser(File file_in)
    {
        this.data_file = file_in;
    }

    /**
     *  Parse the input file; generate one OperationList and one RegisterFiles.
     */
    public void parseFile(OperationList oplist)
         throws Exception
    {
        FileReader in_file = new FileReader(data_file);
        BufferedReader file_buff = new BufferedReader(in_file);

        String[] split_1, split_2, operands;
        String op_portion, comment_portion = "";
        String line, operation;
        int issue = 0;

        boolean comment_exists;

        while ((line = file_buff.readLine()) != null) {
            //trim line
            line = line.trim();

            //only process non-empty lines
            if (line != null && !line.equals("")) {
                //increment the issue_number
                issue++;

                //Check for comment
                if (line.indexOf(";") >= 0) {
                    split_1         = line.split(";");
                    comment_portion = split_1[1].trim();
                    comment_exists = true;
                }
                else {
                    split_1 = new String[1];
                    split_1[0] = line;
                    comment_exists = false;
                    op_portion = line;
                }

                //Split/Prse the operation & operands
                split_2 = split_1[0].trim().split("\\s+");
                operation = split_2[0].trim();

                operands = new String[split_2.length - 1];

                for (int i = 1; i < split_2.length; i++) {
                    //temporary index
                    int temp_index = split_2[i].indexOf(',');
                    //check for a comma
                    temp_index = (temp_index == -1 ? split_2[i].length() : temp_index);

                    operands[i - 1] = split_2[i].substring(0, temp_index);
                }

                if (operands.length == 3) {
                    oplist.addOperation(new Operation(operation,
                                                      operands[0],
                                                      operands[1],
                                                      operands[2],
                                                      comment_exists));
                }
                else if (operands.length == 2) {
                        oplist.addOperation(new Operation(operation,
                                                          operands[0],
                                                          operands[1],
                                                          comment_exists));
                }
                else {
                    throw new Exception() {
                        public String toString() {
                             return "File Parse Error: Malformed Operation";
                        }
                    };
                }

                if (comment_exists) {
                    oplist.getLastOperation().setComment(comment_portion);
                }

                oplist.getLastOperation().setIssueNum(issue);
            }
        }
    }

    /**
     * Generates the string representation of the OperationFileParser Object.
     */
    public String toString()
    {
        return "Operation File Parser";
    }
}
