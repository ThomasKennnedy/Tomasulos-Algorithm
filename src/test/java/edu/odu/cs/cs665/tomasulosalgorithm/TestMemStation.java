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
public class TestMemStation {

    @Before
    public void setUp()
    {

    }

    @Test
    public void testStations()
    {
        // Reservation Stations
        MemStation mStation =  new MemStation("Load1");

        assertThat(mStation.getName(), equalTo("Load1"));
        assertTrue(mStation.isReady());
        assertThat(mStation.getAddress(), is(nullValue()));
        assertThat(mStation.isStore(), is(false));
    }

    @Test
    public void testHasPriority()
    {
        fail("TBW");
    }
}
