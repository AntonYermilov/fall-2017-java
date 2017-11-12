package me.eranik.utils;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;

/**
 * Implementation of MyTreeSetInterface using binary search tree.
 * @param <K> type of the stored keys
 */
public class MyTreeSet<K> extends AbstractSet<K> implements MyTreeSetInterface<K> {

    private int size = 0;
    private Node root = null;
    private Comparator<? super K> comparator;
    private boolean ascendingOrder = true;

    /**
     * Constructs set with default comparator if stored keys implement Comparable.
     */
    public MyTreeSet() {
        this.comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
    }

    /**
     * Constructs set with specified comparator.
     * @param comparator specified comparator for comparing stored keys
     */
    public MyTreeSet(@NotNull Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    private MyTreeSet(@NotNull MyTreeSet<K> set, boolean changeOrder) {
        this.size = set.size;
        this.root = set.root;
        this.comparator = set.comparator;
        this.ascendingOrder = set.ascendingOrder ^ changeOrder;
    }

    /**
     * Returns number of stored in set elements.
     * @return number of stored in set elements
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Tells if set is empty.
     * @return tells if set is empty
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Adds specified key to set.
     * @param key specified key
     * @return {@code true} if this set did not already contain specified key
     */
    @Override
    public boolean add(@NotNull K key) {
        if (root == null) {
            root = new Node(key);
            size++;
            return true;
        }

        Node parent = null;
        Node node = root;
        int cmpResult = 0;

        while (node != null) {
            cmpResult = comparator.compare(key, node.key);
            if (cmpResult == 0) {
                return false;
            }
            parent = node;
            node = cmpResult < 0 ? node.left : node.right;
        }

        node = new Node(key, parent);
        if (cmpResult < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }
        size++;

        return true;
    }

    /**
     * Checks if specified key contains in set.
     * @param key specified key
     * @return true if set contains specified key
     */
    @Override
    public boolean contains(@NotNull Object key) {
        Node node = findNode(key);
        return node != null;
    }

    /**
     * Removes specified key from set.
     * @param key specified key
     * @return true if set contained specified key
     */
    @Override
    public boolean remove(@NotNull Object key) {
        Node node = findNode(key);
        if (node == null)
            return false;

        size--;
        removeNode(node);
        return true;
    }

    /**
     * Return an iterator over the elements in this set in ascending order.
     * @return an iterator over the elements in this set in ascending order
     */
    @Override
    public Iterator<K> iterator() {
        return new MyTreeSetIterator(false);
    }

    /**
     * Returns an iterator over the elements in this set in descending order.
     * @return an iterator over the elements in this set in descending order
     */
    @Override
    public Iterator<K> descendingIterator() {
        return new MyTreeSetIterator(true);
    }

    /**
     * Returns set that contains the same elements in descending order.
     * @return set that contains the same elements in descending order
     */
    @Override
    public MyTreeSetInterface<K> descendingSet() {
        return new MyTreeSet<>(this, true);
    }

    /**
     * Returns the least element of the set.
     * @return the least element of the set
     */
    @Override
    public K first() {
        Node node = ascendingOrder ? firstNode() : lastNode();
        return node == null ? null : node.key;
    }

    /**
     * Returns the greatest element of the set.
     * @return the greatest element of the set
     */
    @Override
    public K last() {
        Node node = ascendingOrder ? lastNode() : firstNode();
        return node == null ? null : node.key;
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
        Function<K, Boolean> predicate = x ->
                ascendingOrder ? comparator.compare(key, x) <= 0 : comparator.compare(key, x) < 0;
        Node node = findNearestByPredicate(predicate, ascendingOrder);
        return node == null ? null : node.key;
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
        Function<K, Boolean> predicate = x ->
                ascendingOrder ? comparator.compare(key, x) < 0 : comparator.compare(key, x) <= 0;
        Node node = findNearestByPredicate(predicate, ascendingOrder);
        return node == null ? null : node.key;
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
        Function<K, Boolean> predicate = x ->
                ascendingOrder ? comparator.compare(key, x) <= 0 : comparator.compare(key, x) < 0;
        Node node = findNearestByPredicate(predicate, !ascendingOrder);
        return node == null ? null : node.key;
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
        Function<K, Boolean> predicate = x ->
                ascendingOrder ? comparator.compare(key, x) < 0 : comparator.compare(key, x) <= 0;
        Node node = findNearestByPredicate(predicate, !ascendingOrder);
        return node == null ? null : node.key;
    }

    private void removeNode(@NotNull Node node) {
        if (node.left == null || node.right == null) {
            boolean onLeftSide = node.parent != null && node.parent.left == node;
            if (node.left != null) {
                hangOn(node.parent, node.left, onLeftSide);
            } else {
                hangOn(node.parent, node.right, onLeftSide);
            }
            if (node == root) {
                root = node.left != null ? node.left : node.right;
            }
        } else {
            Node next = getNextNode(node);
            swapKeys(node, next);
            removeNode(next);
        }
    }

    private void swapKeys(@NotNull Node first, @NotNull Node second) {
        K key = first.key;
        first.key = second.key;
        second.key = key;
    }

    private void hangOn(Node parent, Node node, boolean leftSide) {
        if (node != null) {
            node.parent = parent;
        }
        if (parent != null) {
            if (leftSide) {
                parent.left = node;
            } else {
                parent.right = node;
            }
        }
    }

    private Node getNextNode(@NotNull Node node) {
        if (node.right != null) {
            node = node.right;
            while (node.left != null) {
                node = node.left;
            }
        } else {
            while (node.parent != null && node.parent.right == node) {
                node = node.parent;
            }
            node = node.parent;
        }
        return node;
    }

    private Node getPreviousNode(@NotNull Node node) {
        if (node.left != null) {
            node = node.left;
            while (node.right != null) {
                node = node.right;
            }
        } else {
            while (node.parent != null && node.parent.left == node) {
                node = node.parent;
            }
            node = node.parent;
        }
        return node;
    }

    private Node findNode(@NotNull Object key) {
        Node node = root;
        while (node != null) {
            int cmpResult = comparator.compare((K) key, node.key);
            if (cmpResult == 0) {
                return node;
            }
            node = cmpResult < 0 ? node.left : node.right;
        }
        return null;
    }

    private Node firstNode() {
        Node node = root;
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node lastNode() {
        Node node = root;
        while (node != null && node.right != null) {
            node = node.right;
        }
        return node;
    }

    private Node findNearestByPredicate(@NotNull Function<K, Boolean> predicate, boolean lowerSide) {
        Node node = root;
        Node result = null;
        while (node != null) {
            if (predicate.apply(node.key)) {
                if (!lowerSide) {
                    result = node;
                }
                node = node.left;
            } else {
                if (lowerSide) {
                    result = node;
                }
                node = node.right;
            }
        }
        return result;
    }

    private class Node {
        Node left = null;
        Node right = null;
        Node parent = null;
        K key;

        Node(K key) {
            this.key = key;
        }

        Node(K key, Node parent) {
            this.key = key;
            this.parent = parent;
        }
    }

    private class MyTreeSetIterator implements Iterator<K> {
        boolean ascendingOrder = MyTreeSet.this.ascendingOrder;
        Node first = firstNode();
        Node last = lastNode();
        Node currentNode = null;

        MyTreeSetIterator(boolean changeOrder) {
            ascendingOrder ^= changeOrder;
        }

        @Override
        public boolean hasNext() {
            return currentNode != (ascendingOrder ? last : first);
        }

        void nextAscendingOrder() {
            if (currentNode == null) {
                currentNode = first;
                return;
            }
            currentNode = getNextNode(currentNode);
        }

        void nextDescendingOrder() {
            if (currentNode == null) {
                currentNode = last;
                return;
            }
            currentNode = getPreviousNode(currentNode);
        }

        @Override
        public K next() {
            if (ascendingOrder) {
                nextAscendingOrder();
            } else {
                nextDescendingOrder();
            }
            return currentNode.key;
        }
    }

}
