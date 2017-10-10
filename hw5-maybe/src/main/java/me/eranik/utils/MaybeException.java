package me.eranik.utils;

/**
 * Throws if trying to get value of empty Maybe.
 */
public class MaybeException extends Exception {
    public MaybeException() {
        super("Maybe does not contain any value");
    }
}
