package edu.odu.cs.cs665.tomasulosalgorithm;

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.StringReader;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;
import org.hamcrest.core.IsNull;


/**
 * This class contains all module testing.
 *
 * @todo Refactor this into proper tests.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestOperationFileParser {

    final String inputStr = "L.D        F0,    100(R8)      ; R6=998, R8=1040\n"
                          + "DADDI R6, R6, #2\n"
                          + "\n"
                          + "\n"
                          + "L.D     F2     60(R6)\n"
                          + "DIV.D F4, F2, F0\n"
                          + " \n"
                          + "  \n"
                          + "S.D F6, 100(R6) \n"
                          + "S.D    F8, 60(R8) \n"
                          + "L.D                 F4, 24(R4)      ; R4 = 1200\n";

    @Before
    public void setUp()
    {

    }

    @Test
    public void testOperationFileParser()
    {
        OperationList testList = new OperationList();
        OperationFileParser testParser = new OperationFileParser(new StringReader(inputStr));
        RegisterFiles testRegs = new RegisterFiles();

        try {
            testParser.parseFile(testList);
        }
        catch (Exception e) {
            fail(e.toString());
        }

        String expectedStr = "# Operations: 7\n"
                           + "L.D F0 100 R8 ; R6=998, R8=1040\n"
                           + "DADDI R6 R6 #2\n"
                           + "L.D F2 60 R6\n"
                           + "DIV.D F4 F2 F0\n"
                           + "S.D F6 100 R6\n"
                           + "S.D F8 60 R8\n"
                           + "L.D F4 24 R4 ; R4 = 1200";

        assertThat(testList.toString(), equalTo(expectedStr));
    }
}
