package me.eranik;

import me.eranik.util.Matrix;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);

        int size = in.nextInt();
        int arr[][] = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                arr[i][j] = in.nextInt();
            }
        }

        Matrix matrix = new Matrix(arr);
        matrix.sortColumns();
        matrix.drawSpiral();
    }
}
