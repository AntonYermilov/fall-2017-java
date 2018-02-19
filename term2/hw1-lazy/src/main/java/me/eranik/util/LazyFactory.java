package me.eranik.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Provides an ability to create instances of lazy classes.
 */
public class LazyFactory {

    private LazyFactory() {}

    /**
     * Creates lazy version of specified job.
     * @param supplier specified job
     * @param <T> type of return value
     * @return lazy version of specified job
     */
    public static <T> Lazy<T> createLazy(@NotNull Supplier<T> supplier) {
        return new LazyImpl<>(supplier);
    }

    /**
     * Creates thread-safe lazy version of specified job.
     * @param supplier specified job
     * @param <T> type of return value
     * @return thread-safe lazy version of specified job
     */
    public static <T> Lazy<T> createConcurrentLazy(@NotNull Supplier<T> supplier) {
        return new ConcurrentLazyImpl<>(supplier);
    }

    /**
     * Implementation of Lazy interface.
     * @param <T> type of return value
     */
    private static class LazyImpl<T> implements Lazy<T> {

        private Supplier<T> supplier;
        private T result = null;

        /**
         * Creates lazy version of the specified job
         * @param supplier specified job
         */
        public LazyImpl(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public T get() {
            if (supplier != null) {
                result = supplier.get();
                supplier = null;
            }
            return result;
        }
    }

    /**
     * Thread-safe implementation of Lazy interface.
     * @param <T> type of return value
     */
    private static class ConcurrentLazyImpl<T> implements Lazy<T> {

        private Supplier<T> supplier;
        private T result = null;

        /**
         * Creates thread-safe lazy version of the specified job
         * @param supplier specified job
         */
        public ConcurrentLazyImpl(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public synchronized T get() {
            if (supplier != null) {
                result = supplier.get();
                supplier = null;
            }
            return result;
        }
    }

}
