package me.eranik.xunit;

import me.eranik.xunit.exceptions.IncompatibleAnnotationsException;
import me.eranik.xunit.exceptions.TooManyAnnotationsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestInvokerTest {

    private static ByteArrayOutputStream output;
    private static Method method;

    @BeforeAll
    static void initialize() throws NoSuchMethodException {
        method = TestInvoker.class.getDeclaredMethod("processClass", Class.class);
        method.setAccessible(true);
    }

    @BeforeEach
    void start() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void finish() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }

    @Test
    void testIncompatibilityAnnotations() throws ClassNotFoundException {
        for (int i = 1; i <= 10; i++) {
            Class test = ClassLoader.getSystemClassLoader().loadClass("examples.IncompatibleAnnotations" + i);
            assertThrows(IncompatibleAnnotationsException.class, () -> {
                try {
                    method.invoke(null, test);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
            });
        }
    }

    @Test
    void testTooManyAnnotations() throws ClassNotFoundException {
        for (int i = 1; i <= 4; i++) {
            Class test = ClassLoader.getSystemClassLoader().loadClass("examples.TooManyAnnotations" + i);
            assertThrows(TooManyAnnotationsException.class, () -> {
                try {
                    method.invoke(null, test);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
            });
        }
    }

    @Test
    void testNoDefaultConstructor() throws ClassNotFoundException {
        Class test1 = ClassLoader.getSystemClassLoader().loadClass("examples.NoDefaultConstructor1");
        assertThrows(IllegalAccessException.class, () -> {
            try {
                method.invoke(null, test1);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });

        Class test2 = ClassLoader.getSystemClassLoader().loadClass("examples.NoDefaultConstructor2");
        assertThrows(InstantiationException.class, () -> {
            try {
                method.invoke(null, test2);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void testValidAnnotations() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        for (int i = 1; i <= 3; i++) {
            Class test = ClassLoader.getSystemClassLoader().loadClass("examples.ValidAnnotations" + i);
            assertEquals(null, method.invoke(null, test));
        }
    }

    @Test
    void testAnnotationOrder() throws ClassNotFoundException, InvocationTargetException,
            IllegalAccessException, NoSuchFieldException {
        Class test = ClassLoader.getSystemClassLoader().loadClass("examples.Order");

        Field field = test.getDeclaredField("writer");
        field.setAccessible(true);
        StringWriter output = (StringWriter) field.get(null);

        assertEquals(null, method.invoke(null, test));
        assertEquals("12342342345", output.toString());
    }

    @Test
    void testCorrectTests() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class test = ClassLoader.getSystemClassLoader().loadClass("examples.CorrectTests");
        assertEquals(null, method.invoke(null, test));

        assertTrue(output.toString().contains("Tests passed: 2/2"));
        assertTrue(output.toString().contains("Test test1 passed"));
        assertTrue(output.toString().contains("Test test2 passed"));
        assertTrue(output.toString().contains("Test test3 ignored"));
    }

    @Test
    void testIncorrectTests1() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class test = ClassLoader.getSystemClassLoader().loadClass("examples.IncorrectTests1");
        assertEquals(null, method.invoke(null, test));

        assertTrue(output.toString().contains("Tests passed: 2/3"));
        assertTrue(output.toString().contains("Test test1 passed"));
        assertTrue(output.toString().contains("Test test2 passed"));
        assertTrue(output.toString().contains("Test test3 failed"));
    }

    @Test
    void testIncorrectTests2() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class test = ClassLoader.getSystemClassLoader().loadClass("examples.IncorrectTests2");
        assertEquals(null, method.invoke(null, test));

        assertTrue(output.toString().contains("Tests passed: 1/2"));
        assertTrue(output.toString().contains("Test test1 passed"));
        assertTrue(output.toString().contains("Test test2 failed"));
        assertTrue(output.toString().contains("Test test3 ignored"));
    }

    @Test
    void testInvokerFull() {
        String dir = Paths.get("src", "test", "resources").toString();
        String name = "examples.CorrectTests";
        TestInvoker.main(new String[]{dir, name});

        assertTrue(output.toString().contains("Tests passed: 2/2"));
        assertTrue(output.toString().contains("Test test1 passed"));
        assertTrue(output.toString().contains("Test test2 passed"));
        assertTrue(output.toString().contains("Test test3 ignored"));
    }

}
