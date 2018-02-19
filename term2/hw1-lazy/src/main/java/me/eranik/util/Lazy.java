package me.eranik.util;

/**
 * Describes interface that provides lazy calculation of stored job.
 * @param <T> type of return value
 */
public interface Lazy<T> {

    /**
     * Returns the result of job calculation. Every next method call will return the result immediately,
     * without another calculation.
     * @return the result of job calculation
     */
    T get();
}
