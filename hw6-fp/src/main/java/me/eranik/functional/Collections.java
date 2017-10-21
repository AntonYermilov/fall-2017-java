package me.eranik.functional;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class contains different algorithms that work with functions and sequences.
 */
public class Collections {
    /**
     * Maps specified function to the elements of specified iterable container.
     * Returns the array of resulting values.
     * @param func function f :: T -> U
     * @param values iterable container
     * @param <T> type of values in the specified container
     * @param <U> type of result values
     * @return the array of resulting values
     */
    public static <T, U> ArrayList<U> map(Function1<? super T, ? extends U> func,
                                          Iterable<? extends T> values) {
        ArrayList<U> newValues = new ArrayList<>();
        for (T value : values) {
            newValues.add(func.apply(value));
        }
        return newValues;
    }

    /**
     * Returns the subset of the specified container's values that correspond
     * to the predicate.
     * @param pred predicate p :: T -> Boolean
     * @param values iterable container
     * @param <T> type of values in the specified container
     * @return the array of values that correspond to the specified predicate
     */
    public static <T> ArrayList<T> filter(Predicate<? super T> pred,
                                          Iterable<? extends T> values) {
        ArrayList<T> newValues = new ArrayList<>();
        for (T value : values) {
            if (pred.apply(value)) {
                newValues.add(value);
            }
        }
        return newValues;
    }

    /**
     * Returns the longest prefix of the specified container of elements that
     * satisfy specified predicate.
     * @param pred predicate p :: T -> Boolean
     * @param values iterable container
     * @param <T> type of values in the specified container
     * @return the longest prefix of the specified container of elements that
     * satisfy specified predicate
     */
    public static <T> ArrayList<T> takeWhile(Predicate<? super T> pred,
                                             Iterable<? extends T> values) {
        ArrayList<T> newValues = new ArrayList<>();
        for (T value : values) {
            if (!pred.apply(value)) {
                break;
            }
            newValues.add(value);
        }
        return newValues;
    }

    /**
     * Returns the longest prefix of the specified container of elements that
     * do not satisfy specified predicate.
     * @param pred predicate p :: T -> Boolean
     * @param values iterable container
     * @param <T> type of values in the specified container
     * @return the longest prefix of the specified container of elements that
     * do not satisfy specified predicate
     */
    public static <T> ArrayList<T> takeUnless(Predicate<? super T> pred,
                                              Iterable<? extends T> values) {
        return takeWhile(pred.not(), values);
    }

    private static <T, U> U applyBackwards(Function2<? super T, ? super U, ? extends U> func,
                                           U init, Iterator<? extends T> iterator) {
        if (iterator.hasNext()) {
            return func.apply(iterator.next(), applyBackwards(func, init, iterator));
        }
        return init;
    }

    /**
     * Reduces the specified container using the function of two arguments
     * from right to left.
     * @param func function f :: T -> U -> U
     * @param init initial value, reduction starts with it
     * @param values iterable container
     * @param <T> type of values in the specified container
     * @param <U> type of reduction result
     * @return result of the container's reduction
     */
    public static <T, U> U foldr(Function2<? super T, ? super U, ? extends U> func,
                                 U init, Iterable<? extends T> values) {
        return applyBackwards(func, init, values.iterator());
    }

    /**
     * Reduces the specified container using the function of two arguments
     * from left to right.
     * @param func function f :: T -> U -> T
     * @param init initial value, reduction starts with it
     * @param values iterable container
     * @param <T> type of reduction result
     * @param <U> type of values in the specified container
     * @return result of the container's reduction
     */
    public static <T, U> T foldl(Function2<? super T, ? super U, ? extends T> func,
                                 T init, Iterable<? extends U> values) {
        T result = init;
        for (U value : values) {
            result = func.apply(result, value);
        }
        return result;
    }
}
