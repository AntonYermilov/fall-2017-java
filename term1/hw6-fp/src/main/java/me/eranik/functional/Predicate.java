package me.eranik.functional;

import org.jetbrains.annotations.NotNull;

/**
 * Wrapper over the predicate -- special function that returns True
 * if argument matches the specified condition and False otherwise.
 * @param <T> type of specified argument
 */
public abstract class Predicate<T> extends Function1<T, Boolean> {
    /**
     * Returns predicate that for any argument returns true.
     * @param <T> type of argument
     * @return predicate that always returns true
     */
    public static <T> Predicate<T> ALWAYS_TRUE() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return true;
            }
        };
    }

    /**
     * Returns predicate that for any argument returns false.
     * @param <T> type of argument
     * @return predicate that always returns false
     */
    public static <T> Predicate<T> ALWAYS_FALSE() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return false;
            }
        };
    }

    /**
     * Returns the result of applying function to the specified
     * argument.
     * @param x argument to be applied
     * @return the result of the application
     */
    public abstract Boolean apply(@NotNull T x);

    /**
     * Returns predicate that equals to the {@code logical or}
     * of this predicate and specified one.
     * @param pred specified predicate
     * @return predicate that equals to the {@code logical or}
     * of this predicate and specified one
     */
    public Predicate<T> or(@NotNull Predicate<? super T> pred) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(@NotNull T x) {
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
    public Predicate<T> and(@NotNull Predicate<? super T> pred) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(@NotNull T x) {
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
            public Boolean apply(@NotNull T x) {
                return !Predicate.this.apply(x);
            }
        };
    }
}
