package me.eranik.util.exceptions;

public class AmbiguousImplementationException extends Exception {
    public AmbiguousImplementationException() {
        super("Found multiple implementations of class");
    }
}
