package me.eranik.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class MatrixTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Before
    public void setOutputStream() {
        System.setOut(new PrintStream(output));
    }

    @Test(expected = IllegalArgumentException.class)
    public void matrixConstructor_NullMatrix() {
        new Matrix(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void matrixConstructor_InvalidDimensions_1() {
        new Matrix(new int[][]{{1, 2}, {3, 4}});
    }

    @Test(expected = IllegalArgumentException.class)
    public void matrixConstructor_InvalidDimensions_2() {
        new Matrix(new int[][]{{1, 2, 3}, {4, 5}, {7, 8, 9}});
    }

    @Test
    public void matrixConstructor_ValidDimensions() throws Exception {
        new Matrix(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
    }

    @Test
    public void drawSpiral_1() throws Exception {
        Matrix matrix =  new Matrix(new int[][]{{1}});
        matrix.drawSpiral();
        assertEquals("1", output.toString().trim());
    }

    @Test
    public void drawSpiral_2() throws Exception {
        Matrix matrix =  new Matrix(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        matrix.drawSpiral();
        assertEquals("5 4 7 8 9 6 3 2 1", output.toString().trim());
    }

    @Test
    public void printMatrix_1() throws Exception {
        Matrix matrix = new Matrix(new int[][]{{1}});
        matrix.printMatrix();
        assertEquals("1", output.toString().trim());
    }

    @Test
    public void printMatrix_2() throws Exception {
        Matrix matrix = new Matrix(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        matrix.printMatrix();
        assertEquals("1 2 3\n4 5 6\n7 8 9", output.toString().trim());
    }

    @Test
    public void sortColumns_1() throws Exception {
        Matrix matrix = new Matrix(new int[][]{{1}});
        matrix.sortColumns();
        matrix.printMatrix();
        assertEquals("1", output.toString().trim());
    }

    @Test
    public void sortColumns_2() throws Exception {
        Matrix matrix = new Matrix(new int[][]{{2, 1, 3}, {4, 5, 6}, {9, 7, 8}});
        matrix.sortColumns();
        matrix.printMatrix();
        assertEquals("1 2 3\n5 4 6\n7 9 8", output.toString().trim());
    }

    @After
    public void cleanOutputStream() {
        System.setOut(null);
    }

}