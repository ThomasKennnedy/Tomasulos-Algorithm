package edu.odu.cs.cs665.tomasulosalgorithm;

import java.io.Reader;
import java.io.BufferedReader;


/**
 * This class provides all file parsing funtionality. Given an input file, one
 * OperationList and one RegisterFiles will be generated.
 */
public class OperationFileParser {

    /**
     * Input file containg operations.
     */
    private BufferedReader dataFile;

    /**
     * Set the input file to the default value of "a.in".
     */
    private OperationFileParser()
    {
        this.dataFile = null; //new File("a.in");
    }

    /**
     * Set the input file to the specified filename.
     *
     * @param fileIn reader referencing the input source.
     */
    public OperationFileParser(Reader fileIn)
    {
        this.dataFile = new BufferedReader(fileIn);
    }

    /**
     * Parse the input file; generate one OperationList and one RegisterFiles.
     *
     * @throws Exception if the an operation is malformed (e.g., contains an
     *  invalid number of operands).
     */
    public void parseFile(OperationList oplist)
         throws Exception
    {
        //FileReader inFile = new FileReader(dataFile);
        BufferedReader fileBuff = new BufferedReader(dataFile);

        String[] split1, split2, operands;
        String opPortion, commentPortion = "";
        String line, operation;
        int issue = 0;

        boolean commentExists;

        while ((line = fileBuff.readLine()) != null) {
            //trim line
            line = line.trim();

            //only process non-empty lines
            if (line != null && !line.equals("")) {
                //increment the issueNumber
                issue++;

                //Check for comment
                if (line.indexOf(";") >= 0) {
                    split1         = line.split(";");
                    commentPortion = split1[1].trim();
                    commentExists = true;
                }
                else {
                    split1 = new String[1];
                    split1[0] = line;
                    commentExists = false;
                    opPortion = line;
                }

                //Split/Prse the operation & operands
                split2 = split1[0].trim().split("\\s+");
                operation = split2[0].trim();

                operands = new String[split2.length - 1];

                for (int i = 1; i < split2.length; i++) {
                    // Locate the comma index or end of string (if no comma
                    // exists)
                    int commaIndex = split2[i].indexOf(',');

                    if (commaIndex == -1) {
                        commaIndex = split2[i].length();
                    }

                    operands[i - 1] = split2[i].substring(0, commaIndex);
                }

                if (operands.length == 3) {
                    oplist.addOperation(new Operation(operation,
                                                      operands[0],
                                                      operands[1],
                                                      operands[2],
                                                      commentExists));
                }
                else if (operands.length == 2) {
                        oplist.addOperation(new Operation(operation,
                                                          operands[0],
                                                          operands[1],
                                                          commentExists));
                }
                else {
                    throw new Exception() {
                        public String toString() {
                             return "File Parse Error: Malformed Operation";
                        }
                    };
                }

                if (commentExists) {
                    oplist.getLastOperation().setComment(commentPortion);
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
