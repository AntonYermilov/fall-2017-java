package me.eranik.utils;

/**
 * Throws if trying to get value of empty Maybe.
 */
public class AccessToNothingException extends Exception {
    public AccessToNothingException() {
        super("Maybe does not contain any value");
    }
}
