package me.eranik.threads;

import java.util.function.Function;

/**
 * Special class that stores tasks and provides interaction with thread pool.
 * @param <T> type of result that task returns
 */
public interface LightFuture<T> {

    /**
     * Checks if task is already processed or not
     * @return {@code true} if task is already processed by thread pool; {@code false} otherwise
     */
    boolean isReady();

    /**
     * Waits until task processing is completed and returns its result.
     * @return task result
     * @throws LightExecutionException if exception occurred during task processing
     */
    T get() throws LightExecutionException;


    /**
     * Applies function to the result of task.
     * @param function specified function
     * @return result of application of specified function to the result of current task
     */
    LightFuture<T> thenApply(Function<T, T> function);

    /**
     * Exception that is thrown if some error occurred when executing task.
     */
    class LightExecutionException extends Exception {
        LightExecutionException(Exception e) {
            super(e);
        }
    }

}