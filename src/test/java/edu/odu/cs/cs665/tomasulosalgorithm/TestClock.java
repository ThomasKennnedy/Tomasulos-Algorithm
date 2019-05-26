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
public class TestClock {

    @Test
    public void testGetInstance()
    {
        assertThat(Clock.getInstance(), not(nullValue()));
    }

    @Test
    public void testGet()
    {
        Clock clock= Clock.getInstance();
        assertThat(clock.get(), is(0));
    }

    @Test
    public void testIncrement()
    {
        Clock clock = Clock.getInstance();

        assertThat(clock.get(), is(0));
        clock.increment();
        assertThat(clock.get(), is(1));
        clock.increment();
        assertThat(clock.get(), is(2));
    }
}
