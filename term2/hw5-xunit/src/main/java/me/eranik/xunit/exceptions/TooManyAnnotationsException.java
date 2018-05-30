package me.eranik.xunit.exceptions;

/**
 * Exception which is thrown if class contains to or more
 * Before, BeforeClass, After or AfterClass annotations.
 */
public class TooManyAnnotationsException extends Exception {
    public TooManyAnnotationsException(String annotation) {
        super("Class can't contains two or more " + annotation + " annotations");
    }
}
