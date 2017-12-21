package me.eranik.math;

import me.eranik.util.Stack;

/**
 * Implements algorithms of transforming expressions to reversed polish notation
 * and calculating this expressions.
 */
public class Calculator {
    private Stack<Character> operations;
    private Stack<Double> operands;

    /**
     * Uses specified stack to calculate expressions.
     * @param operations stack to store operations
     * @param operands stack to store operands
     */
    public Calculator(Stack<Character> operations, Stack<Double> operands) {
        this.operations = operations;
        this.operands = operands;
    }

    /**
     * Receives an expression in infix notation.
     * Returns this expression in reversed polish notation.
     * @param expr specified expression in infix notation
     * @return expression in reversed polish notation
     * @throws ParseException if specified expression is not in infix notation or
     * contains symbols, different from digits or supported operations (+, -, *, /)
     */
    public String getReversedPolishNotation(String expr) throws ParseException {
        operations.clear();

        if (!checkCorrectness(expr)) {
            throw new ParseException();
        }

        StringBuilder builder = new StringBuilder();
        for (char symbol : expr.toCharArray()) {
            if (symbol == '(') {
                operations.push(symbol);
                continue;
            }
            if (symbol == ')') {
                while (!operations.empty() && operations.top() != '(') {
                    builder.append(operations.pop());
                }
                operations.pop();
                continue;
            }
            if (isOperation(symbol)) {
                while (!operations.empty() && getPriority(symbol) <= getPriority(operations.top())) {
                    builder.append(operations.pop());
                }
                operations.push(symbol);
                continue;
            }
            builder.append(symbol);
        }
        while (!operations.empty()) {
            builder.append(operations.pop());
        }

        return builder.toString();
    }

    /**
     * Receives an expression in reversed polish notation and calculates it.
     * @param expr specified expression in reversed polish notation
     * @return the result of evaluating this expression
     */
    public double processExpression(String expr) {
        operands.clear();

        int index = 0;
        while (index < expr.length()) {
            if (isDigit(expr.charAt(index))) {
                operands.push(toDigit(expr.charAt(index)));
            } else {
                double rightOperand = operands.pop();
                double leftOperand = operands.pop();
                operands.push(apply(expr.charAt(index), leftOperand, rightOperand));
            }
            index++;
        }

        return operands.pop();
    }

    /**
     * Checks if received string is correct mathematical expression.
     * @param expression specified expression
     * @return {@code true} if specified string satisfies the correct mathematical
     * expression; {@code false} otherwise
     */
    private boolean checkCorrectness(String expression) {
        expression = "0+" + expression + "+0";

        int opened = 0;
        for (int i = 0; i < expression.length() - 1; i++) {
            char current = expression.charAt(i);
            char next = expression.charAt(i + 1);
            if (isDigit(current)) {
                if (isDigit(next) || next == '(') {
                    return false;
                }
                continue;
            }
            if (isOperation(current)) {
                if (isOperation(next) || next == ')') {
                    return false;
                }
                continue;
            }
            if (current == '(') {
                opened++;
                if (isOperation(next)) {
                    return false;
                }
                continue;
            }
            if (current == ')') {
                opened--;
                if (isDigit(next) || opened < 0) {
                    return false;
                }
                continue;
            }
            return false;
        }

        return opened == 0;
    }

    private int getPriority(char operation) {
        switch (operation) {
            case '+' :
            case '-' : 
                return 1;
            case '*' : 
                return 2;
            case '/' : 
                return 2;
            default  : 
                return 0;
        }
    }

    private double apply(char operation, double leftOperand, double rightOperand) {
        switch (operation) {
            case '+' : 
                return leftOperand + rightOperand;
            case '-' : 
                return leftOperand - rightOperand;
            case '*' : 
                return leftOperand * rightOperand;
            case '/' : 
                return leftOperand / rightOperand;
        }
        throw new UnsupportedOperationException();
    }

    private double toDigit(char symbol) {
        return symbol - '0';
    }

    private boolean isDigit(char symbol) {
        return '0' <= symbol && symbol <= '9';
    }

    private boolean isOperation(char symbol) {
        return "+-*/".indexOf(symbol) != -1;
    }

    /**
     * This exception is thrown if specified string does not satisfy correct mathematical
     * expression.
     * Correct expression can consist of operations +, -, * or /, parentheses and integers
     * between 0 and 9 inclusive.
     */
    public static class ParseException extends Exception {
        private ParseException() {
            super("Specified expression is not a correct expression in infix form.\n" +
                    "Correct expression can contain only digits, parentheses and " +
                    "binary operations '+', '-', '*' or '/'.\n" +
                    "In particular, all numbers must be from 0 to 9.\n" +
                    "Try again.");
        }
    }

}
