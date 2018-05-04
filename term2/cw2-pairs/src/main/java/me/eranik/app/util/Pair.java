package me.eranik.app.util;

/**
 * Stores pair of elements of the specified types.
 * @param <T> type of first element
 * @param <U> type of second element
 */
public class Pair<T, U> {
    public T first;
    public U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Pair) {
            Pair p = (Pair) o;
            return first.equals(p.first) && second.equals(p.second);
        }
        return false;
    }
}
