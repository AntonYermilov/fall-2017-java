package me.eranik.util.exceptions;

public class ImplementationNotFoundException extends Exception {
    public ImplementationNotFoundException() {
        super("Found no implementation of the specified class");
    }
}
