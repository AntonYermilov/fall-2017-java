package me.eranik.math;

import me.eranik.util.Stack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CalculatorTest {
    @Mock
    private Stack<Character> operationsMock;
    @Mock
    private Stack<Double> operandsMock;

    private Calculator calculator;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        calculator = new Calculator(operationsMock, operandsMock);
    }

    @Test
    void testGetReversedPolishNotationIncorrectExpression() {
        assertThrows(Calculator.ParseException.class, () -> calculator.getReversedPolishNotation("12+3"));
        assertThrows(Calculator.ParseException.class, () -> calculator.getReversedPolishNotation("2(3+4)"));
        assertThrows(Calculator.ParseException.class, () -> calculator.getReversedPolishNotation("(3+4)2"));
        assertThrows(Calculator.ParseException.class, () -> calculator.getReversedPolishNotation("2(-3+4)"));
        assertThrows(Calculator.ParseException.class, () -> calculator.getReversedPolishNotation("2(-3+4)"));
        assertThrows(Calculator.ParseException.class, () -> calculator.getReversedPolishNotation("(1+(2*3)"));
        assertThrows(Calculator.ParseException.class, () -> calculator.getReversedPolishNotation("1+(2/3))"));
        assertThrows(Calculator.ParseException.class, () -> calculator.getReversedPolishNotation("1++2"));
    }

    @Test
    void testGetReversedPolishNotationSumWithParentheses() throws Calculator.ParseException {
        String expression = "1+(2+3)+4";

        doNothing().when(operationsMock).push(anyChar());
        when(operationsMock.pop()).thenReturn('+', '(', '+', '+');
        when(operationsMock.empty()).thenReturn(true, false, false, false, false, true, false, true);
        when(operationsMock.top()).thenReturn('(', '+', '(', '+');

        assertEquals("123++4+", calculator.getReversedPolishNotation(expression));

        InOrder inOrder = inOrder(operationsMock);

        inOrder.verify(operationsMock).push('+');
        inOrder.verify(operationsMock).push('(');
        inOrder.verify(operationsMock).push('+');
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).push('+');
        inOrder.verify(operationsMock).pop();
    }

    @Test
    void testGetReversedPolishNotationSubtractionWithParentheses() throws Calculator.ParseException {
        String expression = "1-(2-3)-4";

        doNothing().when(operationsMock).push(anyChar());
        when(operationsMock.pop()).thenReturn('-', '(', '-', '-');
        when(operationsMock.empty()).thenReturn(true, false, false, false, false, true, false, true);
        when(operationsMock.top()).thenReturn('(', '-', '(', '-');

        assertEquals("123--4-", calculator.getReversedPolishNotation(expression));

        InOrder inOrder = inOrder(operationsMock);

        inOrder.verify(operationsMock).push('-');
        inOrder.verify(operationsMock).push('(');
        inOrder.verify(operationsMock).push('-');
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).push('-');
        inOrder.verify(operationsMock).pop();
    }

    @Test
    void testGetReversedPolishNotationMultiplicationWithParentheses() throws Calculator.ParseException {
        String expression = "1*(2*3)*4";

        doNothing().when(operationsMock).push(anyChar());
        when(operationsMock.pop()).thenReturn('*', '(', '*', '*');
        when(operationsMock.empty()).thenReturn(true, false, false, false, false, true, false, true);
        when(operationsMock.top()).thenReturn('(', '*', '(', '*');

        assertEquals("123**4*", calculator.getReversedPolishNotation(expression));

        InOrder inOrder = inOrder(operationsMock);

        inOrder.verify(operationsMock).push('*');
        inOrder.verify(operationsMock).push('(');
        inOrder.verify(operationsMock).push('*');
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).push('*');
        inOrder.verify(operationsMock).pop();
    }

    @Test
    void testGetReversedPolishNotationDivisionWithParentheses() throws Calculator.ParseException {
        String expression = "1/(2/3)/4";

        doNothing().when(operationsMock).push(anyChar());
        when(operationsMock.pop()).thenReturn('/', '(', '/', '/');
        when(operationsMock.empty()).thenReturn(true, false, false, false, false, true, false, true);
        when(operationsMock.top()).thenReturn('(', '/', '(', '/');

        assertEquals("123//4/", calculator.getReversedPolishNotation(expression));

        InOrder inOrder = inOrder(operationsMock);

        inOrder.verify(operationsMock).push('/');
        inOrder.verify(operationsMock).push('(');
        inOrder.verify(operationsMock).push('/');
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).push('/');
        inOrder.verify(operationsMock).pop();
    }

    @Test
    void testGetReversedPolishNotationSumAndSubtraction() throws Calculator.ParseException {
        String expression = "1+2-3+4";

        doNothing().when(operationsMock).push(anyChar());
        when(operationsMock.pop()).thenReturn('+', '-', '+');
        when(operationsMock.empty()).thenReturn(true, false, true, false, true, false, true);
        when(operationsMock.top()).thenReturn('+', '-');

        assertEquals("12+3-4+", calculator.getReversedPolishNotation(expression));

        InOrder inOrder = inOrder(operationsMock);

        inOrder.verify(operationsMock).push('+');
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).push('-');
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).push('+');
        inOrder.verify(operationsMock).pop();
    }

    @Test
    void testGetReversedPolishNotationMultiplicationAndDivision() throws Calculator.ParseException {
        String expression = "1*2/3*4";

        doNothing().when(operationsMock).push(anyChar());
        when(operationsMock.pop()).thenReturn('*', '/', '*');
        when(operationsMock.empty()).thenReturn(true, false, true, false, true, false, true);
        when(operationsMock.top()).thenReturn('*', '/');

        assertEquals("12*3/4*", calculator.getReversedPolishNotation(expression));

        InOrder inOrder = inOrder(operationsMock);

        inOrder.verify(operationsMock).push('*');
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).push('/');
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).push('*');
        inOrder.verify(operationsMock).pop();
    }

    @Test
    void testGetReversedPolishNotationComplex() throws Calculator.ParseException {
        String expression = "1+(2-3*4)/5-6";

        doNothing().when(operationsMock).push(anyChar());
        when(operationsMock.pop()).thenReturn('*', '-', '(', '/', '+', '-');
        when(operationsMock.empty()).thenReturn(true, false,
                false, false, false, false, false, false, false,  true, false, true);
        when(operationsMock.top()).thenReturn('(', '-', '*', '-', '(', '+', '/', '+', '-');

        assertEquals("1234*-5/+6-", calculator.getReversedPolishNotation(expression));

        InOrder inOrder = inOrder(operationsMock);

        inOrder.verify(operationsMock).push('+');
        inOrder.verify(operationsMock).push('(');
        inOrder.verify(operationsMock).push('-');
        inOrder.verify(operationsMock).push('*');
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).push('/');
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).pop();
        inOrder.verify(operationsMock).push('-');
        inOrder.verify(operationsMock).pop();
    }

    @Test
    void testProcessExpressionSum() {
        String expression = "12+";

        doNothing().when(operandsMock).push(anyDouble());
        when(operandsMock.pop()).thenReturn(2.0, 1.0, 3.0);

        assertEquals(3.0, calculator.processExpression(expression));

        InOrder inOrder = inOrder(operandsMock);

        inOrder.verify(operandsMock).push(1.0);
        inOrder.verify(operandsMock).push(2.0);
        inOrder.verify(operandsMock, times(2)).pop();
        inOrder.verify(operandsMock).push(3.0);
        inOrder.verify(operandsMock).pop();
    }

    @Test
    void testProcessExpressionSubtraction() {
        String expression = "57-";

        doNothing().when(operandsMock).push(anyDouble());
        when(operandsMock.pop()).thenReturn(7.0, 5.0, -2.0);

        assertEquals(-2.0, calculator.processExpression(expression));

        InOrder inOrder = inOrder(operandsMock);

        inOrder.verify(operandsMock).push(5.0);
        inOrder.verify(operandsMock).push(7.0);
        inOrder.verify(operandsMock, times(2)).pop();
        inOrder.verify(operandsMock).push(-2.0);
        inOrder.verify(operandsMock).pop();
    }

    @Test
    void testProcessExpressionMultiplication() {
        String expression = "37*";

        doNothing().when(operandsMock).push(anyDouble());
        when(operandsMock.pop()).thenReturn(7.0, 3.0, 21.0);

        assertEquals(21.0, calculator.processExpression(expression));

        InOrder inOrder = inOrder(operandsMock);

        inOrder.verify(operandsMock).push(3.0);
        inOrder.verify(operandsMock).push(7.0);
        inOrder.verify(operandsMock, times(2)).pop();
        inOrder.verify(operandsMock).push(21.0);
        inOrder.verify(operandsMock).pop();
    }

    @Test
    void testProcessExpressionDivision() {
        String expression = "94/";

        doNothing().when(operandsMock).push(anyDouble());
        when(operandsMock.pop()).thenReturn(4.0, 9.0, 2.25);

        assertEquals(2.25, calculator.processExpression(expression));

        InOrder inOrder = inOrder(operandsMock);

        inOrder.verify(operandsMock).push(9.0);
        inOrder.verify(operandsMock).push(4.0);
        inOrder.verify(operandsMock, times(2)).pop();
        inOrder.verify(operandsMock).push(2.25);
        inOrder.verify(operandsMock).pop();
    }

    @Test
    void testProcessExpressionComplex() {
        String expression = "532-4*-1+4/"; //(5 - (3 - 2) * 4 + 1) / 4 = 0.5

        doNothing().when(operandsMock).push(anyDouble());
        when(operandsMock.pop()).thenReturn(2.0, 3.0, 4.0, 1.0, 4.0, 5.0, 1.0, 1.0, 4.0, 2.0, 0.5);

        assertEquals(0.5, calculator.processExpression(expression));

        InOrder inOrder = inOrder(operandsMock);

        inOrder.verify(operandsMock).push(5.0);
        inOrder.verify(operandsMock).push(3.0);
        inOrder.verify(operandsMock).push(2.0);
        inOrder.verify(operandsMock, times(2)).pop();
        inOrder.verify(operandsMock).push(1.0);
        inOrder.verify(operandsMock).push(4.0);
        inOrder.verify(operandsMock, times(2)).pop();
        inOrder.verify(operandsMock).push(4.0);
        inOrder.verify(operandsMock, times(2)).pop();
        inOrder.verify(operandsMock, times(2)).push(1.0);
        inOrder.verify(operandsMock, times(2)).pop();
        inOrder.verify(operandsMock).push(2.0);
        inOrder.verify(operandsMock).push(4.0);
        inOrder.verify(operandsMock, times(2)).pop();
        inOrder.verify(operandsMock).push(0.5);
        inOrder.verify(operandsMock).pop();
    }

}