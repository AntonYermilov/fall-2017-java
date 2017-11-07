package me.eranik.functional;

import org.jetbrains.annotations.NotNull;

/**
 * Wrapper over the function f :: T -> U.
 * @param <T> type of argument
 * @param <U> type of result
 */
public abstract class Function1<T, U> {
    /**
     * Returns the result of applying function to the specified argument.
     * @param x argument to be applied
     * @return the result of application
     */
    public abstract U apply(T x);

    /**
     * Returns composition with function g :: U -> V.
     * @param g specified function
     * @param <V> type of composition result
     * @return function h :: T -> V, such that h = g(f)
     */
    public <V> Function1<T, V> compose(@NotNull Function1<? super U, ? extends V> g) {
        return new Function1<T, V>() {
            @Override
            public V apply(@NotNull T x) {
                 return g.apply(Function1.this.apply(x));
            }
        };
    }
}
