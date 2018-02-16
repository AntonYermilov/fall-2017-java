package me.eranik.util;

import java.util.Arrays;

public class Matrix {
    private int size = 0;
    private int matrix[][];

    /**
     * Constructs square matrix, stores it as transpose matrix. Size of matrix should be odd.
     * @param matrix matrix to be stored
     */
    public Matrix(int matrix[][]) {
        boolean correct = matrix != null && matrix.length % 2 == 1;
        for (int i = 0; correct && i < matrix.length; i++) {
            correct = matrix[i] != null && matrix[i].length == matrix.length;
        }
        if (!correct) {
            throw new IllegalArgumentException("Matrix dimensions are incorrect");
        }

        this.size = matrix.length;
        this.matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.matrix[i][j] = matrix[j][i];
            }
        }
    }

    /**
     * Prints elements of matrix on the screen spirally, starting
     * from the center of matrix.
     */
    public void drawSpiral() {
        int i = size / 2;
        int j = size / 2;
        for (int radius = 0; radius < (size + 1) / 2; radius++) {
            if (radius == 0) {
                System.out.print(matrix[i][j] + " ");
                i--;
                continue;
            }

            for (int down = 0; down < radius * 2 - 1; down++) {
                System.out.print(matrix[i][j] + " ");
                j++;
            }
            for (int right = 0; right < radius * 2; right++) {
                System.out.print(matrix[i][j] + " ");
                i++;
            }
            for (int up = 0; up < radius * 2; up++) {
                System.out.print(matrix[i][j] + " ");
                j--;
            }
            for (int left = 0; left < radius * 2 + 1; left++) {
                System.out.print(matrix[i][j] + " ");
                i--;
            }
        }
        System.out.println();
    }

    /**
     * Sorts columns of the matrix ascending first elements.
     */
    public void sortColumns() {
        Arrays.sort(matrix, (o1, o2) -> {
            if (o1[0] == o2[0])
                return 0;
            return o1[0] > o2[0] ? 1 : -1;
        });
    }

    /**
     * Prints matrix on the screen.
     */
    public void printMatrix() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(matrix[j][i]);
                System.out.print(j + 1 < size ? " " : "\n");
            }
        }
    }

}
