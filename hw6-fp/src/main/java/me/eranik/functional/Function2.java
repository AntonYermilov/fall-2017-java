package me.eranik.functional;

/**
 * Wrapper over the function f :: T -> U -> V.
 * @param <T> type of first argument
 * @param <U> type of second argument
 * @param <V> type of result
 */
public abstract class Function2<T, U, V> {
    /**
     * Returns the result of applying function to the specified arguments.
     * @param x first argument
     * @param y second argument
     * @return the result of application
     */
    public abstract V apply(T x, U y);

    /**
     * Returns composition with function g :: V -> W.
     * @param g specified function
     * @param <W> type of composition result
     * @return function h :: T -> U -> W, such that h = g(f)
     */
    public <W> Function2<T, U, W> compose(Function1<? super V, ? extends W> g) {
        return new Function2<T, U, W>() {
            @Override
            public W apply(T x, U y) {
                return g.apply(Function2.this.apply(x, y));
            }
        };
    }

    /**
     * Binds first argument of the function to the specified value.
     * @param x value to be bound with
     * @return function of one argument, that is similar to f(_, y)
     */
    public Function1<U, V> bind1(T x) {
        return new Function1<U, V>() {
            @Override
            public V apply(U y) {
                return Function2.this.apply(x, y);
            }
        };
    }

    /**
     * Binds second argument of the function to the specified value.
     * @param y value to be bound with
     * @return function of one argument, that is similar to f(x, _)
     */
    public Function1<T, V> bind2(U y) {
        return new Function1<T, V>() {
            @Override
            public V apply(T x) {
                return Function2.this.apply(x, y);
            }
        };
    }

    /**
     * Curries second argument of the function, substituting for it the specified value.
     * @param y value to be substituted
     * @return function of one argument, that is similar to f(x, _)
     */
    public Function1<T, V> curry(U y) {
        return bind2(y);
    }
}
