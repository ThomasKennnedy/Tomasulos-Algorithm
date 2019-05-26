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
 * @todo write documentation.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestOperation {
    Operation loadNoComment;
    Operation loadComment;

    Operation store;
    Operation add;

    /*
    L.D        F0,    100(R8)      ; R6=998, R8=1040
    DADDI R6, R6, #2


    L.D     F2     60(R6)
    DIV.D F4, F2, F0


    S.D F6, 100(R6)
    S.D    F8, 60(R8)
    L.D                 F4, 24(R4)      ; R4 = 1200
    */

    @Before
    public void setUp()
    {
        loadNoComment = new Operation("L.D", "F2", "60(R6)", false);

        loadComment = new Operation("L.D", "F0", "100(R8)", true);
        loadComment.setComment("R6=998, R8=1040");
    }

    @Test
    public void testConstructor2Operands()
    {
        assertThat(loadNoComment.hasComment(), is(false));
        assertThat(loadNoComment.getOpcode(), equalTo("L.D"));
        assertThat(loadNoComment.getComment(), equalTo(""));
        assertThat(loadNoComment.getOperand(1), equalTo("F2"));
        assertThat(loadNoComment.getOperand(2), equalTo("60"));
        assertThat(loadNoComment.getOperand(3), equalTo("R6"));
        assertThat(loadNoComment.getNumberOfOperands(), is(3));
        assertThat(loadNoComment.getOperands(), equalTo("F2 60 R6"));
        assertThat(loadNoComment.getExecStart(), is(-1));
        assertThat(loadNoComment.getExecEnd(), is(-1));
        assertThat(loadNoComment.getIssueNum(), is(0));
        assertThat(loadNoComment.getExecution(), equalTo(""));
        assertThat(loadNoComment.getWriteTime(), is(-1));
        assertThat(loadNoComment.isScheduled(), is(false));
        assertThat(loadNoComment.toString(), equalTo("L.D F2 60 R6"));
    }

}
