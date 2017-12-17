package me.eranik.util.exceptions;

public class InjectionCycleException extends Exception {
    public InjectionCycleException() {
        super("Found cycle in dependencies");
    }
}
