package me.eranik.utils;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Implementation of MyTreeSetInterface using binary search tree.
 * @param <K> type of the stored keys
 */
public class MyTreeSet<K> extends AbstractSet<K> implements MyTreeSetInterface<K> {

    private Tree<K> tree;
    private boolean ascendingOrder = true;

    /**        Node parent = null;

     * Constructs set with default comparator if stored keys implement Comparable.
     */
    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        tree = new Tree<>((o1, o2) -> ((Comparable) o1).compareTo(o2));
    }

    /**
     * Constructs set with specified comparator.
     * @param comparator specified comparator for comparing stored keys
     */
    public MyTreeSet(@NotNull Comparator<? super K> comparator) {
        tree = new Tree<>(comparator);
    }

    private MyTreeSet(@NotNull MyTreeSet<K> set, boolean changeOrder) {
        this.tree = set.tree;
        this.ascendingOrder = set.ascendingOrder ^ changeOrder;
    }

    /**
     * Returns number of stored in set elements.
     * @return number of stored in set elements
     */
    @Override
    public int size() {
        return tree.size();
    }

    /**
     * Tells if set is empty.
     * @return {@code true} if set is empty; {@code false} otherwise
     */
    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    /**
     * Adds specified key to set.
     * @param key specified key
     * @return {@code true} if this set did not already contain specified key
     */
    @Override
    public boolean add(@NotNull K key) {
        return tree.add(key);
    }

    /**
     * Checks if set contains specified key.
     * @param key specified key
     * @return true if set contains specified key
     */
    @Override
    public boolean contains(@NotNull Object key) {
        return tree.contains(key);
    }

    /**
     * Removes specified key from set.
     * @param key specified key
     * @return true if set contained specified key
     */
    @Override
    public boolean remove(@NotNull Object key) {
        return tree.remove(key);
    }

    /**
     * Return an iterator over the elements in this set in ascending order.
     * @return an iterator over the elements in this set in ascending order
     */
    @Override
    @NotNull
    public Iterator<K> iterator() {
        return ascendingOrder ? tree.ascendingIterator() : tree.descendingIterator();
    }

    /**
     * Returns an iterator over the elements in this set in descending order.
     * @return an iterator over the elements in this set in descending order
     */
    @Override
    @NotNull
    public Iterator<K> descendingIterator() {
        return ascendingOrder ? tree.descendingIterator() : tree.ascendingIterator();
    }

    /**
     * Returns set that contains the same elements in descending order.
     * @return set that contains the same elements in descending order
     */
    @Override
    @NotNull
    public MyTreeSetInterface<K> descendingSet() {
        return new MyTreeSet<>( this,true);
    }

    /**
     * Returns the least element of the set.
     * @return the least element of the set
     */
    @Override
    public K first() {
        return ascendingOrder ? tree.first() : tree.last();
    }

    /**
     * Returns the greatest element of the set.
     * @return the greatest element of the set
     */
    @Override
    public K last() {
        return ascendingOrder ? tree.last() : tree.first();
    }

    /**
     * Returns the greatest element among those that are strictly less
     * than specified key, or null if there are no such elements.
     * @param key specified key
     * @return the greatest element among those that are strictly less
     * than specified key, or null if there are no such elements
     */
    @Override
    public K lower(@NotNull K key) {
        return ascendingOrder ? tree.lower(key) : tree.higher(key);
    }

    /**
     * Returns the greatest element among those that are less than or equal
     * to the specified key, or null if there are no such elements.
     * @param key specified key
     * @return the greatest element among those that are less than or equal
     * to the specified key, or null if there are no such elements
     */
    @Override
    public K floor(@NotNull K key) {
        return ascendingOrder ? tree.floor(key) : tree.ceiling(key);
    }

    /**
     * Returns the least element among those that are greater than or equal
     * to the specified key, or null if there are no such elements.
     * @param key specified key
     * @return the least element among those that are greater than or equal
     * to the specified key, or null if there are no such elements
     */
    @Override
    public K ceiling(@NotNull K key) {
        return ascendingOrder ? tree.ceiling(key) : tree.floor(key);
    }

    /**
     * Returns the least element among those that are strictly greater
     * than specified key, or null if there are no such elements.
     * @param key specified key
     * @return the least element among those that are strictly greater
     * than specified key, or null if there are no such elements
     */
    @Override
    public K higher(@NotNull K key) {
        return ascendingOrder ? tree.higher(key) : tree.lower(key);
    }

}
