package me.eranik.utils;

import java.util.function.Function;

public class Maybe<T> {
    private T value;

    private Maybe() {
        this.value = null;
    }

    private Maybe(T value) {
        this.value = value;
    }

    /**
     * Creates new Maybe with specified value.
     * @param value value to be used in Maybe
     * @param <U> type of Maybe
     * @return new Maybe with specified value
     */
    public static <U> Maybe<U> just(U value) {
        return new Maybe<>(value);
    }

    /**
     * Creates empty Maybe.
     * @param <U> type of Maybe
     * @return new empty Maybe with specified type
     */
    public static <U> Maybe<U> nothing() {
        return new Maybe<>();
    }

    /**
     * Returns value that is stored in Maybe. Throws exception if value does not exist.
     * @return value that is stored in Maybe
     * @throws MaybeException
     */
    public T get() throws MaybeException {
        if (value == null)
            throw new MaybeException();
        return value;
    }

    /**
     * Tells if Maybe contains any value.
     * @return true if Maybe contains something; false otherwise
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * Applies the specified function to the stored value and returns just value.
     * If Maybe is empty returns nothing.
     * @param mapper function to be applied
     * @param <U> type of result Maybe
     * @return just value with applied function, if current one is not empty;
     *         nothing otherwise
     */
    public <U> Maybe<U> map(Function<? super T, ? extends U> mapper) {
        if (value == null)
            return new Maybe<>();
        return new Maybe<>(mapper.apply(value));
    }

    /**
     * Converts string value to Integer.
     * @param strValue string value
     * @return just Integer value, if conversion was successful; nothing otherwise
     */
    public static Maybe<Integer> convertToInteger(String strValue) {
        try {
            return Maybe.just(Integer.valueOf(strValue));
        } catch (NumberFormatException e) {
            return Maybe.nothing();
        }
    }

}
