package me.eranik.util;

/**
 * Stores keys in binary search tree. Supports adding new values.
 * Stored keys should implement interface {@code Comparable}.
 * @param <T> key type
 */
public class Set<T extends Comparable<? super T>> {

    private class Node {
        private Node left = null;
        private Node right = null;
        private int weight = 1;
        private T key;

        private Node(T key) {
            this.key = key;
        }
    }

    private Node root = null;

    /**
     * Adds specified key to set.
     * @param key element to be added
     * @return {@code true} if key didn't exist before; {@code false} otherwise
     */
    public boolean add(T key) {
        if (contains(key))
            return false;
        if (root == null) {
            root = new Node(key);
            return true;
        }

        Node node = root, parent = null;
        while (node != null) {
            parent = node;
            node.weight++;
            node = node.key.compareTo(key) < 0 ? node.right : node.left;
        }
        if (parent.key.compareTo(key) < 0) {
            parent.right = new Node(key);
        } else {
            parent.left = new Node(key);
        }
        return true;
    }

    /**
     * Tells if specified key contains in set.
     * @param key element to be checked
     * @return {@code true} if element exists; {@code false} otherwise
     */
    public boolean contains(T key) {
        Node node = root;
        while (node != null) {
            int cmp = node.key.compareTo(key);
            if (cmp == 0) {
                break;
            }
            node = cmp < 0 ? node.right : node.left;
        }
        return node != null;
    }

    /**
     * Returns the number of stored elements
     * @return the number of stored elements
     */
    public int size() {
        return root == null ? 0 : root.weight;
    }

}
