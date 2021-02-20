package com.example.vacomputingtask;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CalculatorTest {

    @Test
    public void add() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void sub() {
        assertEquals(4, 6 - 2);
    }


    @Test
    public void mul() {
        assertEquals(4, 2 * 2);
    }

    @Test
    public void div() {
        assertEquals(4, 8 / 2);
    }

}