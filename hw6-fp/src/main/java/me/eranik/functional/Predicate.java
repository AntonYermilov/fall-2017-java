package me.eranik.functional;

/**
 * Wrapper over the predicate -- special function that returns True
 * if argument matches the specified condition and False otherwise.
 * @param <T> type of specified argument
 */
public abstract class Predicate<T> extends Function1<T, Boolean> {
    /**
     * Predicate that always returns true.
     */
    public static final Predicate ALWAYS_TRUE = new Predicate() {
        @Override
        public Boolean apply(Object x) {
            return true;
        }
    };

    /**
     * Predicate that always returns false.
     */
    public static final Predicate ALWAYS_FALSE = new Predicate() {
        @Override
        public Boolean apply(Object x) {
            return false;
        }
    };

    /**
     * Returns the result of applying function to the specified
     * argument.
     * @param x argument to be applied
     * @return the result of the application
     */
    public abstract Boolean apply(T x);

    /**
     * Returns predicate that equals to the {@code logical or}
     * of this predicate and specified one.
     * @param pred specified predicate
     * @return predicate that equals to the {@code logical or}
     * of this predicate and specified one
     */
    public Predicate<T> or(Predicate<? super T> pred) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return Predicate.this.apply(x) || pred.apply(x);
            }
        };
    }

    /**
     * Returns predicate that equals to the {@code logical and}
     * of this predicate and specified one.
     * @param pred specified predicate
     * @return predicate that equals to the {@code logical and}
     * of this predicate and specified one
     */
    public Predicate<T> and(Predicate<? super T> pred) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return Predicate.this.apply(x) && pred.apply(x);
            }
        };
    }

    /**
     * Returns predicate that equals to the {@code logical not}
     * of this predicate.
     * @return predicate that equals to the {@code logical not}
     * of this predicate
     */
    public Predicate<T> not() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return !Predicate.this.apply(x);
            }
        };
    }
}
