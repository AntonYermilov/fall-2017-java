package me.eranik.xunit.exceptions;

/**
 * Exception which is thrown if method annotated with incompatible exceptions.
 */
public class IncompatibleAnnotationsException extends Exception {
    public IncompatibleAnnotationsException() {
        super("No method can be annotated with different XUnit annotations.");
    }
}