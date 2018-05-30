package me.eranik.xunit.exceptions;

/**
 * Exception which is thrown if test method expected an exception but it was not thrown.
 */
public class ExpectedExceptionNotThrown extends Exception {
    public ExpectedExceptionNotThrown(Class expected) {
        super("Expected " + expected.getName() + ", but it was not thrown");
    }
}
