package me.eranik;

import me.eranik.math.Calculator;
import me.eranik.util.DefaultStack;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Console application that reads math expressions and calculates them.
 */
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

        Calculator calculator = new Calculator(new DefaultStack<>(), new DefaultStack<>());

        while (in.hasNext()) {
            String expression = in.nextLine();
            try {
                String reversedPolishNotation = calculator.getReversedPolishNotation(expression);
                out.println(calculator.processExpression(reversedPolishNotation));
            } catch (Calculator.ParseException e) {
                out.println(e.getMessage());
            }
            out.flush();
        }

        in.close();
        out.close();
    }
}
