package me.eranik;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class NumberReaderTest {
    private File input;
    private File output;

    @BeforeEach
    void createFiles() throws IOException {
        Paths.get("src", "test", "resources").toFile().mkdir();
        input = Paths.get("src", "test", "resources", "input.txt").toFile();
        input.createNewFile();
        output = Paths.get("src", "test", "resources", "output.txt").toFile();
        output.createNewFile();
    }

    @AfterEach
    void removeFiles() throws IOException {
        input.delete();
        output.delete();
    }

    @Test
    void testMainNumbers() throws IOException {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(input)));
        pw.println(1);
        pw.println(1024);
        pw.println(566);
        pw.close();

        NumberReader.main(new String[]{input.toString(), output.toString()});
        Scanner sc = new Scanner(new InputStreamReader(new FileInputStream(output)));
        assertEquals(1, sc.nextInt());
        assertEquals(1024 * 1024, sc.nextInt());
        assertEquals(566 * 566, sc.nextInt());
        assertFalse(sc.hasNext());
        sc.close();
    }

    @Test
    void testMainNoNumbers() throws IOException {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(input)));
        pw.println("123h");
        pw.println("abc");
        pw.println("Hello world! 321");
        pw.close();

        NumberReader.main(new String[]{input.toString(), output.toString()});
        Scanner sc = new Scanner(new InputStreamReader(new FileInputStream(output)));
        assertEquals("null", sc.next());
        assertEquals("null", sc.next());
        assertEquals("null", sc.next());
        assertFalse(sc.hasNext());
        sc.close();
    }

    @Test
    void testMainMixed() throws IOException {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(input)));
        pw.println(1);
        pw.println("abc");
        pw.println(566 + "Hello World");
        pw.println(12);
        pw.close();

        NumberReader.main(new String[]{input.toString(), output.toString()});
        Scanner sc = new Scanner(new InputStreamReader(new FileInputStream(output)));
        assertEquals("1", sc.next());
        assertEquals("null", sc.next());
        assertEquals("null", sc.next());
        assertEquals("144", sc.next());
        assertFalse(sc.hasNext());
        sc.close();
    }

}