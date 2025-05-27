import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {

    @Test
    void testAdd() {
        assertEquals(5.0, Calculator.add(2.0, 3.0), "Test addition of two positive numbers");
        assertEquals(-1.0, Calculator.add(-2.0, 1.0), "Test addition of a negative and a positive number");
        assertEquals(-5.0, Calculator.add(-2.0, -3.0), "Test addition of two negative numbers");
        assertEquals(2.0, Calculator.add(2.0, 0.0), "Test addition with zero");
        assertEquals(0.0, Calculator.add(0.0, 0.0), "Test addition of two zeros");
        assertEquals(3.14 + 2.71, Calculator.add(3.14, 2.71), "Test addition of two floating point numbers");
    }

    @Test
    void testSubtract() {
        assertEquals(-1.0, Calculator.subtract(2.0, 3.0), "Test subtraction of two positive numbers (a < b)");
        assertEquals(1.0, Calculator.subtract(3.0, 2.0), "Test subtraction of two positive numbers (a > b)");
        assertEquals(-3.0, Calculator.subtract(-2.0, 1.0), "Test subtraction with a negative minuend");
        assertEquals(-1.0, Calculator.subtract(1.0, 2.0), "Test subtraction resulting in negative");
        assertEquals(5.0, Calculator.subtract(2.0, -3.0), "Test subtraction with a negative subtrahend");
        assertEquals(2.0, Calculator.subtract(2.0, 0.0), "Test subtraction with zero");
        assertEquals(0.0, Calculator.subtract(0.0, 0.0), "Test subtraction of two zeros");
        assertEquals(3.14 - 2.71, Calculator.subtract(3.14, 2.71), "Test subtraction of two floating point numbers");
    }

    @Test
    void testMultiply() {
        assertEquals(6.0, Calculator.multiply(2.0, 3.0), "Test multiplication of two positive numbers");
        assertEquals(-6.0, Calculator.multiply(-2.0, 3.0), "Test multiplication with one negative number (first)");
        assertEquals(-6.0, Calculator.multiply(2.0, -3.0), "Test multiplication with one negative number (second)");
        assertEquals(6.0, Calculator.multiply(-2.0, -3.0), "Test multiplication of two negative numbers");
        assertEquals(0.0, Calculator.multiply(2.0, 0.0), "Test multiplication by zero (first)");
        assertEquals(0.0, Calculator.multiply(0.0, 2.0), "Test multiplication by zero (second)");
        assertEquals(0.0, Calculator.multiply(0.0, 0.0), "Test multiplication of two zeros");
        assertEquals(3.14 * 2.71, Calculator.multiply(3.14, 2.71), "Test multiplication of two floating point numbers");
    }

    @Test
    void testDivide() {
        assertEquals(2.0, Calculator.divide(6.0, 3.0), "Test division of two positive numbers");
        assertEquals(-2.0, Calculator.divide(-6.0, 3.0), "Test division with negative dividend");
        assertEquals(-2.0, Calculator.divide(6.0, -3.0), "Test division with negative divisor");
        assertEquals(2.0, Calculator.divide(-6.0, -3.0), "Test division of two negative numbers");
        assertEquals(0.0, Calculator.divide(0.0, 3.0), "Test division of zero by a number");
        assertEquals(2.5, Calculator.divide(5.0, 2.0), "Test division resulting in a float");
        assertEquals(0.5, Calculator.divide(1.0, 2.0), "Test division of smaller by larger number");
    }

    @Test
    void testDivideByZero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Calculator.divide(1.0, 0.0);
        });
        assertEquals("Cannot divide by zero", exception.getMessage(), "Test division by zero throws IllegalArgumentException with correct message");

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> {
            Calculator.divide(0.0, 0.0);
        });
        assertEquals("Cannot divide by zero", exception2.getMessage(), "Test division of zero by zero throws IllegalArgumentException with correct message");
        
        IllegalArgumentException exception3 = assertThrows(IllegalArgumentException.class, () -> {
            Calculator.divide(-1.0, 0.0);
        });
        assertEquals("Cannot divide by zero", exception3.getMessage(), "Test division of negative number by zero throws IllegalArgumentException with correct message");
    }
}
