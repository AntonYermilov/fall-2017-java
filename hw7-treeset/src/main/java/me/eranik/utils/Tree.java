package me.eranik.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Implementation of binary search tree.
 * @param <K> type of stored keys
 */
public class Tree<K> {

    private int size = 0;
    private Node root = null;
    private Comparator<? super K> comparator;

    /**
     * Constructs tree with specified comparator.
     * @param comparator specified comparator for comparing stored keys
     */
    public Tree(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    /**
     * Returns number of stored in tree elements.
     * @return number of stored in tree elements
     */
    public int size() {
        return size;
    }

    /**
     * Tells if tree is empty.
     * @return {@code true} if tree is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Adds specified key to tree.
     * @param key specified key
     * @return {@code true} if this tree did not already contain specified key
     */
    public boolean add(@NotNull K key) {
        if (root == null) {
            root = new Node(key, null);
            size++;
            return true;
        }

        if (contains(key)) {
            return false;
        }

        Node node = lowerBound(key);
        if (node == null || node.left != null) {
            node = lastNode(node == null ? root : node.left);
            node.right = new Node(key, node);
        } else {
            node.left = new Node(key, node);
        }
        size++;

        return true;
    }

    /**
     * Checks if tree contains specified key.
     * @param key specified key
     * @return true if tree contains specified key
     */
    public boolean contains(@NotNull Object key) {
        Node node = lowerBound(key);
        return node != null && node.key.equals(key);
    }

    /**
     * Removes specified key from tree.
     * @param key specified key
     * @return true if tree contained specified key
     */
    public boolean remove(@NotNull Object key) {
        if (!contains(key)) {
            return false;
        }

        Node node = lowerBound(key);
        removeNode(node);
        return true;
    }

    /**
     * Returns the least element of the tree.
     * @return the least element of the tree
     */
    public K first() {
        Node node = firstNode(root);
        return node == null ? null : node.key;
    }

    /**
     * Returns the greatest element of the tree.
     * @return the greatest element of the tree
     */
    public K last() {
        Node node = lastNode(root);
        return node == null ? null : node.key;
    }

    /**
     * Returns the greatest element among those that are strictly less
     * than specified key, or null if there are no such elements.
     * @param key specified key
     * @return the greatest element among those that are strictly less
     * than specified key, or null if there are no such elements
     */
    public K lower(@NotNull K key) {
        Node node = lowerBound(key);
        if (node == null) {
            return last();
        }

        node = getPreviousNode(node);
        return node == null ? null : node.key;
    }

    /**
     * Returns the greatest element among those that are less than or equal
     * to the specified key, or null if there are no such elements.
     * @param key specified key
     * @return the greatest element among those that are less than or equal
     * to the specified key, or null if there are no such elements
     */
    public K floor(@NotNull K key) {
        Node node = upperBound(key);
        if (node == null) {
            return last();
        }

        node = getPreviousNode(node);
        return node == null ? null : node.key;
    }

    /**
     * Returns the least element among those that are greater than or equal
     * to the specified key, or null if there are no such elements.
     * @param key specified key
     * @return the least element among those that are greater than or equal
     * to the specified key, or null if there are no such elements
     */
    public K ceiling(@NotNull K key) {
        Node node = lowerBound(key);
        return node == null ? null : node.key;
    }

    /**
     * Returns the least element among those that are strictly greater
     * than specified key, or null if there are no such elements.
     * @param key specified key
     * @return the least element among those that are strictly greater
     * than specified key, or null if there are no such elements
     */
    public K higher(@NotNull K key) {
        Node node = upperBound(key);
        return node == null ? null : node.key;
    }

    /**
     * Return an iterator over the elements in this tree in ascending order.
     * @return an iterator over the elements in this tree in ascending order
     */
    @NotNull
    public Iterator<K> ascendingIterator() {
        return new TreeAscendingIterator();
    }

    /**
     * Returns an iterator over the elements in this tree in descending order.
     * @return an iterator over the elements in this tree in descending order
     */
    @NotNull
    public Iterator<K> descendingIterator() {
        return new TreeDescendingIterator();
    }

    private Node lowerBound(@NotNull Object key) {
        Node node = root;
        Node result = null;

        while (node != null) {
            int cmpResult = comparator.compare((K) key, node.key);
            if (cmpResult <= 0) {
                result = node;
                node = node.left;
            } else {
                node = node.right;
            }
        }

        return result;
    }

    private Node upperBound(@NotNull Object key) {
        Node node = root;
        Node result = null;

        while (node != null) {
            int cmpResult = comparator.compare((K) key, node.key);
            if (cmpResult < 0) {
                result = node;
                node = node.left;
            } else {
                node = node.right;
            }
        }

        return result;
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
            size--;
        } else {
            Node next = getNextNode(node);
            swapKeys(node, next);
            removeNode(next);
        }
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

    private void swapKeys(@NotNull Node first, @NotNull Node second) {
        K key = first.key;
        first.key = second.key;
        second.key = key;
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

    private Node firstNode(Node node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node lastNode(Node node) {
        while (node != null && node.right != null) {
            node = node.right;
        }
        return node;
    }

    private class Node {
        Node left = null;
        Node right = null;
        Node parent = null;
        K key;

        Node(K key, Node parent) {
            this.key = key;
            this.parent = parent;
        }
    }

    private class TreeAscendingIterator implements Iterator<K> {
        Node first = firstNode(root);
        Node last = lastNode(root);
        Node currentNode = null;

        @Override
        public boolean hasNext() {
            return currentNode != last;
        }

        @Override
        public K next() {
            currentNode = currentNode == null ? first : getNextNode(currentNode);
            return currentNode.key;
        }
    }

    private class TreeDescendingIterator implements Iterator<K> {
        Node first = firstNode(root);
        Node last = lastNode(root);
        Node currentNode = null;

        @Override
        public boolean hasNext() {
            return currentNode != first;
        }

        @Override
        public K next() {
            currentNode = currentNode == null ? last : getPreviousNode(currentNode);
            return currentNode.key;
        }
    }
}
