package me.eranik.util;

import java.io.*;
import java.util.HashMap;

public class Trie implements Serializable {

    /**
     * Class {@code Node} stores such information as links to children,
     * number of strings, that starts with corresponding prefix, and
     * special flag to tell if current node is terminal.
     *
     * Links to children are stored as hash map of characters, each link
     * matches a special character.
     */
    private class Node implements Serializable {
        private HashMap<Character, Node> to = new HashMap<>();
        private boolean isEnd = false;
        private int weight = 0;

        /**
         * Moves you to another node through given symbol. If link to another
         * node does not exists, creates new one.
         * @param symbol symbol to go through
         * @return node corresponding to the path by symbol
         */
        private Node move(char symbol) {
            if (!to.containsKey(symbol))
                to.put(symbol, new Node());
            return to.get(symbol);
        }

        /**
         * Tells if move through specified symbol is valid and if the next node
         * to move exists.
         * @param symbol symbol to go through
         * @return true if link exists; false otherwise
         */
        private boolean canMove(char symbol) {
            return to.containsKey(symbol);
        }
    }

    private Node root = new Node();

    /**
     * Adds string to trie or tells that specified string already exists.
     * @param word string to be added
     * @return true if specified string did not exist until now
     */
    public boolean add(String word) {
        if (contains(word))
            return false;

        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            node.weight++;
            node = node.move(word.charAt(i));
        }
        node.weight++;

        if (node.isEnd)
            return false;
        node.isEnd = true;
        return true;
    }

    /**
     * Tells if trie contains specified string.
     * @param word string to be verified
     * @return true if trie contains specified string; false otherwise
     */
    public boolean contains(String word) {
        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            if (!node.canMove(word.charAt(i))) {
                return false;
            }
            node = node.move(word.charAt(i));
        }
        return node.isEnd;
    }

    /**
     * Removes string from trie or tells that it does not exist.
     * @param word string to be removed
     * @return true if string was successfully removed; false otherwise
     */
    public boolean remove(String word) {
        if (!contains(word))
            return false;

        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            node.weight--;

            node = node.move(word.charAt(i));
        }
        node.weight--;
        node.isEnd = false;
        return true;
    }

    /**
     * @return number of strings stored in trie
     */
    public int size() {
        return root.weight;
    }

    /**
     * Returns number of strings that starts with specified prefix.
     * @param prefix prefix of some string
     * @return number of strings that starts with specified prefix
     */
    public int startsWith(String prefix) {
        Node node = root;
        for (int i = 0; i < prefix.length(); i++) {
            if (!node.canMove(prefix.charAt(i)))
                return 0;
            node = node.move(prefix.charAt(i));
        }
        return node.weight;
    }

    /**
     * Serializes trie to the output stream.
     * @param out output stream
     * @throws IOException
     */
    public void serialize(OutputStream out) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(this);
        oos.close();
    }

    /**
     * Replaces trie with new one from input stream.
     * @param in input stream
     * @throws IOException
     */
    public void deserialize(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(in);
        root = ((Trie) ois.readObject()).root;
        ois.close();
    }

}