package ute.buiquanghuy.maytinh;

import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.*;

public class CalculatorLogicTest {

    @Test
    public void testBasicEval() {
        assertEquals(new BigDecimal("5"), MainActivity.eval("2+3"));
        assertEquals(new BigDecimal("10"), MainActivity.eval("2*5"));
        assertEquals(new BigDecimal("2"), MainActivity.eval("10/5"));
        assertEquals(new BigDecimal("-1"), MainActivity.eval("2-3"));
    }

    @Test
    public void testParentheses() {
        assertEquals(new BigDecimal("10"), MainActivity.eval("2*(3+2)"));
    }

    @Test
    public void testImplicitMultiplication() {
        // This is expected to fail with the current implementation if we check for full consumption
        // or it might just return the first number if we don't.
        // Currently it returns 2 for "2(3+2)"
        try {
            BigDecimal result = MainActivity.eval("2(3+2)");
            System.out.println("Result for 2(3+2): " + result);
            // If it returns 2, it's not what we want. We want 10.
        } catch (Exception e) {
            System.out.println("Error for 2(3+2): " + e.getMessage());
        }
    }

    @Test
    public void testUnconsumedInput() {
        // "2+3*4 5" should probably fail
        try {
            BigDecimal result = MainActivity.eval("2+3*4 5");
            System.out.println("Result for 2+3*4 5: " + result);
        } catch (Exception e) {
            System.out.println("Error for 2+3*4 5: " + e.getMessage());
        }
    }
}
