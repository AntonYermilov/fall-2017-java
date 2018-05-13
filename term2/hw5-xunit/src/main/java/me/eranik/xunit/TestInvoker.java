package me.eranik.xunit;

import me.eranik.xunit.annotations.*;
import me.eranik.xunit.exceptions.ExpectedExceptionNotThrown;
import me.eranik.xunit.exceptions.TooManyAnnotationsException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for loading classes with special tests methods and invoking them.
 * Takes path to the java class with tests as an argument.
 * Invokes these tests and shows information about them.
 */
public class TestInvoker {

    /**
     * Takes path to .class file with tests as an argument, loads specified class and invokes tests in it.
     * Test is the method annotated with {@code Test} annotation from {@code me.eranik.xunit.annotations}.
     * Also it provides possibility to run some methods either before/after each method or before/after
     * invoking all test methods in class. Such methods should be annotated with corresponding annotations.
     * @param args list of argument which should contain only path to .class file
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Expected one argument: <path to .class file with tests>");
            System.exit(1);
        }

        String dir = Paths.get(args[0]).getParent().toString();
        String name = Paths.get(args[0]).getFileName().toString();

        ClassLoader loader = null;
        try {
            loader = new URLClassLoader(new URL[]{new File(dir).toURI().toURL()});
        } catch (MalformedURLException e) {
            System.out.println("Path to the class seems to be incorrect.");
            System.out.println("Check it and try once more.");
            System.exit(2);
        }

        try {
            processClass(loader.loadClass(name));
        } catch (ClassNotFoundException e) {
            System.out.println("Class " + name + " was not found.");
            System.out.println("Check provided arguments and try once more.");
            System.exit(3);
        } catch (TooManyAnnotationsException e) {
            System.out.println(e.getMessage());
            System.exit(4);
        }
    }

    /**
     * Gets methods annotated with {@code me.eranik.xunit.annotations} and invokes them.
     * @param testClass class with test methods
     * @throws TooManyAnnotationsException if class contains two or more methods annotated with
     *                                     Before, BeforeClass, After or AfterClass annotations
     */
    private static void processClass(@NotNull Class testClass) throws TooManyAnnotationsException {
        ClassMethods classMethods = getTestMethods(testClass);
        invoke(classMethods);
    }

    /**
     * Returns methods annotated with {@code me.eranik.xunit.annotations}.
     * @param testClass class with test methods
     * @return annotated methods to invoke
     * @throws TooManyAnnotationsException if class contains two or more methods annotated with
     *                                     Before, BeforeClass, After or AfterClass annotations
     */
    private static ClassMethods getTestMethods(@NotNull Class testClass) throws TooManyAnnotationsException {
        ClassMethods classMethods = new ClassMethods();

        for (Method method : testClass.getMethods()) {
            if (method.getAnnotation(Test.class) != null) {
                Test test = method.getAnnotation(Test.class);
                classMethods.tests.add(new TestMethod(method, test.expected(), test.ignore()));
            }
            if (method.getAnnotation(Before.class) != null) {
                if (classMethods.beforeMethod == null) {
                    classMethods.beforeMethod = method;
                } else {
                    throw new TooManyAnnotationsException("Before");
                }
            }
            if (method.getAnnotation(After.class) != null) {
                if (classMethods.afterMethod == null) {
                    classMethods.afterMethod = method;
                } else {
                    throw new TooManyAnnotationsException("After");
                }
            }
            if (method.getAnnotation(BeforeClass.class) != null) {
                if (classMethods.beforeClassMethod == null) {
                    classMethods.beforeClassMethod = method;
                } else {
                    throw new TooManyAnnotationsException("BeforeClass");
                }
            }
            if (method.getAnnotation(AfterClass.class) != null) {
                if (classMethods.afterClassMethod == null) {
                    classMethods.afterClassMethod = method;
                } else {
                    throw new TooManyAnnotationsException("AfterClass");
                }
            }
        }

        return classMethods;
    }

    /**
     * Invokes methods annotated with {@code me.eranik.xunit.annotations} in the following order:
     * 1. Invokes method annotated with {@code BeforeClass} if present
     * 2. For each method annotated with {@code Test}:
     *    2.1. Invokes method annotated with {@code Before} if present
     *    2.2. Invokes test method itself
     *    2.3. Invokes method annotated with {@code After} if present
     * 3. Invokes method annotated with {@code AfterClass} if present
     * Prints to the standard output stream information about tests.
     * @param classMethods annotated methods to invoke
     */
    private static void invoke(@NotNull ClassMethods classMethods) {
        int passedTests = 0;
        int allTests = classMethods.tests.size();
        double totalTime = 0;

        try {
            if (classMethods.beforeClassMethod != null) {
                classMethods.beforeClassMethod.invoke(null);
            }

            for (TestMethod test : classMethods.tests) {
                if (!test.ignore.equals(Test.EMPTY)) {
                    System.out.printf("Test %s ignored\nReason: %s\n\n", test.test.getName(), test.ignore);
                    allTests--;
                    continue;
                }

                long startTime = System.currentTimeMillis();

                boolean success = true;
                String reason = null;

                try {
                    if (classMethods.beforeMethod != null) {
                        classMethods.beforeMethod.invoke(null);
                    }

                    try {
                        test.test.invoke(null);
                        if (!test.expected.equals(Test.NONE.class)) {
                            throw new ExpectedExceptionNotThrown(test.expected);
                        }
                    } catch (Exception e) {
                        if (!test.expected.equals(e.getClass())) {
                            throw e;
                        }
                    }

                    if (classMethods.afterMethod != null) {
                        classMethods.afterMethod.invoke(null);
                    }
                } catch (Exception e) {
                    success = false;
                    reason = e.getMessage();

                }

                long endTime = System.currentTimeMillis();
                double time = 1.0 * (endTime - startTime) / 1000;

                if (success) {
                    System.out.printf("Test %s passed\nTime: %f sec.\n\n",
                            test.test.getName(), time);
                } else {
                    System.out.printf("Test %s failed\nReason: %s\nTime: %f sec.\n\n",
                            test.test.getName(), reason, time);
                }

                passedTests++;
                totalTime += time;
            }

            if (classMethods.afterClassMethod != null) {
                classMethods.afterClassMethod.invoke(null);
            }
        } catch (Exception e) {
            System.out.println("Unexpected exception occurred:");
            System.out.println(e.getMessage());
        }

        System.out.println("Test invocation finished");
        System.out.printf("Tests passed: %d/%d\n", passedTests, allTests);
        System.out.printf("Total time: %f\n", totalTime);
    }

    /**
     * Class that describes method annotated as {@code Test}.
     * It contains test method itself and annotation arguments: {@code expected} exception if present or
     * {@code ignore} string if we want to ignore some method.
     * For more information look at {@code Test} annotation.
     */
    private static class TestMethod {
        Method test;
        Class expected;
        String ignore;

        TestMethod(@NotNull Method test, @NotNull Class expected, @NotNull String ignore) {
            this.test = test;
            this.expected = expected;
            this.ignore = ignore;
        }
    }

    /**
     * Class that contains methods annotated with {@code me.eranik.xunit.annotations}.
     */
    private static class ClassMethods {
        List<TestMethod> tests = new ArrayList<>();
        Method beforeMethod = null;
        Method afterMethod = null;
        Method beforeClassMethod = null;
        Method afterClassMethod = null;

        ClassMethods() {}
    }

}
