package me.eranik;

import me.eranik.utils.*;

import java.io.*;
import java.util.Scanner;

public class NumberReader {

    /**
     * Reads numbers from the input file and writes their squares to the output file.
     * @param args names of input and output files
     * @throws IOException if input or output file does not exist.
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Expected the name of the input file and " +
                    "the name of the output file, try again.");
            return;
        }

        File inputFile = new File(args[0]);
        if (!inputFile.isFile()) {
            System.out.println(args[0] + " does not exist or is not a file");
            return;
        }

        File outputFile = new File(args[1]);
        outputFile.getParentFile().mkdirs();
        outputFile.createNewFile();

        Scanner input = new Scanner(new InputStreamReader(new FileInputStream(inputFile)));
        PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));

        while (input.hasNext()) {
            String next = input.nextLine().trim();
            Maybe<Integer> value = Maybe.convertToInteger(next).map(x -> x * x);
            try {
                output.write(value.get() + "\n");
            } catch (AccessToNothingException e) {
                output.write("null\n");
            }
        }

        input.close();
        output.close();
    }
}
